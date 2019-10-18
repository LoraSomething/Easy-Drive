const express = require('express');
const bodyParser = require('body-parser');
var Busboy = require('busboy');
var multer = require('multer');
const uuidv1 = require('uuid/v1');
const app = express();
const fs = require('fs');

app.use(bodyParser.urlencoded({ extended: true }))

app.use(bodyParser.json())

app.use(function (req, res, next) {
	res.header("Access-Control-Allow-Origin", "*"); // update to match the domain you will make the request from
	res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
	next();
});

app.get('/', (req, res) => {
	res.send("Welcome to Easy Drive \n Routes: \n /uploadFile -> POST -> multipart/form-data with a singe file with a file using the name/key value `userfile` \n /files -> GET -> retrieve all files data \n /files/:fileID -> GET -> retrieve one file data \n /files/:fileID -> PUT -> update one file data in JSON Format, updatable fields: name \n /files/:fileID -> DELETE -> delete the file with the selected ID")
});

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
}).then(() => {
	console.log("Succesfully connected to the database");
}).catch(err => {
	console.log('Could not connect to the database. Error:', err);
	process.exit();
})

//----------------------//
//File Handling
//----------------------//
const UserFile = require('./app/models/file.model.js');

var storage = multer.diskStorage({
	destination: function (req, file, cb) {
		cb(null,  __dirname + "\\files\\")
	},
	filename: function (req, file, cb) {
		cb(null, file.fieldname + '-' + uuidv1())
	}
});

var upload = multer({ storage: storage });

//create
app.post('/uploadfile', upload.single('userfile'), function (req, res, next) {
	console.log(req.file);
	console.log(req.body);

	if (!req) {
		return res.status(400).send({
			message: "Name can not be empty"
		});
	}

	const file = new UserFile({
		name: req.file.originalname,
		storageID: req.file.filename
	});

	file.save()
		.then(data => {
			res.send(data);
		}).catch(err => {
			res.status(500).send({
				message: err.message || "Some error occurred while creating the File."
			});
		});
})

//find all
app.get('/files', function (req, res) {
	UserFile.find()
		.then(files => {
			res.send(files);
		}).catch(err => {
			res.status(500).send({
				message: err.message || "Some error occurred while retrieving files."
			});
		});
})

//find one
app.get('/files/:fileID', function (req, res) {
	UserFile.findById(req.params.fileID, function (err, userfile) {
		if (!err) {
			res.status(200).send(userfile)
		}
		else {
			res.status(404).send({
				message: "File not found with id " + req.params.fileID
			});
		}

	});
})

//update
app.put('/files/:fileID', function (req, res) {
	// Find file and update it with the request body
	UserFile.findByIdAndUpdate(req.params.fileID, {name: req.body.name}, {new: true}, function(err, userfile) {
		if (!err) {
			res.status(200).send(userfile)
		}
		else {
			res.status(404).send({
				message: "File not found with id " + req.params.fileID
			});
		}
	});
})

//delete
app.delete('/files/:fileID', function (req, res) {
	UserFile.findById(req.params.fileID, function (err, userfile) {
		console.log(userfile);
		let path = __dirname + "\\files\\" + userfile.storageID;
		console.log(path);
		fs.unlink(path, (err) => {
			if (err) {
				console.error(err)
				return
			}
			console.log(userfile + 'removed');
		});
	});

	UserFile.findOneAndDelete(req.params.fileID)
		.then(file => {
			if (!file) {
				return res.status(404).send({
					message: "File not found with id " + req.params.fileID
				});
			}
			res.send({ message: "File deleted successfully!" });
		}).catch(err => {
			if (err.kind === 'ObjectId' || err.name === 'NotFound') {
				return res.status(404).send({
					message: "File not found with id " + req.params.fileID
				});
			}
			return res.status(500).send({
				message: "Could not delete file with id " + req.params.fileID
			});
		});
})


