const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const db = require('../config/db');

// Registrasi Pengguna
const register = async (req, res) => {
  try {
    const { email, password, username } = req.body;
    console.log('Register request received:', { email, password, username });

    const existingUser = await db.collection('users').where('email', '==', email).get();
    if (!existingUser.empty) {
      return res.status(400).json({ message: 'User already exists' });
    }

    const hashedPassword = await bcrypt.hash(password, 10);
    console.log('Hashed Password:', hashedPassword);

    const newUser = {
      email,
      password: hashedPassword,
      username,
      createdAt: new Date().toISOString()
    };

    const userRef = await db.collection('users').add(newUser);
    const userId = userRef.id;

    // Update user document with user_id
    await userRef.update({ user_id: userId });

    res.status(201).json({ message: 'User registered successfully', userId });
  } catch (error) {
    console.error('Error during registration:', error);
    res.status(500).json({ message: 'Internal server error', error });
  }
};

// Login Pengguna
const login = async (req, res) => {
  try {
    const { email, password } = req.body;
    console.log('Login request received:', { email, password });

    const userSnapshot = await db.collection('users').where('email', '==', email).get();
    if (userSnapshot.empty) {
      console.log('No user found with that email');
      return res.status(400).json({ message: 'Invalid credentials' });
    }

    const userDoc = userSnapshot.docs[0];
    const user = userDoc.data();
    console.log('User found:', user);

    const isMatch = await bcrypt.compare(password, user.password);
    console.log('Password match:', isMatch);

    if (!isMatch) {
      console.log('Password does not match');
      return res.status(400).json({ message: 'Invalid credentials' });
    }

    const token = jwt.sign({ userId: userDoc.id }, process.env.JWT_SECRET, { expiresIn: '1h' });
    console.log('Token generated:', token);
    
    // Update user document with user_id if it doesn't exist
    if (!user.user_id) {
      await userDoc.ref.update({ user_id: userDoc.id });
    }

    res.json({ message: 'Login successful', token, userId: userDoc.id });
  } catch (error) {
    console.error('Error during login:', error);
    res.status(500).json({ message: 'Internal server error', error });
  }
};

// Update Pengguna
const updateUser = async (req, res) => {
  try {
    const user_id = req.user;
    const { email, password, username } = req.body;

    const updates = {};
    if (email) updates.email = email;
    if (username) updates.username = username;
    if (password) {
      const hashedPassword = await bcrypt.hash(password, 10);
      updates.password = hashedPassword;
    }

    const userRef = db.collection('users').doc(user_id);
    await userRef.update(updates);

    res.status(200).json({ message: 'User updated successfully' });
  } catch (error) {
    console.error('Error updating user:', error);
    res.status(500).json({ message: 'Internal server error' });
  }
};

const getUser = async (req, res) => {
  try {
    const user_id = req.user;
    const userSnapshot = await db.collection('users').doc(user_id).get();

    if (!userSnapshot.exists) {
      return res.status(404).json({ message: 'User not found' });
    }

    const user = userSnapshot.data();
    res.status(200).json({ user });
  } catch (error) {
    console.error('Error fetching user data:', error);
    res.status(500).json({ message: 'Internal server error' });
  }
};

module.exports = { register, login, updateUser, getUser };

