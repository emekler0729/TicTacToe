package io.github.emekler0729.TicTacToe;

import java.net.*;
import java.io.*;
import java.util.Date;

class Server implements TicTacToeProtocol {
    // Server Member Variables
    private ServerSocket listener;
    private Socket player1;
    private Socket player2;
    private GameSession game;
    private GameState state;
    private CommDriver comms;
    private DebugConsole console;

    public Server(boolean enableDebug, int port) throws IOException {

        console = new DebugConsole(enableDebug, "Tic Tac Toe Server Console");

        comms = new CommDriver();
        listener = new ServerSocket(port);

        console.println("Server started at " + new Date() + " on port number " + port);


        InitializeThread initialize = new InitializeThread();
        initialize.start();
    }

    private class InitializeThread extends Thread {
        public InitializeThread() {

        }

        public void run() {
            try {
                comms = new CommDriver();

                console.println("Waiting for connection from Player 1...");
                player1 = listener.accept();
                console.println("Connection made from Player 1 at " + player1.getInetAddress() + ":" + player1.getPort());
                comms.setP1IO(new BufferedReader(new InputStreamReader(player1.getInputStream())), new PrintWriter(player1.getOutputStream(), true));
                console.println("Player 1 I/O streams established.");
                comms.toP1(TTTP_MSG + "You are Player 1... waiting for Player 2");

                console.println("Waiting for connection from Player 2...");
                player2 = listener.accept();
                console.println("Connection made from Player 2 at " + player2.getInetAddress() + ":" + player2.getPort() + ".");
                comms.setP2IO(new BufferedReader(new InputStreamReader(player2.getInputStream())), new PrintWriter(player2.getOutputStream(), true));
                console.println("Player 2 I/O streams established.");
                comms.toP1(TTTP_MSG + "Player 2 has joined... starting the game");
                comms.toP2(TTTP_MSG + "You are Player 2... starting the game");


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

            comms.toP1(TTTP_SYMBOL + "X");
            comms.toP2(TTTP_SYMBOL + "O");

            comms.toAll(TTTP_START);
            comms.toAll(TTTP_MSG + "Player 1's turn.");

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
                            comms.toP1(TTTP_MSG + "Invalid move. Try again.");
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
                            comms.toP2(TTTP_MSG + "Invalid move. Try again.");
                        }
                    }
                }

                state.checkState();
                currentPlayer = currentPlayer == 'X' ? 'O' : 'X';

                if(currentPlayer == 'O') {
                    comms.toAll(TTTP_MSG + "Player 2's turn.");
                }
                else {
                    comms.toAll(TTTP_MSG + "Player 1's turn.");
                }

                validMove = false;
            }

            comms.toAll(TTTP_MSG + "Game Over!");

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

            comms.toAll(TTTP_MSG + "Waiting for opponent...");

            boolean try1 = true;
            boolean try2 = true;
            boolean tryAgain = false;
            String P1Again = "";
            String P2Again = "";

            do {
                tryAgain = false;

                if(try1) {
                    P1Again = comms.fromP1();
                    try1 = false;
                }
                if(try2) {
                    P2Again = comms.fromP2();
                    try2 = false;
                }

                if (P1Again.equals(TTTP_EXIT) || P2Again.equals(TTTP_EXIT)) {
                    disconnect();
                } else if (P1Again.equals(TTTP_PLAY_AGAIN) && P2Again.equals(TTTP_PLAY_AGAIN)) {
                    playAgain();
                }
                else {
                    tryAgain = true;
                    if(!(P1Again.equals(TTTP_PLAY_AGAIN) || P1Again.equals(TTTP_EXIT))) {
                        try1 = true;
                    }
                    if(!(P2Again.equals(TTTP_PLAY_AGAIN) || (P2Again.equals(TTTP_EXIT)))) {
                        try2 = true;
                    }
                }
            } while(tryAgain);
        }

        private int parseMsg(String s) {
            if(s.startsWith(TTTP_MOVE)) {
                return Integer.parseInt(s.substring(s.length()-1));
            }

            else {
                return -1;
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
            console.println("Message sent to all: " + s);
        }
        public void toP1(String s) {
            p1Out.println(s);
            console.println("Message sent to Player 1: " + s);
        }
        public void toP2(String s) {
            p2Out.println(s);
            console.println("Message sent to Player 2: " + s);
        }

        public String fromP1() {
            String s;
            try {
                s = new String(p1In.readLine());
                console.println("Message received from Player 1: " + s);
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
                console.println("Message received from Player 2: " + s);
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


    private void disconnect() {
        comms.toAll(TTTP_EXIT);
        comms = null;
        console.println("All I/O streams shut down.");

        try {
            player1.close();
            console.println("Player 1 socket closed.");
            player2.close();
            console.println("Player 2 socket closed.");
            listener.close();
            console.println("Server socket closed.");
        }

        catch(IOException e) {

        }

        console.dispose();
    }
    private void playAgain() {
        game = new GameSession();

        game.start();
    }
}
