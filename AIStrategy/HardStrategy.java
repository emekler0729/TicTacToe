package io.github.emekler0729.TicTacToe.AIStrategy;


import java.util.ArrayList;

public class HardStrategy extends AbstractStrategy {
    private String mySymbol;
    private int choice;

    private static final int WIN = 2;
    private static final int DRAW = 1;
    private static final int NONE = 0;


    public HardStrategy() {
        mySymbol = "O";
        choice = 0;
    }

    public int chooseMove(String[][] board) {
        minimax(board, mySymbol, 0);

        return choice;
    }

    private int getGameState(String[][] board) {
        int myCount = 0;
        int oppCount = 0;
        boolean draw = true;

        for(int row = 0; row < 3; row++) {
            for(int col = 0; col < 3; col++) {
                if(board[row][col].equals(mySymbol)) {
                    myCount++;
                }
                else if(board[row][col].equals("")) {
                    draw = false;
                }
                else {
                    oppCount++;
                }
            }

            if(myCount == 3 || oppCount == 3) {
                return WIN;
            }

            myCount = oppCount = 0;
        }

        for(int col = 0; col < 3; col++) {
            for(int row = 0; row < 3; row++) {
                if(board[row][col].equals(mySymbol)) {
                    myCount++;
                }
                else if(board[row][col].equals("")) {
                    draw = false;
                }
                else {
                    oppCount ++;
                }
            }

            if(myCount == 3 || oppCount == 3) {
                return WIN;
            }

            myCount = oppCount = 0;
        }

        if (board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2]) && !board[1][1].equals("")) {
            return WIN;
        }

        if (board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0]) && !board[1][1].equals("")) {
            return WIN;
        }

        if (draw) {
            return DRAW;
        }

        return NONE;
    }
    private int score(int state, String currentPlayer, int depth) {
        if (state == WIN) {
            if (currentPlayer.equals(mySymbol)) {
                return depth-10;
            }
            else {
                return depth+10;
            }
        }
        else {
            return 0;
        }
    }
    private ArrayList<Integer> getMoves(String[][] board) {
        ArrayList<Integer> options = new ArrayList<Integer>();

        for(int row = 0; row < 3; row++) {
            for(int col = 0; col < 3; col++) {
                if(board[row][col].equals("")) {
                    options.add(new Integer((row*3)+col));
                }
            }
        }

        return options;
    }
    private String[][] createBoard(String[][] board, int move, String currentPlayer) {
        String[][] temp = new String[3][3];

        for(int row = 0; row < 3; row++) {
            for(int col = 0; col < 3; col++) {
                temp[row][col] = board[row][col];
            }
        }

        temp[move/3][move%3] = currentPlayer;
        return temp;
    }
    private String swapPlayer(String currentPlayer) {
        String temp = (currentPlayer == "X") ? "O" : "X";
        return temp;
    }

    private int minimax(String[][] board, String currentPlayer, int depth) {
        if (getGameState(board) != NONE) {
            return score(getGameState(board), currentPlayer, depth);
        }
        depth++;

        String[][] possibleBoard;

        ArrayList<Integer> moves = new ArrayList<Integer>();
        ArrayList<Integer> scores = new ArrayList<Integer>();

        ArrayList<Integer> possibleMoves = getMoves(board);

        for(int i = 0; i < possibleMoves.size(); i++) {
            possibleBoard = createBoard(board, possibleMoves.get(i).intValue(), currentPlayer);
            scores.add(minimax(possibleBoard, swapPlayer(currentPlayer), depth));
            moves.add(possibleMoves.get(i).intValue());
        }

        if (currentPlayer.equals(mySymbol)) {
            int maxScoreIndex = -1;
            int maxScore = -100;

            for (int i = 0; i < scores.size(); i++) {
                if (scores.get(i) > maxScore) {
                    maxScore = scores.get(i);
                    maxScoreIndex = i;
                }
            }

            choice = moves.get(maxScoreIndex);
            return scores.get(maxScoreIndex);
        }

        else {
            int minScoreIndex = -1;
            int minScore = 100;

            for (int i = 0; i < scores.size(); i++) {
                if (scores.get(i) < minScore) {
                    minScore = scores.get(i);
                    minScoreIndex = i;
                }
            }

            choice = moves.get(minScoreIndex);
            return scores.get(minScoreIndex);
        }
    }
}

