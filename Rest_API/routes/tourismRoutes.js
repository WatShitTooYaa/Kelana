const express = require('express');
const { getTourismDetail } = require('../controllers/tourismController');
const authMiddleware = require('../middlewares/authMiddleware');
const router = express.Router();

router.get('/detail', authMiddleware, getTourismDetail);

module.exports = router;
