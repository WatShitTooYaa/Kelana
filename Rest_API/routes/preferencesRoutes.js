const express = require('express');
const { getCategories } = require('../controllers/categoryController');
const { savePreferences } = require('../controllers/preferencesController');
const router = express.Router();

router.get('/categories', getCategories);
router.post('/preferences', savePreferences);

module.exports = router;
