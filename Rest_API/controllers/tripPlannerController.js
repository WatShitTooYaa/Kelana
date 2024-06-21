const db = require('../config/db');

async function createDataModel(city, price, destinationLimitPerDay, numDays) {
    const snapshot = await db.collection('indonesia_tourism').get();
    const distanceSnapshot = await db.collection(`${city.toLowerCase()}_distances`).get();

    if (snapshot.empty || distanceSnapshot.empty) {
        throw new Error('No data available');
    }

    const tourismData = [];
    snapshot.forEach(doc => tourismData.push(doc.data()));

    const distanceData = [];
    distanceSnapshot.forEach(doc => distanceData.push(doc.data()));

    const uniqueDestinations = [...new Set(distanceData.map(item => [item.Source, item.Destination]).flat())];
    const destinationIndex = Object.fromEntries(uniqueDestinations.map((dest, index) => [dest, index]));

    const distanceMatrix = Array.from({ length: uniqueDestinations.length }, () => Array(uniqueDestinations.length).fill(0));
    distanceData.forEach(({ Source, Destination, Distance }) => {
        const sourceIndex = destinationIndex[Source];
        const destIndex = destinationIndex[Destination];
        distanceMatrix[sourceIndex][destIndex] = Distance;
    });

    // Remove the city name from the end of each place name
    const cityRegex = new RegExp(`\\s*${city}$`, 'i');
    const cleanedPlaceNames = uniqueDestinations.map(dest => dest.replace(cityRegex, ''));

    return {
        distanceMatrix,
        prices: tourismData.map(item => item.Price),
        averageSpentTime: tourismData.map(item => item.Time_Minutes),
        priceThreshold: price,
        destinationLimitPerDay: destinationLimitPerDay,
        numDays: numDays,
        numVehicles: 1,
        depot: 0,
        placeNames: cleanedPlaceNames,
        photo_url: tourismData.map(item => item.Photo_URL),
        lat: tourismData.map(item => item.Lat),
        long: tourismData.map(item => item.Long)
    };
}

async function selectHotelWithinBudget(city, remainingBudget) {
    const hotelsSnapshot = await db.collection(`${city.toLowerCase()}_hotels`).get();
    const hotels = [];
    hotelsSnapshot.forEach(doc => hotels.push(doc.data()));

    const validHotels = hotels.filter(hotel => hotel.Price * 3 <= remainingBudget);
    if (validHotels.length === 0) return null;

    const randomIndex = Math.floor(Math.random() * validHotels.length);
    return validHotels[randomIndex];
}

async function generateTourPlan(req, res) {
    try {
        const { city, priceThreshold, destinationLimitPerDay, numDays } = req.query;
        const data = await createDataModel(city, parseInt(priceThreshold), parseInt(destinationLimitPerDay), parseInt(numDays));

        const selectedHotel = await selectHotelWithinBudget(city, data.priceThreshold);
        if (!selectedHotel) {
            return res.status(400).json({ message: 'No hotel found within budget' });
        }

        const hotelPricePerNight = selectedHotel.Price;
        const remainingBudget = data.priceThreshold - (hotelPricePerNight * data.numDays);
        if (remainingBudget < 0) {
            return res.status(400).json({ message: 'Not enough budget for the selected hotel' });
        }

        let totalRoute = [];
        for (let i = 0; i < data.placeNames.length; i++) {
            totalRoute.push(i);
        }
        totalRoute.push(0);

        const stopsPerDay = data.destinationLimitPerDay;
        const numDestinations = totalRoute.length - 1;

        const allDaysData = [];
        let totalPrice = hotelPricePerNight * data.numDays;

        for (let day = 0; day < data.numDays; day++) {
            const startIndex = day * stopsPerDay;
            const endIndex = Math.min(startIndex + stopsPerDay, numDestinations);
            const dayRoute = totalRoute.slice(startIndex, endIndex);

            if (dayRoute.length === 0) break;

            const dayData = {
                day: day + 1,
                stops: []
            };

            let dayPrice = 0;

            for (let i = 0; i < dayRoute.length - 1; i++) {
                const fromNode = dayRoute[i];
                const toNode = dayRoute[i + 1];
                const destinationName = data.placeNames[fromNode];
                const destinationPrice = data.prices[fromNode];
                const destinationPhoto_URL = data.photo_url[fromNode];
                const destinationLat = data.lat[fromNode];
                const destinationLong = data.long[fromNode];

                if (destinationPrice !== undefined) {
                    dayPrice += destinationPrice;
                }

                if (totalPrice + destinationPrice > data.priceThreshold) {
                    break; // Stop adding more destinations if price threshold is exceeded
                }

                dayData.stops.push({
                    name: destinationName,
                    price: destinationPrice,
                    photo_url: destinationPhoto_URL,
                    lat: destinationLat,
                    long: destinationLong
                });

                totalPrice += destinationPrice;
            }

            dayData.total_price = dayPrice;

            allDaysData.push(dayData);
        }

        const finalResult = {
            total_price: totalPrice,
            routes: allDaysData,
            selected_hotel: selectedHotel.name,
            hotel_price_per_night: hotelPricePerNight
        };

        // Clean up data by replacing undefined values with null or 0
        function cleanData(obj) {
            for (const key in obj) {
                if (obj[key] === undefined) {
                    obj[key] = null; // or obj[key] = 0 if appropriate
                } else if (typeof obj[key] === 'object' && obj[key] !== null) {
                    cleanData(obj[key]);
                }
            }
        }

        cleanData(finalResult);

        const tourPlanRef = await db.collection('tour_plans').add(finalResult);
        finalResult.id = tourPlanRef.id;

        res.status(200).json(finalResult);
    } catch (error) {
        console.error('Error generating tour plan:', error);
        res.status(500).json({ message: 'Internal server error' });
    }
}

module.exports = { generateTourPlan };
