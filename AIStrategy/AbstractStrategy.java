package io.github.emekler0729.TicTacToe.AIStrategy;

abstract public class AbstractStrategy {

    public static final int EASY_DIFFICULTY = 0;
    public static final int MEDIUM_DIFFICULTY = 1;
    public static final int HARD_DIFFICULTY = 2;

    public AbstractStrategy() {

    }

    abstract public int chooseMove(String[][] board);
}

