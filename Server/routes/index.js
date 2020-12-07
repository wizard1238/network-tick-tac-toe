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


  console.log(req.body)

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
        game.yPresent = true
        game.save(function(err, savedGame) {
          res.send(game)
        })
      })
    }
  } else {
    let game = new gameModel({
      positions: {
        0: "0",
        1: "1",
        2: "2",
        3: "3",
        4: "4",
        5: "5",
        6: "6",
        7: "7",
        8: "8",
      },
      turn: "X"
    })
    game.save(function(err, savedGame) {
      res.send(savedGame._id)
    })
  }  
})

module.exports = router;
