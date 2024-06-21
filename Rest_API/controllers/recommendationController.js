const db = require('../config/db');

const Recommendations = async (req, res) => {
    try {
        const { category, limit = 20, startAfter } = req.query;
        const user_id = req.user; 

        if (!category) {
            return res.status(400).json({ message: 'Category is required' });
        }

        
        let query = db.collection('indonesia_tourism').where('Category', '==', category);

        if (startAfter) {
            const startAfterDoc = await db.collection('indonesia_tourism').doc(startAfter).get();
            query = query.startAfter(startAfterDoc);
        }

        const fetchLimit = 50; 
        query = query.limit(fetchLimit);

        const snapshot = await query.get();
        if (snapshot.empty) {
            return res.status(404).json({ message: 'No matching documents.' });
        }

        let allRecommendations = [];
        snapshot.forEach(doc => {
            const { Place_Id, Place_Name, Photo_URL, Description, Rating, Price, City, Category, Lat, Long } = doc.data();
            allRecommendations.push({ 
                id: doc.id, 
                place_id: Place_Id || null, 
                Place_Name: Place_Name || null, 
                image: Photo_URL || null, 
                description: Description || null, 
                rating: Rating || null, 
                price: Price || 0, 
                city: City || null, 
                category: Category || null, 
                lat: Lat || null, 
                long: Long || null 
            });
        });

        
        const recommendations = [];
        while (recommendations.length < limit && allRecommendations.length > 0) {
            const randomIndex = Math.floor(Math.random() * allRecommendations.length);
            recommendations.push(allRecommendations.splice(randomIndex, 1)[0]);
        }

        
        const userSnapshot = await db.collection('users').doc(user_id).get();
        if (!userSnapshot.exists) {
            return res.status(404).json({ message: 'User not found' });
        }

        const { username, email } = userSnapshot.data();

        const finalRecommendations = recommendations.map(recommendation => {
            const recommendation_id = db.collection('recommendations').doc().id;
            const recommendationData = {
                recommendation_id, 
                place_id: recommendation.place_id,
                Place_Name: recommendation.Place_Name,
                image: recommendation.image,
                description: recommendation.description,
                rating: recommendation.rating,
                price: recommendation.price,
                city: recommendation.city,
                category: recommendation.category,
                lat: recommendation.lat,
                long: recommendation.long,
                user_id,
            };
            db.collection('recommendations').doc(recommendation_id).set(recommendationData); 
            return recommendationData; 
        });

        res.status(200).json(finalRecommendations);
    } catch (error) {
        console.error('Error getting recommendations:', error);
        res.status(500).json({ message: 'Internal server error' });
    }
};

module.exports = { Recommendations };
