package io.github.emekler0729.TicTacToe;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;

class GUIClient extends AbstractClient {
    private MainMenuGUI mainmenu;

    private boolean bDebugEnabled;

    GUIClient(boolean debugEnabled) {
        bDebugEnabled = debugEnabled;
        mainmenu = new MainMenuGUI(this);
    }

    GUIClient() {
        this(false);
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
        protected void askPlayAgain() {
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

    protected void initializeGame() {
        mainmenu.setVisible(false);
        int choice = JOptionPane.showOptionDialog(null,"Select Host or Join","Online Options",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,new Object[]{"Host","Join"},"Host");
        if(choice == 0) {
            try {
                Server local = new Server(bDebugEnabled, 9090);
                iostream = new SocketIOStream(new InetSocketAddress("localhost",9090),true);
            } catch (IOException e) {

            }
        }

        else {
            String socketAddress = JOptionPane.showInputDialog(null, "Enter socket address: (xxx.xxx.xxx.xxx:nnnnn)","Enter Host Address",JOptionPane.QUESTION_MESSAGE);
            socketAddress = socketAddress.trim();
            String address[] = socketAddress.split(":");
            try {
                if (address.length != 2) {
                    throw(new IOException());
                }
                InetSocketAddress adr = new InetSocketAddress(address[0],Integer.parseInt(address[1]));
                iostream = new SocketIOStream(adr,true);
            }
            catch (IOException e) {
                JOptionPane.showMessageDialog(null,"Unable to connect to host", "Error", JOptionPane.ERROR_MESSAGE);
                mainmenu.setVisible(true);
                return;
            }
        }

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
        mainmenu.setVisible(true);
    }
}
