package io.github.emekler0729.TicTacToe;

import io.github.emekler0729.TicTacToe.Utility.SocketIOStream;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;

class GUIClient extends AbstractClient {
    GUIClient(InetSocketAddress adr) throws IOException {
        super(adr);
    }

    class GUIGameSession extends AbstractGameSession {
        GUIGameSession() {

        }

        protected void showGameOver(String s) {
            if(s.equals(TTTP_DRAW)) {
                JOptionPane.showMessageDialog(null, "The game was a draw.", "Game Over!", JOptionPane.PLAIN_MESSAGE);
            }
            else if(s.equals(TTTP_WIN)) {
                JOptionPane.showMessageDialog(null,"You win the game!","Game Over!",JOptionPane.PLAIN_MESSAGE);
            }
            else if(s.equals(TTTP_LOSE)) {
                JOptionPane.showMessageDialog(null,"You lose the game.","Game Over!", JOptionPane.PLAIN_MESSAGE);
            }
        }
        synchronized protected void askPlayAgain() {
            int choice = JOptionPane.showOptionDialog(null,"Would you like to play again?","Game Over",
                    JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,new Object[]{"Yes","No"},"Yes");
            if(choice == 0) {
                playAgain();
            } else {
                try {
                    disconnect();
                }
                catch(IOException e) {

                }
            }
        }
    }

    protected void joinGame(InetSocketAddress adr) throws IOException {
        iostream = new SocketIOStream(adr,true);
        gameboard = new GUIGameBoard(this);
        game = new GUIGameSession();
        game.start();
    }
    protected void playAgain() {
        super.playAgain();
        gameboard = new GUIGameBoard(this);
        game = new GUIGameSession();
        game.start();
    }
    protected void disconnect() throws IOException {
        super.disconnect();
        Main.returnToMenu();
    }
}
