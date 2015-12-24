package io.github.emekler0729.TicTacToe;

import io.github.emekler0729.TicTacToe.AIStrategy.AbstractStrategy;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    private static GUIMainMenu menu;
    private static GUIClient client;
    private static AbstractClient localOpponent;
    private static Server localServer;
    private static boolean bDebugEnabled;
    private static int difficulty = AbstractStrategy.HARD_DIFFICULTY;

    static final int SINGLEPLAYER_MODE = 0;
    static final int SPLITSCREEN_MODE = 1;
    static final int MULTIPLAYER_MODE = 2;

    private static final int HOST = 0;
    private static final int JOIN = 1;

    private Main() {

    }

    public static void main(String[] args) {
        bDebugEnabled = false;

        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("debug")) {
                bDebugEnabled = true;
            }
        }

        menu = new GUIMainMenu();
    }

    static void initializeGame(final int GAME_MODE) {
        InetSocketAddress adr;
        int choice = HOST;

        menu.setVisible(false);

        if(GAME_MODE == MULTIPLAYER_MODE) {
            choice = JOptionPane.showOptionDialog(null,"Select Host or Join","Online Options",
                    JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,new Object[]{"Host","Join"},"Host");
            if(choice == HOST) {
                adr = new InetSocketAddress("localhost",9090);
            }
            else {
                adr = getHostAddress();
            }
        }

        else {
            adr = new InetSocketAddress("localhost",9090);
        }

        if(choice != JOIN) {
            try {
                localServer = new Server(bDebugEnabled, 9090);
            }
            catch(IOException e) {
                JOptionPane.showMessageDialog(null,"Failed to initialize Server.","Error",JOptionPane.ERROR_MESSAGE);
            }
        }

        try {
            client = new GUIClient(adr);

            if (GAME_MODE == SINGLEPLAYER_MODE) {
                localOpponent = new AIClient(adr, difficulty);
            } else if (GAME_MODE == SPLITSCREEN_MODE) {
//            localOpponent = new LocalClient(adr);
            }
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(null,"Cannot connect to host.","Error",JOptionPane.ERROR_MESSAGE);
            returnToMenu();
        }
    }
    private static InetSocketAddress getHostAddress() {
        InetSocketAddress adr;
        boolean goodAddress = false;
        String s;
        String[] address = new String[2];

        while(!goodAddress) {
            s = JOptionPane.showInputDialog(null, "Enter the host socket address (xxx.xxx.xxx.xxx:nnnnn):",
                    "Host Information", JOptionPane.PLAIN_MESSAGE);

            s = s.trim();

            address = s.split(":");
            if(address.length == 2) {
                goodAddress = true;
            }
        }

        adr = new InetSocketAddress(address[0],Integer.parseInt(address[1]));

        return adr;
    }
    static void returnToMenu() {
        menu.setVisible(true);
    }
    static void setDifficulty(int difficulty) {
        if (!(difficulty == AbstractStrategy.EASY_DIFFICULTY || difficulty == AbstractStrategy.MEDIUM_DIFFICULTY
                || difficulty == AbstractStrategy.HARD_DIFFICULTY)) {
            Main.difficulty = AbstractStrategy.MEDIUM_DIFFICULTY;
        }
        else {
            Main.difficulty = difficulty;
        }
    }
}
