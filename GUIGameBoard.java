package io.github.emekler0729.TicTacToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


class GUIGameBoard extends AbstractGameBoard implements TicTacToeProtocol {
    private JFrame frame;
    private JButton button[] = new JButton[9];
    private JTextField text = new JTextField(25);

    GUIGameBoard(GUIClient client) {
        super(client);
        frame = new JFrame("Tic Tac Toe");

        frame.setSize(300, 320);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.add(setupBoard(),BorderLayout.CENTER);
        frame.add(setupTextField(),BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    protected void updateText(String s) {
        s = s.substring(4);
        text.setText(s);
    }
    protected void updateView(int move, String symbol) {
        button[move].setText(symbol);
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
    private JPanel setupTextField() {
        JPanel panel = new JPanel();

        text.setEditable(false);

        panel.add(text);

        return panel;
    }

    protected void disposeView() {
        frame.dispose();
    }
    protected void takeTurn() {lock = false;}

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
    }
}
