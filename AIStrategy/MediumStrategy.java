package io.github.emekler0729.TicTacToe.AIStrategy;

import java.util.ArrayList;

public class MediumStrategy extends AbstractStrategy {

    public MediumStrategy() {
    }

    public int chooseMove(String[][] board) {
        int move;
        Option bestOption;

        scanBoard(board);

        bestOption = Option.getBestOption();

        move = bestOption.getRow()*3+bestOption.getColumn();

        return move;
    }
    protected void scanBoard(String[][] board) {
        Line line;
        String[] lineString = new String[3];

        // Scan Rows
        for(int i = 0; i < 3; i++) {
            lineString[0] = board[i][0];
            lineString[1] = board[i][1];
            lineString[2] = board[i][2];

            line = new Line(lineString,Line.ROW,i);
            scanLine(line);
        }

        // Scan Columns
        for(int i = 0; i < 3; i++) {
            lineString[0] = board[0][i];
            lineString[1] = board[1][i];
            lineString[2] = board[2][i];

            line = new Line(lineString,Line.COLUMN,i);
            scanLine(line);
        }

        // Scan Diagonal 1
        for(int i = 0; i < 3; i++) {
            lineString[i] = board[i][i];
        }
        line = new Line(lineString,Line.DIAGONAL,0);
        scanLine(line);

        // Scan Diagonal 2
        for(int i = 0; i < 3; i++) {
            lineString[i] = board[i][2-i];
        }
        line = new Line(lineString,Line.DIAGONAL,1);
        scanLine(line);
    }
    protected void scanLine(Line line) {
        int xCount = 0;
        int oCount = 0;
        String v;
        ArrayList<Integer> emptyCells = new ArrayList<Integer>();

        for(int i = 0; i < 3; i++) {
            v = line.getCellValue(i);
            if(v.equals("X")) {
                xCount++;
            }
            else if(v.equals("O")) {
                oCount++;
            }
            else {
                emptyCells.add(new Integer(i));
            }
        }

        for(int i = 0; i < emptyCells.size(); i++) {
            Integer cell = emptyCells.get(i);
            if (xCount == 1 || (xCount == 0 && oCount == 0)) {
                // Options are IGNORE
                new Option(Option.IGNORE,line.getCellRow(cell),line.getCellColumn(cell));
            } else if (oCount == 1) {
                // Options are VECTOR
                new Option(Option.VECTOR,line.getCellRow(cell),line.getCellColumn(cell));
            } else if (xCount == 2 && oCount == 0) {
                // Options are BLOCK
                new Option(Option.BLOCK, line.getCellRow(cell),line.getCellColumn(cell));
            } else if (xCount == 0 && oCount == 2) {
                // Options are WIN
                new Option(Option.WIN,line.getCellRow(cell),line.getCellColumn(cell));
            }
        }
    }
}


class Option {
    private int row;
    private int column;

    private static int maxStrategicValue;

    private static ArrayList<Option> wins = new ArrayList<Option>();
    private static ArrayList<Option> blocks = new ArrayList<Option>();
    private static ArrayList<Option> vectors = new ArrayList<Option>();
    private static ArrayList<Option> ignores = new ArrayList<Option>();

    public static final int WIN = 3;
    public static final int BLOCK = 2;
    public static final int VECTOR = 1;
    public static final int IGNORE = 0;

    public Option(int strategicValue, int row, int column) {
        if(strategicValue > maxStrategicValue) {
            maxStrategicValue = strategicValue;
        }
        this.row = row;
        this.column = column;

        switch(strategicValue) {
            case WIN:
                wins.add(this);
                break;
            case BLOCK:
                blocks.add(this);
                break;
            case VECTOR:
                vectors.add(this);
                break;
            case IGNORE:
                ignores.add(this);
                break;
        }
    }

    public static Option getBestOption() {
        ArrayList<Option> choices = new ArrayList<Option>();
        Option bestOption = new Option(0,0,0);

        switch (maxStrategicValue) {
            case WIN:
                choices = wins;
                break;
            case BLOCK:
                choices = blocks;
                break;
            case VECTOR:
                choices = vectors;
                break;
            case IGNORE:
                choices = ignores;
                break;
        }

        if (choices.size() > 0) {
            bestOption = choices.get((int) Math.floor(Math.random() * choices.size()));
        }

        resetTracking();
        return bestOption;
    }

    public int getRow() {
        return row;
    }
    public int getColumn() {
        return column;
    }

    private static void resetTracking() {
        maxStrategicValue = 0;

        wins = new ArrayList<Option>();
        blocks = new ArrayList<Option>();
        vectors = new ArrayList<Option>();
        ignores = new ArrayList<Option>();
    }
}

class Line {
    public static final int ROW = 0;
    public static final int COLUMN = 1;
    public static final int DIAGONAL = 2;

    private String[] line;
    private int direction;
    private int option;

    public Line(String[] line, int direction, int option) {
        this.line = line;
        this.direction = direction;
        this.option = option;
    }

    public String getCellValue(int i) {
        return line[i];
    }

    public int getCellRow(int i) {
        if (direction == ROW) {
            return option;
        }
        else {
            return i;
        }
    }

    public int getCellColumn(int i) {
        if (direction == COLUMN) {
            return option;
        }
        else if (direction == DIAGONAL && option == 1) {
            return 2-i;
        }
        else {
            return i;
        }

    }
}