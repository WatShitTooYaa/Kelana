const express = require('express');
const router = express.Router();
const { register, login, updateUser, getUser } = require('../controllers/authController');
const authMiddleware = require('../middlewares/authMiddleware');

router.post('/register', register);
router.post('/login', login);
router.put('/update', authMiddleware, updateUser);
router.get('/me', authMiddleware, getUser);

module.exports = router;
