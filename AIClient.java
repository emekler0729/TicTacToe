package io.github.emekler0729.TicTacToe;

import io.github.emekler0729.TicTacToe.Utility.SocketIOStream;

import java.io.IOException;
import java.net.InetSocketAddress;

class AIClient extends AbstractClient {
    private int difficulty;

    AIClient(InetSocketAddress adr, final int DIFFICULTY) throws IOException {
        difficulty = DIFFICULTY;
        joinGame(adr);
    }

    class AIGameSession extends AbstractGameSession {

        protected void showGameOver(String s) {
            // AI displays nothing
        }
        protected void askPlayAgain() {
            playAgain();
        }
    }

    protected void joinGame(InetSocketAddress adr) throws IOException {
            iostream = new SocketIOStream(adr,true);
            gameboard = new AIGameBoard(this,difficulty);
            game = new AIGameSession();
            game.start();
    }
    protected void playAgain() {
        super.playAgain();
        String msg = "";
        while (!(msg.equals(TTTP_AGAIN) || msg.equals(TTTP_EXIT))) {
            msg = iostream.readLine();
        }
        if (msg.equals(TTTP_AGAIN)) {
            gameboard = new AIGameBoard(this, difficulty);
            game = new AIGameSession();
            game.start();
        }
        else {
            try {
                disconnect();
            }
            catch (IOException e) {

            }
        }
    }
}
