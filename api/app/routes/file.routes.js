module.exports = (app) => {
    const files = require('../controllers/files.controller.js');

    // Create a new File
    app.post('/files', files.create);

    // Create a new File
    app.post('/files/upload', files.upload);
    
    // Retrieve all File
    app.get('/files', files.findAll);

    // Retrieve a single File with fileID
    app.get('/files/:fileID', files.findOne);

    // Update a File with fileID
    app.put('/files/:fileID', files.update);

    // Delete a File with fileID
    app.delete('/files/:fileID', files.delete);
}