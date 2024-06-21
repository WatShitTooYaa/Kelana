const express = require('express');
const { Recommendations } = require('../controllers/recommendationController');
const router = express.Router();

router.get('/', Recommendations);

module.exports = router;
