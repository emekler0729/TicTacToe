package io.github.emekler0729.TicTacToe;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client implements TicTacToeProtocol {
    // Client Member Variables
    private Client instance;
    private MainMenu mainmenu;
    private GameBoard gameboard;
    private Socket serverSocket;
    private GameSession game;
    SocketIODriver comms;

    // Debug flags
    private boolean enableDebug = false;

    // Entry Point
    public static void main(String[] args) {
        Client client = new Client(args);
    }

    // Constructor instantiates main menu and debug settings
    private Client(String[] args) {
        instance = this;
        mainmenu = new MainMenu(this);

        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("debug")) {
                enableDebug = true;
            }
        }
    }

    private class GameSession extends Thread {
        private boolean gameOver = false;
        private boolean firstPlayer = false;
        private String msg;

        private GameSession() {

        }

        public void run() {
            while(!gameOver) {
                msg = comms.readLine();
                parseMsg(msg);
            }

            // Play Again?
            /*
            int choice = JOptionPane.showOptionDialog(null,"Would you like to play again?","Game Over",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,new Object[]{"Yes","No"},"Yes");
            if(choice == 0) {
                comms.setInhibited(false);
                comms.println(TTTP_PLAY_AGAIN);
                gameboard.dispose();
                gameboard = new GameBoard(instance);
                game = new GameSession();

                game.start();
            }
            */
//            else {
                disconnect();
//            }
        }

        private void parseMsg(String s) {
            if(s.startsWith(TTTP_MSG)) {
                gameboard.updateText(s);
            }
            else if(s.equals(TTTP_START) && firstPlayer) {
                comms.setInhibited(false);
                gameboard.updateBoard(s);
            }
            else if(s.startsWith(TTTP_SYMBOL)) {
                String c = s.substring(s.length()-1);
                if(c.equals("X")) {
                    firstPlayer = true;
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
                comms.setInhibited(false);
            }

            else if(s.startsWith(TTTP_OPPONENT_MOVE)) {
                gameboard.updateBoard(s);
                comms.setInhibited(false);
            }

            else if(s.equals(TTTP_WIN) || s.equals(TTTP_LOSE) || s.equals(TTTP_DRAW)) {
                gameOver = true;

                if(msg.equals(TTTP_DRAW)) {
                    JOptionPane.showMessageDialog(null,"The game was a draw","Game Over!",JOptionPane.PLAIN_MESSAGE);
                }
                else if(msg.equals(TTTP_WIN)) {
                    JOptionPane.showMessageDialog(null,"You win the game!","Game Over!",JOptionPane.PLAIN_MESSAGE);
                }
                else if(msg.equals(TTTP_LOSE)) {
                    JOptionPane.showMessageDialog(null,"You lose the game.","Game Over!", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
        private void disconnect() {
            comms.setInhibited(false);
            comms.println(TTTP_EXIT);
            comms = null;

            try {
                serverSocket.close();
            }
            catch(IOException e) {

            }
            gameboard.dispose();
            mainmenu.setVisible(true);
        }
    }

    void initializeGame() {
        mainmenu.setVisible(false);
        int choice = JOptionPane.showOptionDialog(null,"Select Host or Join","Online Options",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,new Object[]{"Host","Join"},"Host");
        if(choice == 0) {
            try {
                Server local = new Server(enableDebug, 9090);
                serverSocket = new Socket("localhost", 9090);
                comms = new SocketIODriver(serverSocket,true);
            } catch (IOException e) {

            }
        }

        else {
            String socketAddress = JOptionPane.showInputDialog(null, "Enter socket address: (xxx.xxx.xxx.xxx:nnnnn)","Enter Host Address",JOptionPane.QUESTION_MESSAGE);
            String address[] = socketAddress.split(":");
            try {
                if (address.length != 2) {
                    throw(new IOException());
                }
                serverSocket = new Socket(address[0],Integer.parseInt(address[1]));
                comms = new SocketIODriver(serverSocket,true);
            }
            catch (IOException e) {
                JOptionPane.showMessageDialog(null,"Unable to connect to host", "Error", JOptionPane.ERROR_MESSAGE);
                mainmenu.setVisible(true);
                return;
            }
        }

        gameboard = new GameBoard(this);
        game = new GameSession();
        game.start();
    }
}
