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

    public static void checkJoined(String gameCode, Consumer<Boolean> callback) {
        boolean running = true;
        while (running) {
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.ofString(String.format("{\"game\": \"%s\", \"host\": \"X\"}", gameCode)))
                    .header("Content-type", "application/json").build();
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
                if (jsonResponse.get("yPresent").toString().equals("\"true\"")) {
                    running = false;
                    callback.accept(true);
                } else {
                    TimeUnit.MILLISECONDS.sleep(500);
                }
            } catch (Exception e) {}
        }
    }

    public static void myTurn(char player, String gameCode, Consumer<Boolean> callback) {
        boolean running = true;
        
        while (running) {
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.ofString(String.format("{\"game\": \"%s\", \"host\": \"X\"}", gameCode)))
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
            } catch (Exception e) {}
        }
    }

    public static void makeMove(String gameCode, int move, char player, char[] board) {
        
        var client = HttpClient.newHttpClient();
        
        var request = HttpRequest.newBuilder(URI.create(url))
            .POST(HttpRequest.BodyPublishers.ofString(String.format("{\"game\": \"%s\", \"move\": {\"position\": \"%d\", \"turn\": \"%c\"}}", gameCode, move, player)))
            .header("Content-type", "application/json").build();
        System.out.println("make Move 3");
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

}
