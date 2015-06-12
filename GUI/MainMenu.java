package io.github.emekler0729.TicTacToe.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame {
    private static JButton jbtSingleplayer = new JButton("1P");
    private static JButton jbtSplitscreen = new JButton("2P");
    private static JButton jbtMultiplayer = new JButton("Online");
    private static JButton jbtAbout = new JButton("About");

    public MainMenu() {
        super("Tic Tac Toe");

        setSize(300,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add(setupTitle(), BorderLayout.NORTH);
        add(setupTitleImage(), BorderLayout.CENTER);
        add(setupButtons(), BorderLayout.SOUTH);

        ButtonListener listener = new ButtonListener();

        jbtSingleplayer.addActionListener(listener);
        jbtSplitscreen.addActionListener(listener);
        jbtMultiplayer.addActionListener(listener);
        jbtAbout.addActionListener(listener);

        setVisible(true);
    }

    private static JPanel setupTitle() {
        JPanel panel = new JPanel();

        panel.add(new JLabel("Welcome to Tic Tac Toe!"));

        return panel;
    }

    private static JPanel setupTitleImage() {
        JPanel panel = new JPanel();

        panel.add(new JLabel("Insert Picture here."));

        return panel;
    }

    private static JPanel setupButtons() {
        JPanel panel = new JPanel(new FlowLayout());

        panel.add(jbtSingleplayer);
        panel.add(jbtSplitscreen);
        panel.add(jbtMultiplayer);
        panel.add(jbtAbout);

        return panel;
    }

    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object button = e.getSource();

            if(button == jbtSingleplayer) {

            }

            else if(button == jbtSplitscreen) {

            }

            else if(button == jbtMultiplayer) {

            }

            else if(button == jbtAbout) {
                JOptionPane.showMessageDialog(null, "Source code at https://github.com/emekler0729/TicTacToe");
            }
        }
    }
}
