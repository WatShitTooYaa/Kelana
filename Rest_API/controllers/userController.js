const db = require('../config/db');

// Get user profile
const getUserProfile = async (req, res) => {
    try {
        const { email } = req.query;

        if (!email) {
            return res.status(400).json({ message: 'Email is required' });
        }

        const userSnapshot = await db.collection('users').where('email', '==', email).get();
        if (userSnapshot.empty) {
            return res.status(404).json({ message: 'User not found' });
        }

        const user = userSnapshot.docs[0].data();
        res.status(200).json(user);
    } catch (error) {
        console.error('Error getting user profile:', error);
        res.status(500).json({ message: 'Internal server error' });
    }
};

// Create a new user
const createUser = async (req, res) => {
    try {
        const { email, password, username } = req.body;

        if (!email || !password || !username) {
            return res.status(400).json({ message: 'Email, username and password are required' });
        }

        const userSnapshot = await db.collection('users').where('email', '==', email).get();
        if (!userSnapshot.empty) {
            return res.status(400).json({ message: 'User already exists' });
        }

        const newUser = { email, password, username };
        const userRef = await db.collection('users').add(newUser);
        res.status(201).json({ message: 'User created successfully', userId: userRef.id });
    } catch (error) {
        console.error('Error creating user:', error);
        res.status(500).json({ message: 'Internal server error' });
    }
};

// Update user profile
const updateUserProfile = async (req, res) => {
    try {
        const { email, updates } = req.body;

        if (!email || !updates) {
            return res.status(400).json({ message: 'Email and updates are required' });
        }

        const userSnapshot = await db.collection('users').where('email', '==', email).get();
        if (userSnapshot.empty) {
            return res.status(404).json({ message: 'User not found' });
        }

        const userId = userSnapshot.docs[0].id;
        await db.collection('users').doc(userId).update(updates);

        res.status(200).json({ message: 'User profile updated successfully' });
    } catch (error) {
        console.error('Error updating user profile:', error);
        res.status(500).json({ message: 'Internal server error' });
    }
};

// Delete user profile
const deleteUserProfile = async (req, res) => {
    try {
        const { email } = req.body;

        if (!email) {
            return res.status(400).json({ message: 'Email is required' });
        }

        const userSnapshot = await db.collection('users').where('email', '==', email).get();
        if (userSnapshot.empty) {
            return res.status(404).json({ message: 'User not found' });
        }

        const userId = userSnapshot.docs[0].id;
        await db.collection('users').doc(userId).delete();

        res.status(200).json({ message: 'User profile deleted successfully' });
    } catch (error) {
        console.error('Error deleting user profile:', error);
        res.status(500).json({ message: 'Internal server error' });
    }
};

module.exports = { getUserProfile, createUser, updateUserProfile, deleteUserProfile };
