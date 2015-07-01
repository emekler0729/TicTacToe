package io.github.emekler0729.TicTacToe;

class AIGameBoard extends AbstractGameBoard {
    private String[][] board;

    AIGameBoard(AIClient client) {
        super(client);

        board = new String[3][3];
    }

    protected void updateText(String s) {
      // AI ignores server MSGs
    }

    protected void updateView(int move, String symbol) {
        board[move/3][move%3] = symbol;
    }

    protected void takeTurn() {
        lock = false;
        int move = (int)Math.floor(Math.random()*9);
        requestMove(move);
    }

    protected void disposeView() {
        // AI has nothing to dispose
    }
}
