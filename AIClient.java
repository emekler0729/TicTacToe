package io.github.emekler0729.TicTacToe;

import io.github.emekler0729.TicTacToe.Utility.SocketIOStream;

import java.io.IOException;
import java.net.InetSocketAddress;

class AIClient extends AbstractClient {
    private int difficulty;

    AIClient(InetSocketAddress adr, final int DIFFICULTY) throws IOException {
        super(adr);
        difficulty = DIFFICULTY;
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
        gameboard = new AIGameBoard(this, difficulty);
        game = new AIGameSession();
        game.start();
    }
}
