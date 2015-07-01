package io.github.emekler0729.TicTacToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


class GameBoard extends JFrame implements TicTacToeProtocol {
    private GUIClient client;
    private JButton button[] = new JButton[9];
    private JTextField text = new JTextField(25);

    private String playerSymbol;
    private String opponentSymbol;
    private int lastMove;
    private boolean lock = true;

    public GameBoard(GUIClient client) {
        super("Tic Tac Toe");

        this.client = client;

        setSize(300,320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add(setupBoard(),BorderLayout.CENTER);
        add(setupTextField(),BorderLayout.SOUTH);

        setVisible(true);
    }

    void updateBoard(String s) {
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
    void updateText(String s) {
        s = s.substring(4);
        text.setText(s);
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

    void setPlayerSymbol(String s) {
        playerSymbol = s;
    }
    void setOpponentSymbol(String s) {
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
                client.iostream.println(TTTP_MOVE + i);
            }
        }
    }
}
