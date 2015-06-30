package io.github.emekler0729.TicTacToe;

public interface TicTacToeProtocol {
    // Server Commands
    public static final String TTTP_START = "START";
    public static final String TTTP_SYMBOL = "SYMBOL ";
    public static final String TTTP_MSG = "MSG ";
    public static final String TTTP_VALID_MOVE = "VALID_MOVE";
    public static final String TTTP_INVALID_MOVE = "INVALID_MOVE";
    public static final String TTTP_OPPONENT_MOVE = "OPPONENT_MOVE ";
    public static final String TTTP_WIN = "WIN";
    public static final String TTTP_LOSE = "LOSE";
    public static final String TTTP_DRAW = "DRAW";

    // Client Commands
    public static final String TTTP_MOVE = "MOVE ";
    public static final String TTTP_PLAY_AGAIN = "PLAY_AGAIN";
    public static final String TTTP_EXIT = "EXIT";
}
