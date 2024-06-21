const express = require('express');
const { addLike, removeLike, getLikedTourism } = require('../controllers/likeController');
const authMiddleware = require('../middlewares/authMiddleware');
const router = express.Router();

router.post('/like', authMiddleware, addLike);
router.delete('/like', authMiddleware, removeLike);
router.get('/likedTourism', authMiddleware, getLikedTourism);

module.exports = router;
