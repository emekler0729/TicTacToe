package io.github.emekler0729.TicTacToe.GUI;

import javax.swing.*;
import java.awt.*;

public class GameBoard extends JFrame {
    private static JButton button[] = new JButton[9];

    public GameBoard() {
        super("Tic Tac Toe");

        setSize(300,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add(setupBoard());

        setVisible(true);
    }

    private static JPanel setupBoard() {
        JPanel panel = new JPanel(new GridLayout(3,3));

        for(int i = 0; i < 9; i++) {
            button[i] = new JButton();
            panel.add(button[i]);
        }

        return panel;
    }
}
