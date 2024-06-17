const express = require('express');
const { getRecommendedPlaces } = require('../controllers/recommendationController');
const { getNearbyPlaces } = require('../controllers/nearbyController');
const router = express.Router();

router.get('/recommendations', getRecommendedPlaces);
router.get('/nearby', getNearbyPlaces);

module.exports = router;
