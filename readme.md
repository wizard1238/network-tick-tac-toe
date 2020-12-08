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

Put `Game.java` into the same directory as your code. Add [gson](https://github.com/google/gson) to the java project. Change the static String `url` to the url of the server. Methods are listed here

- `startNewGame`: Returns a string with the game code.
- `checkJoined`: Returns void when another client has joined the same game. Takes string `gameCode` as a parameter.
- `myTurn`: Runs callback when it is that client's turn. Takes char `player`, string `gameCode`, and a callback that returns a boolean `true`
    ```java
    Game.myTurn('X', "<gameCode>", (callback) -> {
        //Run code here
    })
    ```
- `makeMove`: Posts move to server. Takes string `gameCode`, int `move`, char `player`, char[] `board`. Returns void.
- `updateBoard`: Gets board info from server. Takes string `gameCode`, char[] `board`, with an optional callback.

Example code is in the `/example` folder.
