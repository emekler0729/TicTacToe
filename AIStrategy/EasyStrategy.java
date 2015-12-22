package io.github.emekler0729.TicTacToe.AIStrategy;

public class EasyStrategy extends AbstractStrategy {
    public EasyStrategy() {

    }

    public int chooseMove(String[][] board) {
        int move;
        boolean goodMove = false;

        do {
            move = (int) Math.floor(Math.random() * 9);

            if(board[move/3][move%3] == "") {
                goodMove = true;
            }
        } while (!goodMove);

        return move;
    }
}