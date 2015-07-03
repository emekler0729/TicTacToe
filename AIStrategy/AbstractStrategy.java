package io.github.emekler0729.TicTacToe.AIStrategy;

import io.github.emekler0729.TicTacToe.AIGameBoard;

abstract public class AbstractStrategy {
    protected AIGameBoard gameboard;

    public static final int EASY_DIFFICULTY = 0;
    public static final int MEDIUM_DIFFICULTY = 1;
    public static final int HARD_DIFFICULTY = 2;

    public AbstractStrategy(AIGameBoard gameboard) {
        this.gameboard = gameboard;
    }

    abstract public int chooseMove();
}

