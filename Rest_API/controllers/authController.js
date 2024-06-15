const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const db = require('../config/db');

const register = async (req, res) => {
  try {
    const { email, password } = req.body;
    console.log('Register request received:', { email, password });

    const existingUser = await db.collection('users').where('email', '==', email).get();
    if (!existingUser.empty) {
      return res.status(400).json({ message: 'User already exists' });
    }

    const hashedPassword = await bcrypt.hash(password, 10);
    console.log('Hashed Password:', hashedPassword);

    const newUser = {
      email,
      password: hashedPassword,
      createdAt: new Date().toISOString()
    };

    const userRef = await db.collection('users').add(newUser);
    res.status(201).json({ message: 'User registered successfully', userId: userRef.id });
  } catch (error) {
    console.error('Error during registration:', error);
    res.status(500).json({ message: 'Internal server error', error });
  }
};

const login = async (req, res) => {
  try {
    const { email, password } = req.body;
    console.log('Login request received:', { email, password });

    const userSnapshot = await db.collection('users').where('email', '==', email).get();
    if (userSnapshot.empty) {
      console.log('No user found with that email');
      return res.status(400).json({ message: 'Invalid credentials' });
    }

    const user = userSnapshot.docs[0].data();
    console.log('User found:', user);

    const isMatch = await bcrypt.compare(password, user.password);
    console.log('Password match:', isMatch);

    if (!isMatch) {
      console.log('Password does not match');
      return res.status(400).json({ message: 'Invalid credentials' });
    }

    const token = jwt.sign({ userId: userSnapshot.docs[0].id }, process.env.JWT_SECRET, { expiresIn: '1h' });
    console.log('Token generated:', token);
    res.json({ message: 'Login successful', token });
  } catch (error) {
    console.error('Error during login:', error);
    res.status(500).json({ message: 'Internal server error', error });
  }
};

module.exports = { register, login };
