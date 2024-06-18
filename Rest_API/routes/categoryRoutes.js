const express = require('express');
const { Category } = require('../controllers/categoryController');
const router = express.Router();

router.get('/', Category);

module.exports = router;
