package io.github.emekler0729.TicTacToe;

import io.github.emekler0729.TicTacToe.Utility.SocketIOStream;

import java.io.IOException;
import java.net.InetSocketAddress;

abstract class AbstractClient implements TicTacToeProtocol {
    protected AbstractGameSession game;
    protected AbstractGameBoard gameboard;
    protected SocketIOStream iostream;

    protected AbstractClient(InetSocketAddress adr) throws IOException {
        joinGame(adr);
    }

    abstract protected class AbstractGameSession extends Thread {
        private boolean bGameOver;
        private boolean bFirstPlayer;
        private String msg;

        protected AbstractGameSession() {
            bGameOver = false;
            bFirstPlayer = false;
        }

        public void run() {
            while(!bGameOver) {
                msg = iostream.readLine();
                parseMsg(msg);
            }

            askPlayAgain();
        }

        private void parseMsg(String s) {
            if(s.equals(TTTP_START) && bFirstPlayer) {
                iostream.setInhibited(false);
                gameboard.updateBoard(s);
            }
            else if(s.startsWith(TTTP_SYMBOL)) {
                String c = s.substring(s.length()-1);
                if(c.equals("X")) {
                    bFirstPlayer = true;
                }
                gameboard.setPlayerSymbol(c);
                c = c.equals("X") ? "O" : "X";
                gameboard.setOpponentSymbol(c);
            }
            else if (s.startsWith(TTTP_MSG)) {
                gameboard.updateText(s);
            }
            else if (s.equals(TTTP_VALID_MOVE)) {
                gameboard.updateBoard(s);
            }
            else if (s.equals(TTTP_INVALID_MOVE) || s.startsWith(TTTP_OPPONENT_MOVE)) {
                iostream.setInhibited(false);
                gameboard.updateBoard(s);
            }
            else if (s.equals(TTTP_WIN) || s.equals(TTTP_LOSE) || s.equals(TTTP_DRAW)) {
                bGameOver = true;
                showGameOver(s);
            }
            else if (s.equals(TTTP_EXIT)) {
                try {
                    disconnect();
                }
                catch(IOException e) {

                }
            }
        }

        abstract protected void showGameOver(String s);
        abstract protected void askPlayAgain();
    }

    abstract protected void joinGame(InetSocketAddress adr) throws IOException;
    protected void playAgain() {
        iostream.setInhibited(false);
        iostream.println((TTTP_PLAY_AGAIN));
        gameboard.disposeView();
    }
    protected void disconnect() throws IOException {
        iostream.setInhibited(false);
        iostream.println(TTTP_EXIT);
        iostream.close();
        iostream = null;
        gameboard.disposeView();
    }
}
