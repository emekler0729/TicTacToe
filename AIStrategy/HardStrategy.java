package io.github.emekler0729.TicTacToe.AIStrategy;

public class HardStrategy extends AbstractStrategy {
    AbstractStrategy temp = new MediumStrategy();
    public HardStrategy() {

    }

    public int chooseMove(String[][] board) {
        return temp.chooseMove(board);
    }
}
