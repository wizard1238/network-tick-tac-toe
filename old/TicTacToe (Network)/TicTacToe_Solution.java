import java.io.IOException;
import java.util.Scanner;

public class TicTacToe_Solution {

    private static IPConnect connect;
    private static Scanner scanner;

    public static void main(String[] args) throws IOException {

        char[] board = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        drawBoard(board);

        connect = new IPConnect();
        scanner = new Scanner(System.in);

        System.out.println("Host Server (h) or Connect to Server (c)");
        char hostOrConnect = scanner.nextLine().charAt(0);

        if(hostOrConnect == 'h'){ 

            System.out.println("Your IP is " + connect.getIP());
            System.out.println("What Port to host on?");

            connect.hostSocket(scanner.nextInt());
            while (!winnerExists(board) && !isBoardFull(board)){
                if(waitMove(board, 'X')){
                    break;
                }
                if(makeMove(board, 'O')){
                    break;
                }
            }
        } 
        
        else {

            System.out.println("What Port to connect to?");
            int port = scanner.nextInt();

            System.out.println("What IP to connect to?");
            scanner.nextLine();
            String ip = scanner.nextLine();
            
            connect.connectSocket(port, ip);
            while (!winnerExists(board) && !isBoardFull(board)){
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

        for(int i = 0; i < 3; i++){  
            if(board[i] != ' ' && board[i] == board[i+3] && board[i] == board[i+6]){ // checks vertical win
                return true;
            }
        }
        for(int i = 0; i < 3; i++){ 
            if(board[i] != ' ' && board[3*i] == board[3*i+1] && board[i] == board[3*i+2]){ // checks horizontal win
                return true;
            }
        }
        if(board[0] != ' ' && board[0] == board[4] && board[0] == board[8]){  // checks diagonal 1 win 
            return true;
        }
        if(board[2] != ' ' && board[2] == board[4] && board[2] == board[6]){ // checks diagonal 2 win
            return true;
        } 

        return false;

    }

    private static boolean isBoardFull(char[] board)  {
        for(int i = 0; i < board.length; i++){
            if(!isSpaceFull(board, i)){
                return false;
            }
        }
        return true;
    }

    private static boolean isSpaceFull(char[] board, int i){
        if(i == -1){
            return true;
        }
        return board[i] != ' ';
    }

    private static void drawBoard(char[] board){

        System.out.println();
        System.out.println("  " + board[0]+"|"+board[1]+"|"+board[2]);
        System.out.println("  -----");
        System.out.println("  " + board[3]+"|"+board[4]+"|"+board[5]);
        System.out.println("  -----");
        System.out.println("  " + board[6]+"|"+board[7]+"|"+board[8]);

    }

    private static boolean makeMove(char[] board, char side) throws IOException {

        int pos = -1;
        while (isSpaceFull(board, pos)){
            System.out.println("Please enter board position (0-8) for a " + side + ": ");
            pos = scanner.nextInt();
        }

        board[pos] = side;
        drawBoard(board);
        connect.sendMessage(Integer.toString(pos));

        if(winnerExists(board)){
            System.out.println("You win!");
            return true;
        } 
        else if(isBoardFull(board)){
            System.out.println("Draw");
            return true;
        }
        return false;
    }

    private static boolean waitMove(char[] board, char side) throws IOException {

        System.out.println("Waiting for Opponent to Move");
        int enemyPos = Integer.parseInt(connect.getMessage());
        System.out.println("Opponent placed " + side + " at " + enemyPos);

        board[enemyPos] = side;
        drawBoard(board);

        if(winnerExists(board)){
            System.out.println("You Lose");
            return true;
        } 
        else if(isBoardFull(board)){
            System.out.println("Draw");
            return true;
        }
        return false;
    }

    
    
}
