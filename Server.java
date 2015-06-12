package io.github.emekler0729.TicTacToe;

import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.Date;

public class Server implements Runnable {
    private static final int defaultPort = 9090;
    private static ServerSocket listener;
    private static Socket player1;
    private static Socket player2;

    private static boolean debug;
    private static JTextArea jtaConsole;

    public static void main(String[] args) throws IOException {
        Server server = new Server(true);
    }

    public Server(boolean enableDebug) throws IOException {
        if(enableDebug) {
            debug = true;
            jtaConsole = setupConsole();
        }
        listener = new ServerSocket(defaultPort);
        if(debug) {
                jtaConsole.append("Server started at " + new Date() + " on port " + defaultPort +".\n");
        }
    }

    public void run() {
        try {
            player1 = listener.accept();
            player2 = listener.accept();
        }
        catch(IOException e) {
            if(debug) {
                jtaConsole.append(e.toString());
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
