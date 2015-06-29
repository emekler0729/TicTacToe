package io.github.emekler0729.TicTacToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame {
    // Communication link to client program
    private static Client client;

    public MainMenu(Client client) {
        super("Tic Tac Toe");

        this.client = client;

        setSize(300,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setJMenuBar(setupMenuBar());
        add(setupTitle(), BorderLayout.NORTH);
        add(setupTitleImage(), BorderLayout.CENTER);
        add(setupButtons(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private static JMenuBar setupMenuBar() {
        // Create new Menu Bar
        JMenuBar jmb = new JMenuBar();

        // Create new Menus
        JMenu file = new JMenu("File");
        JMenu help = new JMenu("Help");

        // Set Menu & Menu Item Mnemonics
        file.setMnemonic('F');
        help.setMnemonic('H');

        // Add File menu Menu Items
        file.add(new MenuAction("Options", 'O'));
        file.addSeparator();
        file.add(new MenuAction("Exit", 'E'));

        // Add Help menu Menu Items
        help.add(new MenuAction("About"));

        // Add Menus to Menu Bar
        jmb.add(file);
        jmb.add(help);

        // Return configured Menu Bar to MainMenu constructor.
        return jmb;
    }
    private static JPanel setupTitle() {
        JPanel panel = new JPanel();

        panel.add(new JLabel("Welcome to Tic Tac Toe!"));

        return panel;
    }
    private static JPanel setupTitleImage() {
        JPanel panel = new JPanel();

        //panel.add(new JLabel("Insert Picture here."));

        return panel;
    }
    private static JPanel setupButtons() {
        JPanel panel = new JPanel(new FlowLayout());

        panel.add(new JButton(new ButtonAction("1P")));
        panel.add(new JButton(new ButtonAction("2P")));
        panel.add(new JButton(new ButtonAction("Online")));

        return panel;
    }

    private static class ButtonAction extends AbstractAction {
        String name;

        ButtonAction(String name) {
            super(name);
            this.name = name;
        }

        public void actionPerformed(ActionEvent e) {
            if(name == "1P") {
                JOptionPane.showMessageDialog(null,"This mode is not yet implemented.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            else if(name == "2P") {
                JOptionPane.showMessageDialog(null,"This mode is not yet implemented.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            else if(name == "Online") {
                client.initializeGame();
            }
        }
    }
    private static class MenuAction extends AbstractAction {
        String name;

        MenuAction(String name) {
            super(name);
            this.name = name;
        }

        MenuAction(String name, int mnemonic) {
            super(name);
            putValue(Action.MNEMONIC_KEY, mnemonic);
            this.name = name;
        }

        public void actionPerformed(ActionEvent e) {
            if(name.equals("Exit")) {
                System.exit(0);
            }

            else if(name.equals("About")) {
                JOptionPane.showMessageDialog(null, "Source code at https://github.com/emekler0729/TicTacToe");
            }

            else if (name.equals("Options")) {
                JOptionPane.showMessageDialog(null, "This menu is not yet implemented.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
