require('dotenv').config();

const express = require('express');
const app = express();
const port = process.env.PORT || 3000;

console.log(`Starting server on port: ${port}`);

app.use(express.json());
app.use(express.static('public'));

// Routes
const authRoutes = require('./routes/authRoutes');
const recommendationRoutes = require('./routes/recommendationRoutes');
const categoryRoutes = require('./routes/categoryRoutes');
const tripPlannerRoutes = require('./routes/tripPlannerRoutes');
const likeRoutes = require('./routes/likeRoutes');
const tourismRoutes = require('./routes/tourismRoutes');

app.use('/auth', authRoutes);
app.use('/recommendations', recommendationRoutes);
app.use('/category', categoryRoutes);
app.use('/tripplanner', tripPlannerRoutes);
app.use('/likes', likeRoutes);
app.use('/tourism', tourismRoutes);

app.listen(port, '0.0.0.0', () => {
  console.log(`Server running at http://localhost:${port}`);
}).on('error', (err) => {
  console.error('Failed to start server:', err);
  process.exit(1);
});
