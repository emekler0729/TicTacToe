package io.github.emekler0729.TicTacToe.Utility;

import javax.swing.*;

public class DebugConsole extends JFrame {
    JTextArea text;
    JScrollPane scrollPane;
    JScrollBar bar;

    public DebugConsole(boolean enabled, String windowTitle) {
        super(windowTitle);

        text = new JTextArea();
        scrollPane = new JScrollPane(text);
        bar = scrollPane.getVerticalScrollBar();

        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setEditable(false);

        add(scrollPane);

        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(enabled);
    }

    public void println(String s) {
        text.append(s + "\n");
        bar.setValue(bar.getMaximum());
    }
}
