const mongoose = require('mongoose');
mongoose.set('useNewUrlParser', true);
mongoose.set('useFindAndModify', false);

const UserFileSchema = mongoose.Schema({
    name: String,
    storageID: String
}, {
    timestamps: true
}, { 
    collection: 'userfiles' 
});


module.exports = mongoose.model('UserFile', UserFileSchema);