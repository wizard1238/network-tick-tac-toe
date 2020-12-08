var mongoose = require('mongoose')

var gameSchema = new mongoose.Schema({
    positions: {
        0: String,
        1: String,
        2: String,
        3: String,
        4: String,
        5: String,
        6: String,
        7: String,
        8: String,
    },
    turn: String,
    yPresent: String,
})

module.exports = new mongoose.model('games', gameSchema)