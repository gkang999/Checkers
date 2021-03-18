import java.util.*;

public class Checkers{
    public static class CheckersGame{
        boolean redTurn;
        int numBlackPieces;
        int numRedPieces;
        char[][] board;
        
        public CheckersGame(){
            //Initialize the starting board.
            board = new char[8][8];
            //zero fill the board
            for(int i = 0; i<8; i++){
                for(int j = 0; j<8; j++){
                    board[i][j] = '0';
                }
            }
            //place black pieces
            for(int i = 0; i<3; i++){
                if(i == 1){
                    for(int j = 0; j<8; j=j+2){
                        board[i][j] = 'b';
                    }
                } else {
                    for(int j = 1; j<8; j=j+2){
                        board[i][j] = 'b';
                    }
                }
                
            }
            //place red pieces
            for(int i = 5; i<8; i++){
                if(i == 6){
                    for(int j = 1; j<8; j=j+2){
                        board[i][j] = 'r';
                    }
                } else {
                    for(int j = 0; j<8; j=j+2){
                        board[i][j] = 'r';
                    }
                }
                
            }
            numBlackPieces = 12;
            numRedPieces = 12;
            redTurn = true;
        }
        
        public boolean move(String theMove){
            //validate move here (call capture when necesarry)
            String[] coords;
            char[][] boardDupe = board;
            char splitter[] = {' '};
            coords = theMove.split(" ");
            if(coords.length < 2){
                return false;
            }
            for(int i = 0; i<coords.length-1; i++){
                String curr = coords[i];
                String next = coords[i+1];
                if(curr.length() != 2 || next.length() != 2){
                    board = boardDupe;
                    return false;
                }
                int curri = Integer.parseInt(curr.substring(0, 1));
                int currj = Integer.parseInt(curr.substring(1, 2));
                int nexti = Integer.parseInt(next.substring(0, 1));
                int nextj = Integer.parseInt(next.substring(1, 2));
                if(!validStartSpot(curri, currj) || !validEndSpot(nexti, nextj)){
                    board = boardDupe;
                    return false;
                }
                if(redTurn){
                    if(board[curri][currj] == 'R'){
                        if(validRed(curri, currj, nexti, nextj) || validBlack(curri, currj, nexti, nextj)){
                            board[nexti][nextj] = board[curri][currj];
                            board[curri][currj] = '0';
                        } else {
                            board = boardDupe;
                            return false;
                        }
                    } else {
                        if(validRed(curri, currj, nexti, nextj)){
                            board[nexti][nextj] = board[curri][currj];
                            if(nexti == 0 && board[nexti][nextj] == 'r'){
                                board[nexti][nextj] = 'R';
                            }
                            board[curri][currj] = '0';
                        } else {
                            board = boardDupe;
                            return false;
                        }
                    }
                } else {
                    if(board[curri][currj] == 'B'){
                        if(validRed(curri, currj, nexti, nextj) || validBlack(curri, currj, nexti, nextj)){
                            board[nexti][nextj] = board[curri][currj];
                            board[curri][currj] = '0';
                        } else {
                            board = boardDupe;
                            return false;
                        }
                    } else {
                        if(validBlack(curri, currj, nexti, nextj)){
                            board[nexti][nextj] = board[curri][currj];
                            if(nexti == 7 && board[nexti][nextj] == 'b'){
                                board[nexti][nextj] = 'B';
                            }
                            board[curri][currj] = '0';
                        } else {
                            board = boardDupe;
                            System.out.println("reach");
                            return false;
                        }
                    }
                }
            }
            redTurn = !redTurn;
            return true;
        }
        
        public boolean validRed(int curri, int currj, int nexti, int nextj){
            //reg move?
            int iDiff = nexti - curri;
            int jDiff = nextj - currj;
            if((iDiff == -1 && jDiff == 1) || (iDiff == -1 && jDiff == -1)){
                return true;
            }
            //capture move
            if((iDiff == -2 && jDiff == 2) || (iDiff == -2 && jDiff == -2)){
                return capture(curri, currj, nexti, nextj);
            }
            return false;
        }
        
        public boolean validBlack(int curri, int currj, int nexti, int nextj){
            //reg move?
            int iDiff = nexti - curri;
            int jDiff = nextj - currj;
            if((iDiff == 1 && jDiff == 1) || (iDiff == 1 && jDiff == -1)){
                return true;
            }
            //capture move
            if((iDiff == 2 && jDiff == 2) || (iDiff == 2 && jDiff == -2)){
                return capture(curri, currj, nexti, nextj);
            }
            return false;
        }
        
