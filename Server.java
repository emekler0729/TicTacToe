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

    private static boolean debug;
    private static JTextArea jtaConsole;

    public Server(boolean enableDebug) throws IOException {
        if(enableDebug) {
            debug = true;
            jtaConsole = setupConsole();
        }

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
                player1 = listener.accept();
                if (debug) {
                    jtaConsole.append("Connection made from player1 at " + player1.getInetAddress() + ":" + player1.getPort() + ".\n");
                }

                player2 = listener.accept();
                if (debug) {
                    jtaConsole.append("Connection made from player2 at " + player2.getInetAddress() + ":" + player2.getPort() + ".\n");
                }
            }
            catch(IOException e) {
                jtaConsole.append(e.toString() + "\n");
            }
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
