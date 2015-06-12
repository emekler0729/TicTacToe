package io.github.emekler0729.TicTacToe;

import io.github.emekler0729.TicTacToe.GUI.*;
import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private static JFrame titlemenu;
    private static JFrame game;
    private static Socket server;

    public static void main(String[] args) {
        Client client = new Client();
    }

    private Client() {
        titlemenu = new MainMenu(this);
    }

    public static void launchSingleplayer() {
        titlemenu.setVisible(false);
        try {
            Server local = new Server(true);
            server = new Socket("localhost", 9090);
        }
        catch(IOException e) {

        }

        game = new GameBoard();
    }

    public static void launchSplitscreen() {
        JOptionPane.showMessageDialog(null, "Oops! Not yet implemented.");
    }

    public static void launchMultiplayer() {
        JOptionPane.showMessageDialog(null, "Oops! Not yet implemented.");
    }
}
