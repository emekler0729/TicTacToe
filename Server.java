package io.github.emekler0729.TicTacToe;

import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.Date;

public class Server {
    private static final int defaultPort = 9090;
    private static ServerSocket listener;
    private static Socket player1;
    private static Socket player2;
    private static GameState state;

    private static CommDriver comms;

    private static boolean debug;
    private static JTextArea jtaConsole;

    public Server(boolean enableDebug) throws IOException {
        if(enableDebug) {
            debug = true;
            jtaConsole = setupConsole();
        }

        comms = new CommDriver();
        listener = new ServerSocket(defaultPort);
        if(debug) {
            jtaConsole.append("Server started at " + new Date() + " on port number " + defaultPort + ".\n");
        }

        InitializeThread initialize = new InitializeThread();
        initialize.start();
    }

    private class InitializeThread extends Thread {
        public InitializeThread() {

        }

        public void run() {
            try {
                if(debug) {
                    jtaConsole.append("Waiting for connection from player1...\n");
                }
                player1 = listener.accept();
                if (debug) {
                    jtaConsole.append("Connection made from player1 at " + player1.getInetAddress() + ":" + player1.getPort() + ".\n");
                }

                comms.setP1IO(new BufferedReader(new InputStreamReader(player1.getInputStream())), new PrintWriter(player1.getOutputStream(),true));

                if (debug) {
                    jtaConsole.append("Player 1 I/O streams established.\n");
                }

                comms.toP1("SYMBOL X");

                if(debug) {
                    jtaConsole.append("Waiting for connection from player2...\n");
                }
                player2 = listener.accept();
                if (debug) {
                    jtaConsole.append("Connection made from player2 at " + player2.getInetAddress() + ":" + player2.getPort() + ".\n");
                }

                comms.setP2IO(new BufferedReader(new InputStreamReader(player2.getInputStream())), new PrintWriter(player2.getOutputStream(),true));

                if (debug) {
                    jtaConsole.append("Player 2 I/O streams established.\n");
                }

                comms.toP2("SYMBOL O");

                GameSession game = new GameSession();
                game.start();


            }
            catch(IOException e) {
                jtaConsole.append(e.toString() + "\n");
            }
        }
    }

    private class GameSession extends Thread {
        public GameSession() {

        }
        public void run() {
            state = new GameState();

            comms.toAll("START");
            if(debug) {
                jtaConsole.append("START command sent to all... Initialization completed.\n");
            }

            while(!state.isEnded()) {
                if(debug) {
                    jtaConsole.append(comms.fromP1() + "\n");
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
        }
        public void toP1(String s) {
            p1Out.println(s);
        }
        public void toP2(String s) {
            p2Out.println(s);
        }

        public String fromP1() {
            String s;
            try {
                s = new String(p1In.readLine());
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
        private int turnsTaken;
        private char board[] = new char[9];

        public GameState() {
            isWon = false;
            isDraw = false;
            turnsTaken = 0;

            for(int i = 0; i < 9; i++) {
                board[i] = ' ';
            }
        }

        public boolean isEnded() {
            return (isWon || isDraw);
        }
    }

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
}
