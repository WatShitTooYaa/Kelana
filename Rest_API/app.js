require('dotenv').config();

const express = require('express');
const app = express();
const port = 3000;

app.use(express.json());

app.use(express.static('public'));

const authRoutes = require('./routes/authRoutes');
app.use('/auth', authRoutes);

app.listen(port, () => {
    console.log(`Server berjalan di http://localhost:${port}`);
});
