const express = require('express');
const { Recommendations } = require('../controllers/recommendationController');
const authMiddleware = require('../middlewares/authMiddleware'); // Pastikan ini mengarah ke file middleware yang benar
const router = express.Router();

router.get('/', authMiddleware, Recommendations);

module.exports = router;
