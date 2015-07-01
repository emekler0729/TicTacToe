package io.github.emekler0729.TicTacToe;

abstract class AbstractGameBoard implements TicTacToeProtocol {
    private AbstractClient client;
    private String playerSymbol;
    private String opponentSymbol;
    private int lastMove;
    protected boolean lock = true;

    AbstractGameBoard(AbstractClient client) {
        this.client = client;
    }

    protected void updateBoard(String s) {
        if(s.equals(TTTP_START)) {
            takeTurn();
        }

        else if (s.equals(TTTP_VALID_MOVE)) {
            updateView(lastMove, playerSymbol);
        }

        else if (s.startsWith(TTTP_OPPONENT_MOVE)) {
            int i = Integer.parseInt(s.substring(s.length()-1));
            updateView(i,opponentSymbol);
            takeTurn();
        }
        else if (s.equals(TTTP_INVALID_MOVE)) {
            lastMove = -1;
            takeTurn();
        }
    }

    abstract protected void updateText(String s);

    final protected void setPlayerSymbol(String s) { playerSymbol = s; }
    final protected void setOpponentSymbol(String s) { opponentSymbol = s; }

    abstract protected void updateView(int move, String symbol);
    abstract protected void disposeView();

    abstract protected void takeTurn();

    protected void requestMove(int move) {
        if(!lock) {
            lastMove = move;
            lock = true;
            client.iostream.println(TTTP_MOVE + move);
        }
    }
}
