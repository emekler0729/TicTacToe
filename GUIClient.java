package io.github.emekler0729.TicTacToe;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;

class GUIClient extends AbstractClient {
    private MainMenuGUI mainmenu;
    private GameBoard gameboard;

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

        protected void parseMsg(String s) {
            if(s.startsWith(TTTP_MSG)) {
                gameboard.updateText(s);
            }
            else if(s.equals(TTTP_EXIT)) {
                try {
                    disconnect();
                }
                catch(IOException e) {

                }
            }
            else if(s.equals(TTTP_START) && bFirstPlayer) {
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
            else if(s.equals(TTTP_VALID_MOVE)) {
                gameboard.updateBoard(s);
            }

            else if(s.equals(TTTP_INVALID_MOVE)) {
                gameboard.updateBoard(s);
                iostream.setInhibited(false);
            }

            else if(s.startsWith(TTTP_OPPONENT_MOVE)) {
                gameboard.updateBoard(s);
                iostream.setInhibited(false);
            }

            else if(s.equals(TTTP_WIN) || s.equals(TTTP_LOSE) || s.equals(TTTP_DRAW)) {
                bGameOver = true;

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

        gameboard = new GameBoard(this);
        game = new GUIGameSession();
        game.start();
    }
    protected void playAgain() {
        super.playAgain();

        gameboard.dispose();
        gameboard = new GameBoard(this);

        game = new GUIGameSession();

        game.start();
    }
    protected void disconnect() throws IOException {
        super.disconnect();
        gameboard.dispose();
        mainmenu.setVisible(true);
    }
}
