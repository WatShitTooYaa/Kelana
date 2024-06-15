const express = require('express');
const authMiddleware = require('../middlewares/authMiddleware');
const { getUserData } = require('../controllers/userController');
const router = express.Router();

router.get('/profile', authMiddleware, getUserData);

module.exports = router;
