package io.github.emekler0729.TicTacToe;

import java.io.IOException;
import java.net.InetSocketAddress;

class AIClient extends AbstractClient {
    AIClient() {

    }

    class AIGameSession extends AbstractGameSession {

        protected void showGameOver(String s) {
            // AI displays nothing
        }
        protected void askPlayAgain() {
            playAgain();
        }
    }

    protected void initializeGame() {
        try {
            iostream = new SocketIOStream(new InetSocketAddress("localhost", 9090),true);
            gameboard = new AIGameBoard(this);
            game = new AIGameSession();

            game.start();
        }
        catch (IOException e) {

        }
    }
    protected void playAgain() {
        super.playAgain();
        gameboard = new AIGameBoard(this);
        game = new AIGameSession();

        game.start();
    }
}
