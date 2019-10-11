const UserFile = require('../models/file.model.js');

// Create and Save a new File
exports.create = (req, res) => {
    // Validate the request
    if (!req.body.name) {
        return res.status(400).send({
            message: "Name can not be empty"
        });
    }
    // Create a File
    const file = new UserFile({
        name: req.body.name,
        location: req.body.location || "Base folder"
    });

    // Save File in the database
    file.save()
        .then(data => {
            res.send(data);
        }).catch(err => {
            res.status(500).send({
                message: err.message || "Some error occurred while creating the File."
            });
        });
};

exports.upload = (req, res) => {

    const path = require('path');
    const os = require('os');
    const fs = require('fs');

    var Busboy = require('busboy');

    if (req.method === 'POST') {
        var busboy = new Busboy({ headers: req.headers });
        busboy.on('file', function (fieldname, file, filename, encoding, mimetype) {
            var saveTo = path.join(__dirname,filename);
            console.log(saveTo);
            file.pipe(fs.createWriteStream(saveTo));
        });
        busboy.on('finish', function () {
            res.writeHead(200, { 'Connection': 'close' });
            res.end("That's all folks!");
        });
        return req.pipe(busboy);
    }
    //res.writeHead(404);
    res.end();
};

// Retrieve and return all File from the database.
exports.findAll = (req, res) => {
    UserFile.find()
        .then(files => {
            res.send(files);
        }).catch(err => {
            res.status(500).send({
                message: err.message || "Some error occurred while retrieving files."
            });
        });
};

// Find a single file with a fileID
exports.findOne = (req, res) => {
    UserFile.findById(req.params.fileID)
        .then(file => {
            if (!file) {
                return res.status(404).send({
                    message: "File not found with id " + req.params.fileID
                });
            }
            res.send(file);
        }).catch(err => {
            if (err.kind === 'ObjectId') {
                return res.status(404).send({
                    message: "File not found with id " + req.params.fileID
                });
            }
            return res.status(500).send({
                message: "Error retrieving file with id " + req.params.fileID
            });
        });
};

// Update a file identified by the fileID in the request
exports.update = (req, res) => {

    // Find file and update it with the request body
    UserFile.findByIdAndUpdate(req.params.fileID, {
        name: req.body.name || "Untitled File",
        location: req.body.location
    }, { new: true })
        .then(file => {
            if (!file) {
                return res.status(404).send({
                    message: "File not found with id " + req.params.fileID
                });
            }
            res.send(file);
        }).catch(err => {
            if (err.kind === 'ObjectId') {
                return res.status(404).send({
                    message: "File not found with id " + req.params.fileID
                });
            }
            return res.status(500).send({
                message: "Error updating file with id " + req.params.fileID
            });
        });
};

// Delete a file with the specified fileID in the request
exports.delete = (req, res) => {
    file.findByIdAndRemove(req.params.fileID)
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
};