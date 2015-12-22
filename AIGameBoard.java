package io.github.emekler0729.TicTacToe;

import io.github.emekler0729.TicTacToe.AIStrategy.*;

public class AIGameBoard extends AbstractGameBoard {
    private String[][] board;
    private AbstractStrategy strategy;

    AIGameBoard(AIClient client, final int DIFFICULTY) {
        super(client);

        switch(DIFFICULTY) {
            case AbstractStrategy.EASY_DIFFICULTY:
                strategy = new EasyStrategy();
                break;
            case AbstractStrategy.MEDIUM_DIFFICULTY:
                strategy = new MediumStrategy();
                break;
            case AbstractStrategy.HARD_DIFFICULTY:
                strategy = new HardStrategy();
            default:
                strategy = new MediumStrategy();
                break;
        }

        board = new String[3][3];

        for(int i = 0; i < 9; i++) {
            board[i/3][i%3] = "";
        }
    }

    protected void updateText(String s) {
      // AI ignores server MSGs
    }
    protected void updateScore(int w, int i, int l) {

    }

    protected void updateView(int move, String symbol) {
        board[move/3][move%3] = symbol;
    }
    public Object getView() {
        return board;
    }

    protected void takeTurn() {
        lock = false;
        int move = strategy.chooseMove(board);
        requestMove(move);
    }

    protected void disposeView() {
        // AI has nothing to dispose
    }
}

