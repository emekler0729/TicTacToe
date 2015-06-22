package io.github.emekler0729.TicTacToe.GUI;

import io.github.emekler0729.TicTacToe.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class GameBoard extends JFrame {
    private static Client client;
    private static JButton button[] = new JButton[9];

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

    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == button[0]) {
                client.sendRequest("MOVE 0");
            }

            else if(e.getSource() == button[1]) {
                client.sendRequest("MOVE 1");
            }

            else if(e.getSource() == button[2]) {
                client.sendRequest("MOVE 2");
            }

            else if(e.getSource() == button[3]) {
                client.sendRequest("MOVE 3");
            }

            else if(e.getSource() == button[4]) {
                client.sendRequest("MOVE 4");
            }

            else if(e.getSource() == button[5]) {
                client.sendRequest("MOVE 5");
            }

            else if(e.getSource() == button[6]) {
                client.sendRequest("MOVE 6");
            }

            else if(e.getSource() == button[7]) {
                client.sendRequest("MOVE 7");
            }

            else if(e.getSource() == button[8]) {
                client.sendRequest("MOVE 8");
            }
        }
    }
}
