import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Game {
    static String url = "http://localhost:3000/game";
    static boolean playing = true;
    static char winner = 'T';
    static char player = 'T';
    static String gameCode = "none";

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

    public static void checkJoined(String gameCode) {
        while (true) {
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(URI.create(url))
                    .POST(HttpRequest.BodyPublishers
                            .ofString(String.format("{\"game\": \"%s\", \"host\": \"X\"}", gameCode)))
                    .header("Content-type", "application/json").build();
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
                if (jsonResponse.get("yPresent").toString().equals("\"true\"")) {
                    return;
                } else {
                    TimeUnit.MILLISECONDS.sleep(500);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void myTurn(char player, String gameCode, Consumer<Boolean> callback) {
        boolean running = true;

        while (running) {
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(URI.create(url))
                    .POST(HttpRequest.BodyPublishers
                            .ofString(String.format("{\"game\": \"%s\", \"host\": \"X\"}", gameCode)))
                    .header("Content-type", "application/json").build();
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
                if (jsonResponse.get("turn").toString().equals(String.format("\"%c\"", player))) {
                    TimeUnit.MILLISECONDS.sleep(500);
                } else {
                    TimeUnit.MILLISECONDS.sleep(500);
                    callback.accept(true);
                    running = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void makeMove(String gameCode, int move, char player, char[] board) {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(
                        String.format("{\"game\": \"%s\", \"move\": {\"position\": \"%d\", \"turn\": \"%c\"}}",
                                gameCode, move, player)))
                .header("Content-type", "application/json").build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            for (int i = 0; i < board.length; i++) {
                board[i] = jsonResponse.get("positions").getAsJsonObject().get(Integer.toString(i)).toString()
                        .charAt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Update board methods
    public static void updateBoard(String gameCode, char[] board, Consumer<Boolean> callback) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        callback.accept(true);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
