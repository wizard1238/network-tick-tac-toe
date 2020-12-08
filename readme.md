# Getting Started with Server

First, create a .env file in the root directory of the repo with contents `MONGOOSEURL = "<mongoose connection string>"`
Then, run `yarn`, then `yarn dev`.

# API

Make requests to `/game`. The two parameters you can include are `game` and `move`
To create a new game, make an emtpy post request. You will get an id back. To get the status of a game, include the id of the game in the game parameter. To make a move, include the id of the game, and in the move parameter include the position and the turn.

## Example requests:

Getting status of a game:
```
{
    "game": "5fcae94a8468041e40388c7f" //Id of game
}
```

Making a move: 
```
{
    "game": "5fcae94a8468041e40388c7f", //Id of game
    "move": {
        "position": "2",
        "turn": "Y"
    }
}
```

# Getting Started with Client

