const express = require('express');
const { generateTourPlan } = require('../controllers/tripPlannerController');
const router = express.Router();

router.get('/', generateTourPlan);

module.exports = router;
