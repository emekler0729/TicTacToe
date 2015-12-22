package io.github.emekler0729.TicTacToe.AIStrategy;

import io.github.emekler0729.TicTacToe.AIGameBoard;

public class HardStrategy extends AbstractStrategy {
    AbstractStrategy temp = new MediumStrategy();
    public HardStrategy() {

    }

    public int chooseMove(String[][] board) {
        return temp.chooseMove(board);
    }
}
