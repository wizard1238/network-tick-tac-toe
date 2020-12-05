var express = require('express');
var router = express.Router();
var gameModel = require('../models/gameModel')

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.post('/game', function(req, res, next) {
  let gameId = req.body.game
  let move = req.body.move

  if (gameId) {
    if (move) {
      gameModel.findById(gameId, function(err, game) {
        if (err) console.log(err)
        if (!game) {
          res.status(400).send('No game with that id')
        } else {
          game.positions[move.position] = move.turn
          game.turn = move.turn
          game.save(function(err, savedGame) {
            res.send(savedGame)
          })
        }
      })
    } else {
      gameModel.findById(gameId, function(err, game) {
        res.send(game)
      })
    }
  } else {
    let game = new gameModel({
      positions: {
        0: "empty",
        1: "empty",
        2: "empty",
        3: "empty",
        4: "empty",
        5: "empty",
        6: "empty",
        7: "empty",
        8: "empty",
      },
      turn: "X"
    })
    game.save(function(err, savedGame) {
      res.send(savedGame._id)
    })
  }  
})

module.exports = router;
