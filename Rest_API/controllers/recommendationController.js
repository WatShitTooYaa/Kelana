const db = require('../config/db');

const Recommendations = async (req, res) => {
    try {
        const { category, limit, startAfter } = req.query;

        if (!category) {
            return res.status(400).json({ message: 'Category is required' });
        }

        let query = db.collection('indonesia_tourism').where('Category', '==', category);

        if (startAfter) {
            const startAfterDoc = await db.collection('indonesia_tourism').doc(startAfter).get();
            query = query.startAfter(startAfterDoc);
        }

        if (limit) {
            query = query.limit(Number(limit));
        }

        const snapshot = await query.get();
        if (snapshot.empty) {
            return res.status(404).json({ message: 'No matching documents.' });
        }

        let recommendations = [];
        snapshot.forEach(doc => {
            const { Place_Name, image1 } = doc.data();
            recommendations.push({ Place_Name, image1 });
        });

        res.status(200).json(recommendations);
    } catch (error) {
        console.error('Error getting recommendations:', error);
        res.status(500).json({ message: 'Internal server error' });
    }
};

module.exports = { Recommendations };
