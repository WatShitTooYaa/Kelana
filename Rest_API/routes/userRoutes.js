const express = require('express');
const { getUserProfile, createUser, updateUserProfile, deleteUserProfile } = require('../controllers/userController');
const router = express.Router();

router.get('/profile', getUserProfile);
router.post('/profile', createUser);
router.put('/profile', updateUserProfile);
router.delete('/profile', deleteUserProfile);

module.exports = router;
