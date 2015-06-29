package io.github.emekler0729.TicTacToe;

import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.Date;

public class Server implements TicTacToeProtocol {
    // Server Member Variables
    private ServerSocket listener;
    private Socket player1;
    private Socket player2;
    private GameSession game;
    private GameState state;
    private CommDriver comms;
    private ServerConsole console;

    public Server(boolean enableDebug, int port) throws IOException {

        console = new ServerConsole(enableDebug);

        comms = new CommDriver();
        listener = new ServerSocket(port);

        console.debugMsg("Server started at " + new Date() + " on port number " + port);


        InitializeThread initialize = new InitializeThread();
        initialize.start();
    }

    private class InitializeThread extends Thread {
        public InitializeThread() {

        }

        public void run() {
            try {
                comms = new CommDriver();

                console.debugMsg("Waiting for connection from Player 1...");
                player1 = listener.accept();
                console.debugMsg("Connection made from Player 1 at " + player1.getInetAddress() + ":" + player1.getPort());
                comms.setP1IO(new BufferedReader(new InputStreamReader(player1.getInputStream())), new PrintWriter(player1.getOutputStream(), true));
                console.debugMsg("Player 1 I/O streams established.");
                comms.toP1(TTTP_SYMBOL + "X");

                console.debugMsg("Waiting for connection from Player 2...");
                player2 = listener.accept();
                console.debugMsg("Connection made from Player 2 at " + player2.getInetAddress() + ":" + player2.getPort() + ".");
                comms.setP2IO(new BufferedReader(new InputStreamReader(player2.getInputStream())), new PrintWriter(player2.getOutputStream(),true));
                console.debugMsg("Player 2 I/O streams established.");
                comms.toP2(TTTP_SYMBOL + "O");

                game = new GameSession();
                game.start();


            }
            catch(IOException e) {

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

//            msg = comms.fromP1();
//            if(msg.equals(TTTP_PLAY_AGAIN)) {
//                msg = comms.fromP2();
//                if(msg.equals((TTTP_PLAY_AGAIN))) {
//                    game = new GameSession();
//                    game.start();
//                }
//                else {
                    disconnect();
//                }
//            }
//            else {
//                disconnect();
//            }
            // Else Exit
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
            console.debugMsg("Message sent to all: " + s);
        }
        public void toP1(String s) {
            p1Out.println(s);
            console.debugMsg("Message sent to Player 1: " + s);
        }
        public void toP2(String s) {
            p2Out.println(s);
            console.debugMsg("Message sent to Player 2: " + s);
        }

        public String fromP1() {
            String s;
            try {
                s = new String(p1In.readLine());
                console.debugMsg("Message received from Player 1: " + s);
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
                console.debugMsg("Message received from Player 2: " + s);
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
    private void disconnect() {
        comms = null;
        console.debugMsg("All I/O streams shut down.");

        try {
            player1.close();
            console.debugMsg("Player 1 socket closed.");
            player2.close();
            console.debugMsg("Player 2 socket closed.");
            listener.close();
            console.debugMsg("Server socket closed.");
        }

        catch(IOException e) {

        }

        console.dispose();
    }
}
