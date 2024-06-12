const { Firestore } = require('@google-cloud/firestore');

async function storeData(id, data) {
  const db = new Firestore({
    databaseId: ''
  });

  const predictCollection = db.collection('');
  return predictCollection.doc(id).set(data);
}

module.exports = storeData;
