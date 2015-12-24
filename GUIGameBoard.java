package io.github.emekler0729.TicTacToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


class GUIGameBoard extends AbstractGameBoard implements TicTacToeProtocol {
    private JFrame frame;
    private JButton button[] = new JButton[9];
    private JTextField text = new JTextField(25);
    private JLabel winLabel = new JLabel("0");
    private JLabel loseLabel = new JLabel("0");
    private JLabel drawLabel = new JLabel("0");

    private static final ImageIcon X = new ImageIcon(Main.class.getClassLoader().getResource("icons/x.png"));
    private static final ImageIcon O = new ImageIcon(Main.class.getClassLoader().getResource("icons/o.png"));

    GUIGameBoard(final GUIClient client) {
        super(client);
        frame = new JFrame("Tic Tac Toe");

        frame.setSize(300, 370);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        frame.add(setupBoard(),BorderLayout.CENTER);
        frame.add(setupTextField(),BorderLayout.SOUTH);
        frame.add(setupScoreField(),BorderLayout.NORTH);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    client.disconnect();
                }
                catch (IOException er) {
                    System.out.println("Disconnect error occurred.");
                }
            }
        });

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
    protected void updateScore(int w, int l, int d) {
        winLabel.setText(new String(Integer.toString(w)));
        loseLabel.setText(new String(Integer.toString(l)));
        drawLabel.setText(new String(Integer.toString(d)));
    }
    public Object getView() {
        return this;
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
    private JPanel setupScoreField() {
        JPanel panel = new JPanel();
        JPanel winPanel = new JPanel();
        JPanel losePanel = new JPanel();
        JPanel drawPanel = new JPanel();
        JPanel sepPanel = new JPanel();
        JPanel sepPanel2 = new JPanel();

        winPanel.add(new JLabel("Wins: "));
        winPanel.add(winLabel);
        losePanel.add(new JLabel("Losses: "));
        losePanel.add(loseLabel);
        drawPanel.add(new JLabel("Draws: "));
        drawPanel.add(drawLabel);

        sepPanel.add(new JLabel("-"));
        sepPanel2.add(new JLabel("-"));

        panel.add(winPanel);
        panel.add(sepPanel);
        panel.add(losePanel);
        panel.add(sepPanel2);
        panel.add(drawPanel);

        return panel;
    }

    protected void disposeView() {
        frame.dispose();
    }
    protected void takeTurn() {lock = false;}

    class ButtonAction extends AbstractAction {
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
