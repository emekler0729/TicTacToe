package io.github.emekler0729.TicTacToe;

import io.github.emekler0729.TicTacToe.GUI.*;
import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
    private static JFrame titlemenu;
    private static JFrame game;
    private static Socket server;

    private static PrintWriter toServer;
    private static BufferedReader fromServer;

    public static void main(String[] args) {
        Client client = new Client();
    }

    private Client() {
        titlemenu = new MainMenu(this);
    }

    public void launchSingleplayer() {
        titlemenu.setVisible(false);
        try {
            Server local = new Server(true);
            server = new Socket("localhost", 9090);

            toServer = new PrintWriter(server.getOutputStream(),true);
            fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
        }
        catch(IOException e) {

        }

        game = new GameBoard(this);
    }

    public void launchSplitscreen() {
        JOptionPane.showMessageDialog(null, "Oops! Not yet implemented.");
    }

    public void launchMultiplayer() {
        JOptionPane.showMessageDialog(null, "Oops! Not yet implemented.");
    }

    public void sendRequest(String s) {
        toServer.println(s);
    }
}