        public boolean capture(int curri, int currj, int nexti, int nextj){
            //validate capture here
            int iDiff = nexti - curri;
            int jDiff = nextj - currj;
            if(redTurn && board[curri][currj] != 'R'){
                return captureWithRed(curri, currj, nexti, nextj, 'b', 'B');
            } else if(redTurn && board[curri][currj] == 'R'){
                return captureWithRed(curri, currj, nexti, nextj, 'b', 'B') || captureWithBlack(curri, currj, nexti, nextj, 'b', 'B');
            } else if(!redTurn && board[curri][currj] != 'B'){
                return captureWithBlack(curri, currj, nexti, nextj, 'r', 'R');
            } else if(!redTurn && board[curri][currj] == 'B'){
                return captureWithRed(curri, currj, nexti, nextj, 'r', 'R') || captureWithBlack(curri, currj, nexti, nextj, 'r', 'R');
            }
            return false;
        }
        
        public boolean captureWithRed(int curri, int currj, int nexti, int nextj, char eatOne, char eatTwo){
            int iDiff = nexti - curri;
            int jDiff = nextj - currj;
            if(iDiff == -2 && jDiff == 2){
                boolean piece = board[curri-1][currj+1] == eatOne || board[curri-1][currj+1] == eatTwo;
                if(piece){
                    if(eatOne == 'r'){
                        numRedPieces--;
                    } else {
                        numBlackPieces--;
                    }
                    board[curri-1][currj+1] = '0';
                }
                return piece;
            }
            if(iDiff == -2 && jDiff == -2){
                boolean piece = board[curri-1][currj-1] == eatOne || board[curri-1][currj-1] == eatTwo;
                if(piece){
                    if(eatOne == 'r'){
                        numRedPieces--;
                    } else {
                        numBlackPieces--;
                    }
                    board[curri-1][currj-1] = '0';
                }
                return piece;
            }
            return false;
        }
        
        public boolean captureWithBlack(int curri, int currj, int nexti, int nextj, char eatOne, char eatTwo){
            int iDiff = nexti - curri;
            int jDiff = nextj - currj;
            if(iDiff == 2 && jDiff == 2){
                boolean piece = board[curri+1][currj+1] == eatOne || board[curri+1][currj+1] == eatTwo;
                if(piece){
                    if(eatOne == 'r'){
                        numRedPieces--;
                    } else {
                        numBlackPieces--;
                    }
                    board[curri+1][currj+1] = '0';
                }
                return piece;
            }
            if(iDiff == 2 && jDiff == -2){
                boolean piece = board[curri+1][currj-1] == eatOne || board[curri+1][currj-1] == eatTwo;
                if(piece){
                    if(eatOne == 'r'){
                        numRedPieces--;
                    } else {
                        numBlackPieces--;
                    }
                    board[curri+1][currj-1] = '0';
                }
                return piece;
            }
            return false;
        }
        
        public boolean validStartSpot(int i, int j){
        if(redTurn){
            if(board[i][j] != 'r' && board[i][j] != 'R'){
                return false;
            }
        } else {
             if(board[i][j] != 'b' && board[i][j] != 'B'){
                 return false;
             }
        }
        if(i<0 || j<0 || i>7 || j>7){
            return false;
        }
        return true;
        }
        
        public boolean validEndSpot(int i, int j){
            if(board[i][j] != '0'){
                return false;
            }
            if(i<0 || j<0 || i>7 || j>7){
                return false;
            }
            return true;
        }
        
        public boolean GameOver(){
            if(numBlackPieces == 0 || numRedPieces == 0){
                return true;
            } else {
                return false;
            }
        }
        
        public void printBoard(){
            System.out.println("  0 1 2 3 4 5 6 7");
            for(int i = 0; i<8; i++){
                for(int j = 0; j<8; j++){
                    if(j == 0){
                        System.out.print(i);
                    }
                    if(board[i][j] == '0'){
                        System.out.print("|_");
                    } else {
                        System.out.print("|" + board[i][j]);
                    }
                    if(j == 7){
                        System.out.print("|\n");
                    }
                    
                }
            }
        }
    }
    
    public static void main(String []args){
        CheckersGame onlyGame = new CheckersGame();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Red goes first. Moves should look like: \"(coordinate of starting piece) followed by (coordinate of every space that piece moves to in the turn)\" separated by spaces of course.");
        while(!onlyGame.GameOver()){
            //ask for moves here
            onlyGame.printBoard();
            System.out.println("Give me a move");
            String moveStr = scanner.nextLine();
            while(!onlyGame.move(moveStr)){ 
                onlyGame.printBoard();
                System.out.println("Invalid. Give me a move");
                moveStr = scanner.nextLine();
            }
        }
        onlyGame.printBoard();
        String winner = (onlyGame.numBlackPieces == 0) ? "Red" : "Black";
        System.out.println("Game Over. " + winner + " is the winner.");
    }
} 
