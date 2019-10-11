const mongoose = require('mongoose');

const UserFileSchema = mongoose.Schema({
    name: String,
    location: String
}, {
    timestamps: true
}, { 
    collection: 'userfiles' 
});


module.exports = mongoose.model('UserFile', UserFileSchema);