import java.io.IOException;
import java.util.Scanner;

public class TicTacToe {

    private static IPConnect connect;
    private static Scanner scanner;

    public static void main(String[] args) throws IOException {

        char[] board = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        drawBoard(board);

        connect = new IPConnect();
        scanner = new Scanner(System.in);

        System.out.println("Host Server (h) or Connect to Server (c)");
        char hostOrConnect = scanner.nextLine().charAt(0);
    
        if(hostOrConnect == 'h'){ // host

            System.out.println("Your IP is " + connect.getIP());
            System.out.println("What Port to host on?");

            // get hosting port from user input 

            // establish host server with hostSocket() method

            while ( /* determine when the game should continue */ ){

                if(waitMove(board, 'X')){
                    break;
                }
                if(makeMove(board, 'O')){
                    break;
                }
            }
        } 
        
        else { // client

            System.out.println("What Port to connect to?");

            // get port to from user input

            System.out.println("What IP to connect to?");
            scanner.nextLine();

            // get IP Address from user input

            // connect to the host server with connectSocket() method

            while ( /* determine when the game should continue */){
                if(makeMove(board, 'X')){
                    break;
                }
                if(waitMove(board, 'O')){
                    break;
                }

            }
        }
        connect.killSocket();
    }

    private static boolean winnerExists(char[] board)  {

        // implement code to check for winner

    }

    private static boolean isBoardFull(char[] board)  {

        // implement code to check if the board is full (tie/draw)

    }

    private static void drawBoard(char[] board){

        System.out.println();
        System.out.println("  " + board[0]+"|"+board[1]+"|"+board[2]);
        System.out.println("  -----");
        System.out.println("  " + board[3]+"|"+board[4]+"|"+board[5]);
        System.out.println("  -----");
        System.out.println("  " + board[6]+"|"+board[7]+"|"+board[8]);

    }
    
    private static boolean makeMove(char[] board, char side) throws IOException {  // returns true if game is over 

        /*

            get a valid user board position (pos) from console input 
            
            update and display board with user's move (side)

        */

        // send user position (pos) using connect.sendMessage(pos)

        // check for win or draw
        
        return false;
    }

    private static boolean waitMove(char[] board, char side) throws IOException {  // returns true if game is over 

        System.out.println("Waiting for Opponent to Move");

        // get enemy position (enemyPos) using connect.getMessage()

        System.out.println("Opponent placed " + side + " at " + enemyPos);

        // update and display board with opponent's move (side)

        // check for loss or draw 
        
        return false;
    }

}
