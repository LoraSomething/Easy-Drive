const express = require('express');
const bodyParser = require('body-parser');
var Busboy = require('busboy');

const app = express();

app.use(bodyParser.urlencoded({ extended:true }))

app.use(bodyParser.json())

app.use(function(req, res, next) {
	res.header("Access-Control-Allow-Origin", "*"); // update to match the domain you will make the request from
	res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
	next();
  });

app.get('/',(req,res) => {
	res.json({"message": "Welcome to easy drive"})
});

require('./app/routes/file.routes.js')(app);

app.listen(3000, () => {
	console.log("Server is listening on port 3000")
});

const dbConfig = require('./config/database.config.js');
const mongoose = require('mongoose');

//legacy code from older examples that isn't needed with Mongoose 5
mongoose.Promise = global.Promise;

mongoose.connect(dbConfig.url, {
	useUnifiedTopology: true,
	useNewUrlParser: true
}).then (() => {
	console.log("Succesfully connected to the database");
}).catch(err => {
	console.log('Could not connect to the database. Error:',err);
	process.exit();
})

