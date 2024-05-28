const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');

const foodRoutes = require('./routes/food-routes');

const app = express();
const PORT = 5000;

app.use(cors());

app.use(bodyParser.json());

app.use('/', foodRoutes);

app.listen(PORT, '0.0.0.0', () => {
    console.log(`Server is running on port ${PORT}`);
});
