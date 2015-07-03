package io.github.emekler0729.TicTacToe;

import com.sun.javafx.scene.control.Keystroke;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


class GUIGameBoard extends AbstractGameBoard implements TicTacToeProtocol {
    private JFrame frame;
    private JButton button[] = new JButton[9];
    private JTextField text = new JTextField(25);

    private static final ImageIcon X = new ImageIcon(Main.class.getClassLoader().getResource("icons/x.png"));
    private static final ImageIcon O = new ImageIcon(Main.class.getClassLoader().getResource("icons/o.png"));

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
        ImageIcon icon;

        if (symbol.equals("X")) {
            icon = X;
        }
        else {
            icon = O;
        }

        button[move].setIcon(icon);
    }

    private JPanel setupBoard() {
        JPanel panel = new JPanel(new GridLayout(3,3));
        ButtonAction a;

        for(int i = 0; i < 9; i++) {
            a = new ButtonAction(i);
            button[i] = new JButton(a);
            button[i].getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(a.getNumpadKey(),a.getCommandString());
            button[i].getActionMap().put(a.getCommandString(),a);
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

    private class ButtonAction extends AbstractAction {
        private int buttonVal;
        private KeyStroke numpadKey;
        private String commandString;

        public ButtonAction(int i) {
            buttonVal = i;
            commandString = new String(Integer.toString(i));

            switch(i) {
                case 0:
                    numpadKey = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD7,0);
                    break;
                case 1:
                    numpadKey = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8,0);
                    break;
                case 2:
                    numpadKey = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD9,0);
                    break;
                case 3:
                    numpadKey = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4,0);
                    break;
                case 4:
                    numpadKey = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD5,0);
                    break;
                case 5:
                    numpadKey = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6,0);
                    break;
                case 6:
                    numpadKey = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1,0);
                    break;
                case 7:
                    numpadKey = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2,0);
                    break;
                case 8:
                    numpadKey = KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD3,0);
                    break;
            }

        }

        public void actionPerformed(ActionEvent e) {
            requestMove(buttonVal);
        }

        public KeyStroke getNumpadKey() {
            return numpadKey;
        }
        public String getCommandString() { return commandString; }
    }
}
