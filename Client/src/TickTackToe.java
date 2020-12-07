import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TickTackToe {
    static String url = "http://localhost:3000/game";

    public static void drawBoard(char[] board)    {
        System.out.println();
        System.out.println("  " + board[0]+"|"+board[1]+"|"+board[2]);
        System.out.println("  -----");
        System.out.println("  " + board[3]+"|"+board[4]+"|"+board[5]);
        System.out.println("  -----");
        System.out.println("  " + board[6]+"|"+board[7]+"|"+board[8]);
    }

    public static boolean winnerExists(char[] board)  {

        int[][] winCombos = {
                {0,1,2},
                {3,4,5},
                {6,7,8},
                {0,3,6},
                {1,4,7},
                {2,5,8},
        };

        for(int[] i : winCombos) {
            if ((board[i[0]] == board[i[1]]) && (board[i[1]] == board[i[2]])) {
                return true;
            }
        }

        return false;
    }

    public static boolean isBoardFull(char[] board)  {
        for (char i : board) {
            if ((i != 'X') && (i != 'Y')) {
                return false;
            }
        }

        return true;
    }

    public static void doMove(char player, char[] board) {
        drawBoard(board);
        System.out.printf("Player %c. Please enter board position (0-8) for a %c: \n", player, player);
        Scanner s = new Scanner(System.in);
        board[s.nextInt()] = player;
    }

    public static String startNewGame() {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(
                URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .header("Content-type", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            return "error";
        }
    }

    public static void updateBoard(String gameCode, char[] board) {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(
                URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(String.format("{\"game\": \"%s\"}", gameCode)))
                .header("Content-type", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            System.out.println(jsonResponse.get("positions").getAsJsonObject().get("0"));
            for (int i = 0; i < board.length; i++) {
                board[i] = jsonResponse.get("positions").getAsJsonObject().get(Integer.toString(i)).toString().charAt(1);
            }
        } catch (Exception e) {}
    }

    public static void main(String [ ] args) {

        char[] board = {'0', '1', '2', '3', '4', '5', '6', '7', '8'};

        String gameCode = "none";
        char player = 'n';

        Scanner s = new Scanner(System.in);
        System.out.println("Would you like to start a game, or join one? [s(start)/j(join)]");
        String choice = s.nextLine();
        if (choice.equals("s")) {
            gameCode = startNewGame();
            player = 'X';
            System.out.printf("The game code is: %s", gameCode);
            System.out.println("Waiting for other player to join");
            while (true) {
                
            }
        } else if (choice.equals("j")) {
            System.out.println("Please enter the game code");
            gameCode = s.nextLine();
            updateBoard(gameCode, board);
            player = 'O';
        }

        boolean playing = true;
        boolean playerX = true;
        char winner = 'T';
        while (playing) {
            if (playerX) {
                doMove('X', board);
                if (winnerExists(board)) {
                    winner = 'X';
                    playing = false;
                } else if (isBoardFull(board)) {
                    winner = 'T';
                    playing = false;
                } else {
                    playerX = false;
                }
            } else {
                doMove('Y', board);
                if (winnerExists(board)) {
                    winner = 'X';
                    playing = false;
                } else if (isBoardFull(board)) {
                    winner = 'T';
                    playing = false;
                } else {
                    playerX = true;
                }
            }
        }

        if (winner == 'T') {
            System.out.println("The game is tied. Game over");
        } else {
            System.out.printf("The winner is %c. Congratulations", winner);
        }
    }
}

/*
Output:

Tie Game:

  0|1|2
  -----
  3|4|5
  -----
  6|7|8
Player X. Please enter board position (0-8) for a X:
0

  X|1|2
  -----
  3|4|5
  -----
  6|7|8
Player Y. Please enter board position (0-8) for a Y:
1

  X|Y|2
  -----
  3|4|5
  -----
  6|7|8
Player X. Please enter board position (0-8) for a X:
2

  X|Y|X
  -----
  3|4|5
  -----
  6|7|8
Player Y. Please enter board position (0-8) for a Y:
3

  X|Y|X
  -----
  Y|4|5
  -----
  6|7|8
Player X. Please enter board position (0-8) for a X:
4

  X|Y|X
  -----
  Y|X|5
  -----
  6|7|8
Player Y. Please enter board position (0-8) for a Y:
5

  X|Y|X
  -----
  Y|X|Y
  -----
  6|7|8
Player X. Please enter board position (0-8) for a X:
7

  X|Y|X
  -----
  Y|X|Y
  -----
  6|X|8
Player Y. Please enter board position (0-8) for a Y:
6

  X|Y|X
  -----
  Y|X|Y
  -----
  Y|X|8
Player X. Please enter board position (0-8) for a X:
8
The game is tied. Game over


X wins:

  0|1|2
  -----
  3|4|5
  -----
  6|7|8
Player X. Please enter board position (0-8) for a X:
0

  X|1|2
  -----
  3|4|5
  -----
  6|7|8
Player Y. Please enter board position (0-8) for a Y:
3

  X|1|2
  -----
  Y|4|5
  -----
  6|7|8
Player X. Please enter board position (0-8) for a X:
1

  X|X|2
  -----
  Y|4|5
  -----
  6|7|8
Player Y. Please enter board position (0-8) for a Y:
5

  X|X|2
  -----
  Y|4|Y
  -----
  6|7|8
Player X. Please enter board position (0-8) for a X:
2
The winner is X. Congratulations
 */