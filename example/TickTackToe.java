import java.util.Scanner;

public class TickTackToe {
    public static void drawBoard(char[] board) {
        System.out.println();
        System.out.println("  " + board[0] + "|" + board[1] + "|" + board[2]);
        System.out.println("  -----");
        System.out.println("  " + board[3] + "|" + board[4] + "|" + board[5]);
        System.out.println("  -----");
        System.out.println("  " + board[6] + "|" + board[7] + "|" + board[8]);
    }

    public static boolean winnerExists(char[] board) {
        int[][] winCombos = {
            { 0, 1, 2 },
            { 3, 4, 5 },
            { 6, 7, 8 },
            { 0, 3, 6 },
            { 1, 4, 7 },
            { 2, 5, 8 },
            { 0, 4, 8 },
            { 0, 4, 6 },
        };

        for (int[] i : winCombos) {
            if ((board[i[0]] == board[i[1]]) && (board[i[1]] == board[i[2]])) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBoardFull(char[] board) {
        for (char i : board) {
            if ((i != 'X') && (i != 'O')) {
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
    }

    public static void main(String[] args) {
        char[] board = { '0', '1', '2', '3', '4', '5', '6', '7', '8' };

        Scanner s = new Scanner(System.in);
        System.out.println("Would you like to start a game, or join one? [s(start)/j(join)]");
        String choice = s.nextLine();
        if (choice.equals("s")) {
            Game.gameCode = Game.startNewGame();
            Game.player = 'X';
            System.out.printf("The game code is: %s", Game.gameCode);
            System.out.println("\nWaiting for other player to join");
            Game.checkJoined(Game.gameCode);
        } else if (choice.equals("j")) {
            System.out.println("Please enter the game code");
            Game.gameCode = s.nextLine();
            Game.updateBoard(Game.gameCode, board);
            Game.player = 'O';
        }

        while (Game.playing) {
            Game.myTurn(Game.player, Game.gameCode, (turn) -> {
                Game.updateBoard(Game.gameCode, board, (callback -> {
                    if (winnerExists(board)) {
                        if (Game.player == 'X') {
                            Game.winner = 'O';
                        } else if (Game.player == 'O') {
                            Game.winner = 'X';
                        }
                        Game.playing = false;
                    } else if (isBoardFull(board)) {
                        Game.winner = 'T';
                        Game.playing = false;
                    } else {
                        drawBoard(board);
                        if (Game.player == 'X') {
                            doMove('X', board, Game.gameCode);
                            if (winnerExists(board)) {
                                Game.winner = 'X';
                                Game.playing = false;
                            } else if (isBoardFull(board)) {
                                Game.winner = 'T';
                                Game.playing = false;
                            }
                            drawBoard(board);
                        } else {
                            doMove('O', board, Game.gameCode);
                            if (winnerExists(board)) {
                                Game.winner = 'O';
                                Game.playing = false;
                            } else if (isBoardFull(board)) {
                                Game.winner = 'T';
                                Game.playing = false;
                            }
                            drawBoard(board);
                        }
                    }
                }));
            });
        }

        if (Game.winner == 'T') {
            System.out.println("The game is tied. Game over");
        } else {
            System.out.printf("The winner is %c. Good Game", Game.winner);
        }
    }
}