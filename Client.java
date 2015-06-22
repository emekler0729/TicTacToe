package io.github.emekler0729.TicTacToe;

import io.github.emekler0729.TicTacToe.GUI.*;
import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
    private static JFrame mainmenu;
    private static JFrame gameboard;
    private static Socket server;
    private static CommsDriver comms;

    public static void main(String[] args) {
        Client client = new Client();
    }

    private Client() {
        mainmenu = new MainMenu(this);
    }

    public void launchSingleplayer() {
        mainmenu.setVisible(false);
        try {
            Server local = new Server(true);
            server = new Socket("localhost", 9090);
            comms = new CommsDriver();
        }
        catch(IOException e) {

        }

        gameboard = new GameBoard(this);
    }

    public void launchSplitscreen() {
        JOptionPane.showMessageDialog(null, "This mode is not yet implemented", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void launchMultiplayer() {
        JOptionPane.showMessageDialog(null, "This mode is not yet implemented", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void sendRequest(String s) {
        comms.sendRequest(s);
    }

    private class CommsDriver {
        private PrintWriter toServer;
        private BufferedReader fromServer;

        private boolean serverAccess = false;

        public CommsDriver() {
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
    }

}
