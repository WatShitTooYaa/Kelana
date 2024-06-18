require('dotenv').config();

const express = require('express');
const app = express();
const port = 3000;

app.use(express.json());
app.use(express.static('public'));

// Routes
const authRoutes = require('./routes/authRoutes');
const userRoutes = require('./routes/userRoutes');
const recommendationRoutes = require('./routes/recommendationRoutes');
const categoryRoutes = require('./routes/categoryRoutes');
const tripPlannerRoutes = require('./routes/tripPlannerRoutes');
const likeRoutes = require('./routes/likeRoutes');

app.use('/auth', authRoutes);
app.use('/users', userRoutes);
app.use('/recommendations', recommendationRoutes);
app.use('/category', categoryRoutes);
app.use('/tripplanner', tripPlannerRoutes);
app.use('/likes', likeRoutes);

app.listen(port, () => {
  console.log(`Server running at http://localhost:${port}`);
});
