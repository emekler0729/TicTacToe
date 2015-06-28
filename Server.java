package io.github.emekler0729.TicTacToe;

import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.Date;

public class Server implements TicTacToeProtocol {
    // Server Member Variables
    private static final int defaultPort = 9090;
    private static ServerSocket listener;
    private static Socket player1;
    private static Socket player2;
    private static GameSession game;
    private static GameState state;
    private static CommDriver comms;
    private static JTextArea jtaConsole;

    // Server State Variables
    private static boolean debug;

    public Server(boolean enableDebug) throws IOException {
        if(enableDebug) {
            debug = true;
            jtaConsole = setupConsole();
        }

        listener = new ServerSocket(defaultPort);

        debugMsg("Server started at " + new Date() + " on port number " + defaultPort + ".");

        InitializeThread initialize = new InitializeThread();
        initialize.start();
    }
    public Server(boolean enableDebug, int port) throws IOException {
        if(enableDebug) {
            debug = true;
            jtaConsole = setupConsole();
        }

        comms = new CommDriver();
        listener = new ServerSocket(port);
        if(debug) {
            jtaConsole.append("Server started at " + new Date() + " on port number " + port + ".\n");
        }

        InitializeThread initialize = new InitializeThread();
        initialize.start();
    }

    private class InitializeThread extends Thread {
        public InitializeThread() {

        }

        public void run() {
            try {
                comms = new CommDriver();

                debugMsg("Waiting for connection from Player 1...");
                player1 = listener.accept();
                debugMsg("Connection made from Player 1 at " + player1.getInetAddress() + ":" + player1.getPort());
                comms.setP1IO(new BufferedReader(new InputStreamReader(player1.getInputStream())), new PrintWriter(player1.getOutputStream(),true));
                debugMsg("Player 1 I/O streams established.");
                comms.toP1("SYMBOL X");

                debugMsg("Waiting for connection from Player 2...");
                player2 = listener.accept();
                debugMsg("Connection made from Player 2 at " + player2.getInetAddress() + ":" + player2.getPort() + ".");
                comms.setP2IO(new BufferedReader(new InputStreamReader(player2.getInputStream())), new PrintWriter(player2.getOutputStream(),true));
                debugMsg("Player 2 I/O streams established.");
                comms.toP2("SYMBOL O");

                game = new GameSession();
                game.start();


            }
            catch(IOException e) {
                jtaConsole.append(e.toString() + "\n");
            }
        }
    }
    private class GameSession extends Thread {
        boolean validMove = false;
        int move;
        String msg;
        char currentPlayer = 'X';

        public GameSession() {

        }
        public void run() {
            state = new GameState();

            comms.toAll(TTTP_START);

            while(!state.isEnded()) {
                while(!validMove) {
                    if(currentPlayer == 'X') {
                        msg = comms.fromP1();
                        move = parseMsg(msg);
                        if (state.isValid(move, currentPlayer)) {
                            validMove = true;
                            comms.toP1(TTTP_VALID_MOVE);
                            comms.toP2(TTTP_OPPONENT_MOVE + move);
                        }
                        else {
                            comms.toP1(TTTP_INVALID_MOVE);
                        }
                    }

                    else if(currentPlayer == 'O') {
                        msg = comms.fromP2();
                        move = parseMsg(msg);
                        if(state.isValid(move, currentPlayer)) {
                            validMove = true;
                            comms.toP1(TTTP_OPPONENT_MOVE + move);
                            comms.toP2(TTTP_VALID_MOVE);
                        }
                        else {
                            comms.toP2(TTTP_INVALID_MOVE);
                        }
                    }
                }

                state.checkState();
                currentPlayer = currentPlayer == 'X' ? 'O' : 'X';
                validMove = false;
            }

            if(state.isDraw()) {
                comms.toAll(TTTP_DRAW);
            }

            else {
                if(state.getWinner() == 'X') {
                    comms.toP1(TTTP_WIN);
                    comms.toP2(TTTP_LOSE);
                }

                else {
                    comms.toP1(TTTP_LOSE);
                    comms.toP2(TTTP_WIN);
                }
            }
        }
    }

    private class CommDriver {
        private PrintWriter p1Out;
        private PrintWriter p2Out;

        private BufferedReader p1In;
        private BufferedReader p2In;

        private CommDriver() {

        }

        public void setP1IO(BufferedReader in, PrintWriter out) {
            p1In = in;
            p1Out = out;
        }

        public void setP2IO(BufferedReader in, PrintWriter out) {
            p2In = in;
            p2Out = out;
        }

        public void toAll(String s) {
            p1Out.println(s);
            p2Out.println(s);
            debugMsg("Message sent to all: " + s);
        }
        public void toP1(String s) {
            p1Out.println(s);
            debugMsg("Message sent to Player 1: " + s);
        }
        public void toP2(String s) {
            p2Out.println(s);
            debugMsg("Message sent to Player 2: " + s);
        }

        public String fromP1() {
            String s;
            try {
                s = new String(p1In.readLine());
                debugMsg("Message received from Player 1: " + s);
                return s;
            }

            catch(IOException e) {
                return "Error";
            }
        }
        public String fromP2() {
            String s;
            try {
                s = new String(p2In.readLine());
                debugMsg("Message received from Player 2: " + s);
                return s;
            }

            catch(IOException e) {
                return "Error";
            }
        }
    }
    private class GameState {
        private boolean isWon;
        private boolean isDraw;
        private char winner;
        private int turnsTaken;
        private char board[][] = new char[3][3];

        public GameState() {
            isWon = false;
            isDraw = false;
            turnsTaken = 0;

            for(int i = 0; i < 9; i++) {
                board[i/3][i%3] = ' ';
            }
        }

        public boolean isEnded() {
            return (isWon || isDraw);
        }
        public boolean isDraw() { return isDraw; }
        public void checkState() {
            for(int row = 0; row < 3; row ++) {
                if(board[row][0] != ' ' && board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                    isWon = true;
                    winner = board[row][0];
                    return;
                }
            }

            for(int col = 0; col < 3; col++) {
                if(board[0][col] != ' ' && board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                    isWon = true;
                    winner = board[0][col];
                    return;
                }
            }

            if(board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
                isWon = true;
                winner = board[0][0];
                return;
            }

            else if(board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
                isWon = true;
                winner = board[1][1];
                return;
            }

            else if (turnsTaken == 9) {
                isDraw = true;
                return;
            }
        }
        public char getWinner() {
            return winner;
        }
        public boolean isValid(int n, char c) {
            boolean valid = board[n/3][n%3] == ' ' ? true : false;

            if (valid) {
                board[n/3][n%3] = c;
                turnsTaken++;
            }

            else {
                debugMsg("Move was invalid.");
            }

            return valid;
        }
    }

    private static int parseMsg(String s) {
        if(s.startsWith(TTTP_MOVE)) {
            return Integer.parseInt(s.substring(s.length()-1));
        }

        else {
            return -1;
        }
    }


    // Utility functions
    private static JTextArea setupConsole() {
        JFrame frame = new JFrame("Tic Tac Toe Server Console");

        JTextArea text = new JTextArea();

        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(text);

        frame.add(scrollPane);

        frame.setSize(600,300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);

        return text;
    }
    private static void debugMsg(String s) {
        if(debug) {
            jtaConsole.append(s + "\n");
        }
    }
}
