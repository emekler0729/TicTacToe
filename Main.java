package io.github.emekler0729.TicTacToe;

public class Main {
    public static void main(String[] args) {
        boolean bDebugEnabled = false;

        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("debug")) {
                bDebugEnabled = true;
            }
        }

        GUIClient client = new GUIClient(bDebugEnabled);
    }
}
