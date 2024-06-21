const db = require('../config/db');

const Category = async (req, res) => {
    try {
        const snapshot = await db.collection('indonesia_tourism').get();
        if (snapshot.empty) {
            return res.status(404).json({ message: 'No categories found.' });
        }

        let categories = [];
        snapshot.forEach(doc => {
            const data = doc.data();
            if (data.Category && !categories.includes(data.Category)) {
                categories.push(data.Category);
            }
        });

        res.status(200).json(categories);
    } catch (error) {
        console.error('Error getting categories:', error);
        res.status(500).json({ message: 'Internal server error' });
    }
};

module.exports = { Category };
