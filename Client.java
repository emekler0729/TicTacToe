package io.github.emekler0729.TicTacToe;

import io.github.emekler0729.TicTacToe.GUI.*;
import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
    // Client Member Variables
    private static MainMenu mainmenu;
    private static GameBoard gameboard;
    private static Socket server;
    private static GameSession game;
    private static CommsDriver comms;

    // Entry Point
    public static void main(String[] args) {
        Client client = new Client();
    }

    // Instantiates Main Menu
    private Client() {
        mainmenu = new MainMenu(this);
    }

    private class CommsDriver {
        private PrintWriter toServer;
        private BufferedReader fromServer;

        private boolean serverAccess = false;

        public CommsDriver() {
            try {
                toServer = new PrintWriter(server.getOutputStream(), true);
                fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
            }

            catch(IOException e) {

            }
        }

        public void sendRequest(String s) {
            if(serverAccess) {
                toServer.println(s);
            }
        }
        public String readResponse() {
            String s;
            try {
                s = fromServer.readLine();
                return s;
            }

            catch(IOException e) {
                return "Error";
            }
        }

        public boolean isReady() {
            boolean ready = false;

            try {
                ready = fromServer.ready();
            }

            catch(IOException e) {

            }

            return ready;
        }

        public void enable() {
            serverAccess = true;
        }

        public void disable() {
            serverAccess = false;
        }
    }
    private class GameSession extends Thread {
        int lastMove;
        boolean gameOver = false;
        String msg;
        String player;
        String opponent;

        public GameSession() {

        }

        public void run() {
            while(!gameOver) {
                if(comms.isReady()) {
                    msg = comms.readResponse();
                    parseMsg(msg);
                }
            }
        }

        public void setLastMove(int move) {
            lastMove = move;
        }
        public int getLastMove() {
            return lastMove;
        }

        public void setPlayer(String s) {
            player = s;
        }
        public String getPlayer() { return player; }

        public void setOpponent(String s) {
            opponent = s;
        }
        public String getOpponent() { return opponent; }
    }

    // Re-write launch functions block of code to be a single initializer
    public void launchSingleplayer() {
        mainmenu.setVisible(false);
        try {
            Server local = new Server(true);
            server = new Socket("localhost", 9090);
            comms = new CommsDriver();
        }
        catch(IOException e) {

        }

        gameboard = new GameBoard(this);
        game = new GameSession();
        game.start();
    }
    public void launchSplitscreen() {
        JOptionPane.showMessageDialog(null, "This mode is not yet implemented", "Error", JOptionPane.ERROR_MESSAGE);
    }
    public void launchMultiplayer() {
        JOptionPane.showMessageDialog(null, "This mode is not yet implemented", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void requestMove(int move) {
        game.setLastMove(move);
        comms.sendRequest("MOVE " + move);
    }

    private static void parseMsg(String s) {
        if(s.equals("START")) {
            comms.enable();
        }
        else if(s.startsWith("SYMBOL")) {
            String c = s.substring(s.length()-1);
            game.setPlayer(c);
            c = c.equals("X") ? "O" : "X";
            game.setOpponent(c);
        }
        else if(s.equals("VALID_MOVE")) {
            gameboard.updateBoard(game.getLastMove(), game.getPlayer());
        }

        else if(s.startsWith("OPPONENT_MOVE")) {
            gameboard.updateBoard(Integer.parseInt(s.substring(s.length()-1)),game.getOpponent());
        }
    }
}
