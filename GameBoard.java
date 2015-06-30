package io.github.emekler0729.TicTacToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class GameBoard extends JFrame implements TicTacToeProtocol {
    private static Client client;
    private static JButton button[] = new JButton[9];

    private static String playerSymbol;
    private static String opponentSymbol;
    private static int lastMove;
    private static boolean lock = true;

    public GameBoard(Client client) {
        super("Tic Tac Toe");

        this.client = client;

        setSize(300,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add(setupBoard());

        setVisible(true);
    }

    public void updateBoard(String s) {
        if(s.equals(TTTP_START)) {
            lock = false;
        }

        if(s.equals(TTTP_VALID_MOVE)) {
            button[lastMove].setText(playerSymbol);
        }

        else if(s.startsWith(TTTP_OPPONENT_MOVE)) {
            int i = Integer.parseInt(s.substring(s.length() - 1));
            button[i].setText(opponentSymbol);
            lock = false;
        }

        else if(s.equals(TTTP_INVALID_MOVE)) {
            lastMove = -1;
            lock = false;
        }
    }

    private JPanel setupBoard() {
        JPanel panel = new JPanel(new GridLayout(3,3));

        ButtonListener listener = new ButtonListener();

        for(int i = 0; i < 9; i++) {
            button[i] = new JButton();
            button[i].addActionListener(listener);
            panel.add(button[i]);
        }

        return panel;
    }
    public void setPlayerSymbol(String s) {
        playerSymbol = s;
    }
    public void setOpponentSymbol(String s) {
        opponentSymbol = s;
    }

    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == button[0]) {
                requestMove(0);
            }

            else if(e.getSource() == button[1]) {
                requestMove(1);
            }

            else if(e.getSource() == button[2]) {
                requestMove(2);
            }

            else if(e.getSource() == button[3]) {
                requestMove(3);
            }

            else if(e.getSource() == button[4]) {
                requestMove(4);
            }

            else if(e.getSource() == button[5]) {
                requestMove(5);
            }

            else if(e.getSource() == button[6]) {
                requestMove(6);
            }

            else if(e.getSource() == button[7]) {
                requestMove(7);
            }

            else if(e.getSource() == button[8]) {
                requestMove(8);
            }
        }

        private void requestMove(int i) {
            if(!lock) {
                lastMove = i;
                lock = true;
                client.comms.println(TTTP_MOVE + i);
            }
        }
    }
}
