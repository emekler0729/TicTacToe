package io.github.emekler0729.TicTacToe.AIStrategy;

import io.github.emekler0729.TicTacToe.AIGameBoard;

public class EasyStrategy extends AbstractStrategy {
    public EasyStrategy(AIGameBoard gameboard) {
        super(gameboard);
    }

    public int chooseMove() {
        int move;
        String[][] board;
        boolean goodMove = false;

        board = gameboard.getView();

        do {
            move = (int) Math.floor(Math.random() * 9);

            if(board[move/3][move%3] == "") {
                goodMove = true;
            }
        } while (!goodMove);

        return move;
    }
}