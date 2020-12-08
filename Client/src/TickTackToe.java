import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TickTackToe {
    static String url = "http://localhost:3000/game";

    public static void drawBoard(char[] board) {
        System.out.println();
        System.out.println("  " + board[0] + "|" + board[1] + "|" + board[2]);
        System.out.println("  -----");
        System.out.println("  " + board[3] + "|" + board[4] + "|" + board[5]);
        System.out.println("  -----");
        System.out.println("  " + board[6] + "|" + board[7] + "|" + board[8]);
    }

    public static boolean winnerExists(char[] board) {
        int[][] winCombos = { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 }, };

        for (int[] i : winCombos) {
            if ((board[i[0]] == board[i[1]]) && (board[i[1]] == board[i[2]])) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBoardFull(char[] board) {
        for (char i : board) {
            if ((i != 'X') && (i != 'Y')) {
                return false;
            }
        }
        return true;
    }

    public static void doMove(char player, char[] board, String gameCode) {

        System.out.printf("Player %c. Please enter board position (0-8) for a %c: %n", player, player);
        Scanner s = new Scanner(System.in);
        int position = s.nextInt();
        board[position] = player;
        Game.makeMove(gameCode, position, player, board);
        drawBoard(board);

    }

    public static String startNewGame() {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(URI.create(url)).POST(HttpRequest.BodyPublishers.ofString(""))
                .header("Content-type", "application/json").build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body().substring(1, response.body().length() - 1);
        } catch (Exception e) {
            return "error";
        }
    }

    public static void updateBoard(String gameCode, char[] board) {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(String.format("{\"game\": \"%s\"}", gameCode)))
                .header("Content-type", "application/json").build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            for (int i = 0; i < board.length; i++) {
                board[i] = jsonResponse.get("positions").getAsJsonObject().get(Integer.toString(i)).toString()
                        .charAt(1);
            }
        } catch (Exception e) {}
    }

    public static void makeMove(String gameCode, int move, char player, char[] board) {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(URI.create(url))
            .POST(HttpRequest.BodyPublishers.ofString(String.format("{\"game\": \"%s\", \"move\": {\"position\": \"%i\", \"turn\": \"%c\"}}", gameCode, move, player)))
            .header("Content-type", "application/json").build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            for (int i = 0; i < board.length; i++) {
                board[i] = jsonResponse.get("positions").getAsJsonObject().get(Integer.toString(i)).toString()
                        .charAt(1);
            }
        } catch (Exception e) {
            System.out.println("was error");
        }
    }
    
    public static boolean myTurn(char player, String gameCode) {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(String.format("{\"game\": \"%s\", \"host\": \"X\"}", gameCode)))
                .header("Content-type", "application/json").build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            System.out.println(jsonResponse.get("turn").toString());
            if (jsonResponse.get("turn").toString().equals(String.format("\"%c\"", player))) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {

        char[] board = { '0', '1', '2', '3', '4', '5', '6', '7', '8' };

        

        Scanner s = new Scanner(System.in);
        System.out.println("Would you like to start a game, or join one? [s(start)/j(join)]");
        String choice = s.nextLine();
        if (choice.equals("s")) {
            Game.gameCode = startNewGame();
            Game.player = 'X';
            System.out.printf("The game code is: %s", Game.gameCode);
            System.out.println("\nWaiting for other player to join");
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            Game.checkJoined(Game.gameCode, (joined) -> {});
            
        } else if (choice.equals("j")) {
            System.out.println("Please enter the game code");
            Game.gameCode = s.nextLine();
            updateBoard(Game.gameCode, board);
            Game.player = 'O';
        }

        while (Game.playing) {
            Game.myTurn(Game.player, Game.gameCode, (turn) -> {
                drawBoard(board);
                if (Game.player == 'X') {
                    doMove('X', board, Game.gameCode);
                    if (winnerExists(board)) {
                        Game.winner = 'O';
                        Game.playing = false;
                    } else if (isBoardFull(board)) {
                        Game.winner = 'T';
                        Game.playing = false;
                    }
                } else {
                    doMove('O', board, Game.gameCode);
                    if (winnerExists(board)) {
                        Game.winner = 'O';
                        Game.playing = false;
                    } else if (isBoardFull(board)) {
                        Game.winner = 'T';
                        Game.playing = false;
                    }
                }
            });
        }

        if (Game.winner == 'T') {
            System.out.println("The game is tied. Game over");
        } else {
            System.out.printf("The winner is %c. Congratulations", Game.winner);
        }
    }
}