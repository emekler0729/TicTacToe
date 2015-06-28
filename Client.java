package io.github.emekler0729.TicTacToe;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client implements TicTacToeProtocol {
    // Client Member Variables
    private static MainMenu mainmenu;
    private static GameBoard gameboard;
    private static Socket server;
    private static GameSession game;
    public CommsDriver comms;

    // Entry Point
    public static void main(String[] args) {
        Client client = new Client();
    }

    // Instantiates Main Menu
    private Client() {
        mainmenu = new MainMenu(this);
    }

    public class CommsDriver {
        private PrintWriter toServer;
        private BufferedReader fromServer;

        private boolean serverAccess = false;

        CommsDriver() {
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
                serverAccess = false;
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

        public void enable() {
            serverAccess = true;
        }

        public void disable() {
            serverAccess = false;
        }
    }
    private class GameSession extends Thread {
        boolean gameOver = false;
        boolean firstPlayer = false;
        String msg;

        public GameSession() {

        }

        public void run() {
            while(!gameOver) {
                msg = comms.readResponse();
                parseMsg(msg);
            }
        }

        private void parseMsg(String s) {
            if(s.equals(TTTP_START)) {
                comms.enable();
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
                comms.enable();
            }

            else if(s.startsWith(TTTP_OPPONENT_MOVE)) {
                gameboard.updateBoard(s);
                comms.enable();
            }

            else if(s.equals(TTTP_WIN) || s.equals(TTTP_LOSE) || s.equals(TTTP_DRAW)) {
                gameOver = true;
            }
        }

    }

    public void newGame() {
        mainmenu.setVisible(false);
        // Only if Hosting
        try {
            Server local = new Server(true);
            server = new Socket("localhost", 9090);
        }
        catch(IOException e) {

        }

        comms = new CommsDriver();
        gameboard = new GameBoard(this);
        game = new GameSession();
        game.start();
    }
}
