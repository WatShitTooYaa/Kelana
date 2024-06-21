const db = require('../config/db');
const { FieldValue } = require('firebase-admin/firestore');

// Tambah Like
const addLike = async (req, res) => {
  try {
    const { place_id } = req.body;
    const user_id = req.user;

    // Cek apakah sudah di-like
    const likeSnapshot = await db.collection('user_likes')
      .where('user_id', '==', user_id)
      .where('place_id', '==', place_id)
      .get();

    if (!likeSnapshot.empty) {
      return res.status(400).json({ message: 'Already liked' });
    }

    const newLike = {
      user_id,
      place_id,
      createdAt: FieldValue.serverTimestamp(),
    };

    await db.collection('user_likes').add(newLike);
    res.status(201).json({ message: 'Tourism liked successfully' });
  } catch (error) {
    console.error('Error adding like:', error);
    res.status(500).json({ message: 'Internal server error' });
  }
};

// Hapus Like
const removeLike = async (req, res) => {
  try {
    const { place_id } = req.body;
    const user_id = req.user;

    const likeSnapshot = await db.collection('user_likes')
      .where('user_id', '==', user_id)
      .where('place_id', '==', place_id)
      .get();

    if (likeSnapshot.empty) {
      return res.status(400).json({ message: 'Like not found' });
    }

    const likeId = likeSnapshot.docs[0].id;
    await db.collection('user_likes').doc(likeId).delete();
    res.status(200).json({ message: 'Tourism unliked successfully' });
  } catch (error) {
    console.error('Error removing like:', error);
    res.status(500).json({ message: 'Internal server error' });
  }
};

// Dapatkan Daftar Wisata yang Di-Like
const getLikedTourism = async (req, res) => {
  try {
    const user_id = req.user;

    const likeSnapshot = await db.collection('user_likes')
      .where('user_id', '==', user_id)
      .get();

    if (likeSnapshot.empty) {
      return res.status(200).json({ likedTourism: [] });
    }

    const placeIds = likeSnapshot.docs.map(doc => doc.data().place_id);
    console.log(`Found liked placeIds: ${placeIds}`);

    const likedTourism = placeIds.map(id => ({ place_id: id }));

    res.status(200).json({ likedTourism });
  } catch (error) {
    console.error('Error fetching liked tourism:', error);
    res.status(500).json({ message: 'Internal server error' });
  }
};

module.exports = { addLike, removeLike, getLikedTourism };
