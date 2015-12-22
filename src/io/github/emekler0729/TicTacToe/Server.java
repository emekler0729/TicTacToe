package io.github.emekler0729.TicTacToe;

import io.github.emekler0729.TicTacToe.Utility.DebugConsole;
import io.github.emekler0729.TicTacToe.Utility.SocketIOStream;

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
                comms.setP1IO(player1);
                console.println("Player 1 I/O streams established.");
                comms.toP1(TTTP_MSG + "You are Player 1... waiting for Player 2");

                console.println("Waiting for connection from Player 2...");
                player2 = listener.accept();
                console.println("Connection made from Player 2 at " + player2.getInetAddress() + ":" + player2.getPort() + ".");
                comms.setP2IO(player2);
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
        private boolean validMove = false;
        private int move;
        private String msg;
        private int currentPlayer;
        private char currentPlayerSymbol;
        private char p1Symbol;


        public GameSession() {

        }
        public void run() {
            initializeGameSession();

            while(!state.isEnded()) {
                playGame();
            }

            endGame();

            checkPlayAgain();

        }

        private void initializeGameSession() {
            state = new GameState();

            currentPlayer = (int)((Math.floor(Math.random()*100)%2)+1);

            if (currentPlayer == 1) {
                currentPlayerSymbol = p1Symbol = 'X';
            }
            else {
                currentPlayerSymbol = 'X';
                p1Symbol = 'O';
            }

            comms.toCurrentPlayer(TTTP_SYMBOL + "X");
            comms.toOpposingPlayer(TTTP_SYMBOL + "O");

            comms.toAll(TTTP_START);
            comms.toCurrentPlayer(TTTP_MSG + "Your turn.");
            comms.toOpposingPlayer(TTTP_MSG + "Opponent's turn.");
        }
        private void playGame() {
            while(!validMove) {
                msg = comms.fromCurrentPlayer();
                move = parseMsg(msg);
                if (move == -999) {
                    disconnect();
                }
                else if (state.isValid(move, currentPlayerSymbol)) {
                    validMove = true;
                    comms.toCurrentPlayer(TTTP_VALID_MOVE);
                    comms.toOpposingPlayer(TTTP_OPPONENT_MOVE + move);
                } else {
                    comms.toCurrentPlayer(TTTP_INVALID_MOVE);
                    comms.toCurrentPlayer(TTTP_MSG + "Invalid move. Try again.");
                }
            }

            state.checkState();
            currentPlayer = currentPlayer == 1 ? 2 : 1;
            currentPlayerSymbol = currentPlayerSymbol == 'X' ? 'O' : 'X';

            comms.toCurrentPlayer(TTTP_MSG + "Your turn.");
            comms.toOpposingPlayer(TTTP_MSG + "Opponent's turn.");

            validMove = false;
        };
        private void endGame() {
            comms.toAll(TTTP_MSG + "Game Over!");

            if(state.isDraw()) {
                comms.toAll(TTTP_DRAW);
            }

            else {
                if(state.getWinner() == p1Symbol) {
                    comms.toP1(TTTP_WIN);
                    comms.toP2(TTTP_LOSE);
                }

                else {
                    comms.toP1(TTTP_LOSE);
                    comms.toP2(TTTP_WIN);
                }
            }
        };
        private void checkPlayAgain() {
            comms.toAll(TTTP_MSG + "Waiting for opponent...");

            // Play Again?
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
        };
        private int parseMsg(String s) {
            if(s.startsWith(TTTP_MOVE)) {
                return Integer.parseInt(s.substring(s.length()-1));
            }
            else if(s.equals(TTTP_EXIT)) {
                return -999;
            }
            else {
                return -1;
            }
        }
    }

    private class CommDriver {
        private SocketIOStream p1Stream;
        private SocketIOStream p2Stream;
        private int currentPlayerID;

        private CommDriver() {

        }

        public void setP1IO(Socket p1) {
            try {
                p1Stream = new SocketIOStream(p1);
                p1Stream.setInhibited(false);
            }
            catch (IOException e) {

            }
        }
        public void setP2IO(Socket p2) {
            try {
                p2Stream = new SocketIOStream(p2);
                p2Stream.setInhibited(false);
            }
            catch (IOException e) {

            }
        }

        public void toAll(String s) {
            p1Stream.println(s);
            p2Stream.println(s);
            console.println("Message sent to all: " + s);
        }
        public void toCurrentPlayer(String s) {
            currentPlayerID = game.currentPlayer;
            if (currentPlayerID == 1) {
                p1Stream.println(s);
                console.println("Message sent to Player 1: " + s);
            }

            else {
                p2Stream.println(s);
                console.println("Message sent to Player 2: " + s);
            }
        }
        public void toOpposingPlayer(String s) {
            currentPlayerID = game.currentPlayer;
            if (currentPlayerID == 2) {
                p1Stream.println(s);
                console.println("Message sent to Player 1: " + s);
            }

            else {
                p2Stream.println(s);
                console.println("Message sent to Player 2: " + s);
            }
        }
        public String fromCurrentPlayer() {
            currentPlayerID = game.currentPlayer;
            String s;
            if (currentPlayerID == 1) {
                s = new String(p1Stream.readLine());
                console.println("Message received from Player 1: " + s);
            }
            else {
                s = new String(p2Stream.readLine());
                console.println("Message received from Player 2: " + s);
            }
            return s;
        }
        public void toP1(String s) {
            p1Stream.println(s);
            console.println("Message sent to Player 1: " + s);
        }
        public void toP2(String s) {
            p2Stream.println(s);
            console.println("Message sent to Player 2: " + s);
        }
        public String fromP1() {
            String s;
            s = new String(p1Stream.readLine());
            console.println("Message received from Player 1: " + s);
            return s;
        }
        public String fromP2() {
            String s;
            s = new String(p2Stream.readLine());
            console.println("Message received from Player 2: " + s);
            return s;
        }

        public void close() throws IOException {
            p1Stream.close();
            p2Stream.close();
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
            if (n > -1) {
                boolean valid = board[n / 3][n % 3] == ' ' ? true : false;

                if (valid) {
                    board[n / 3][n % 3] = c;
                    turnsTaken++;
                }

                return valid;
            }
            return false;
        }
    }


    private void disconnect() {
        try {
            game.interrupt();
            comms.toAll(TTTP_EXIT);
            comms.close();
            comms = null;
            console.println("All I/O streams shut down.");
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
        comms.toAll(TTTP_AGAIN);

        game = new GameSession();

        game.start();
    }
}