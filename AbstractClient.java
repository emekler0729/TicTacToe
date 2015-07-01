package io.github.emekler0729.TicTacToe;

import java.io.IOException;

abstract class AbstractClient implements TicTacToeProtocol {
    protected AbstractGameSession game;
    protected SocketIOStream iostream;

    protected AbstractClient() {

    }

    abstract protected class AbstractGameSession extends Thread {
        protected boolean bGameOver;
        protected boolean bFirstPlayer;
        private String s;

        protected AbstractGameSession() {
            bGameOver = false;
            bFirstPlayer = false;
        }

        public void run() {
            while(!bGameOver) {
                s = iostream.readLine();
                parseMsg(s);
            }

            askPlayAgain();
        }

        protected void parseMsg(String s) {

        }
        protected void askPlayAgain() {

        }
    }

    protected void initializeGame() {

    }
    protected void playAgain() {
        iostream.setInhibited(false);
        iostream.println((TTTP_PLAY_AGAIN));
    }
    protected void disconnect() throws IOException {
        iostream.setInhibited(false);
        iostream.println(TTTP_EXIT);
        iostream.close();
        iostream = null;
    }
}
