package io.github.emekler0729.TicTacToe;

class AIGameBoard extends AbstractGameBoard {
    private String[][] board;
    private AbstractStrategy strategy;

    public static final int EASY_DIFFICULTY = 0;

    AIGameBoard(AIClient client, final int DIFFICULTY) {
        super(client);

        switch(DIFFICULTY) {
            case EASY_DIFFICULTY:
                strategy = new EasyStrategy();
                break;
            default:
                strategy = new EasyStrategy();
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

    protected void updateView(int move, String symbol) {
        board[move/3][move%3] = symbol;
    }

    protected void takeTurn() {
        lock = false;
        int move = strategy.chooseMove();
        requestMove(move);
    }

    protected void disposeView() {
        // AI has nothing to dispose
    }

    abstract class AbstractStrategy {
        AbstractStrategy() {

        }

        abstract public int chooseMove();
    }
    class EasyStrategy extends AbstractStrategy {
        public int chooseMove() {
            return (int)Math.floor(Math.random()*9);
        }
    }

}

