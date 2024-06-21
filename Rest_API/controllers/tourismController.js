const db = require('../config/db');

const getTourismDetail = async (req, res) => {
  try {
    const { place_id } = req.query;

    if (!place_id) {
      return res.status(400).json({ message: 'Place Id is required' });
    }

    const placeIdNumber = Number(place_id); // Convert place_id to number

    console.log(`Fetching tourism detail for place_id: ${placeIdNumber}`);

    const tourismSnapshot = await db.collection('indonesia_tourism')
      .where('Place_Id', '==', placeIdNumber)
      .get();

    if (tourismSnapshot.empty) {
      console.log(`No tourism found for place_id: ${placeIdNumber}`);
      return res.status(404).json({ message: 'Tourism not found' });
    }

    const tourismData = tourismSnapshot.docs[0].data();
    console.log(`Tourism data found: ${JSON.stringify(tourismData)}`);

    res.status(200).json(tourismData);
  } catch (error) {
    console.error('Error fetching tourism detail:', error);
    res.status(500).json({ message: 'Internal server error' });
  }
};

module.exports = { getTourismDetail };
