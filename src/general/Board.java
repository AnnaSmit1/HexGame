package general;

import static java.lang.String.valueOf;

public class Board {

    public static final int DIM = 9;

    public Color[][] fields;

    private int[] x_axes = {-1, 1, 0, 0, -1, 1};
    private int[] y_axes = {0, 0, 1, -1, 1, -1};

    private Board oneColorBoard;

    public static final String ANSI_RESET = "\u001B[0m";

    public static final String ANSI_RED = "\u001B[31m";

    public static final String ANSI_BLUE = "\u001B[34m";

    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_YELLOW = "\u001B[33m";


    /**
     * Creates an empty board
     */
    public Board() {
        fields = new Color[DIM][DIM];
        for (int i = 0; i < (DIM); i++) {
            for (int j = 0; j < (DIM); j++) {
                fields[i][j] = Color.EMPTY;
            }
        }
    }

    /**
     * Makes a copy of the board
     *
     * @param board
     * @return copy of the board
     */
    public Board deepCopy(Board board) {
        Board newBoard = new Board();
        for (int i = 0; i < (DIM); i++) {
            for (int j = 0; i < (DIM); j++) {
                newBoard.fields[i][j] = board.fields[i][j];
            }
        }
        return newBoard;
    }

    /**
     * Makes a copy of the board of only a single color (So Red, Blue or Empty)
     *
     * @param
     * @param color
     * @return copy of the board of only a single color
     */
    public Board singleColorCopy(Color color) {
        Board newBoard = new Board();
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if (this.fields[i][j] == color) {
                    newBoard.fields[i][j] = this.fields[i][j];
                } else {
                    newBoard.fields[i][j] = Color.EMPTY;
                }
            }
        }
        return newBoard;

    }

    /**
     * Converts two dimensional array (So field[row][column]) to
     * one dimensional array field[field 1]...
     *
     * @param fields
     * @return one dimensional array with colors of fields
     */
    public static int[] fieldsConversion(int[][] fields) {
        int[] result = new int[DIM * DIM];
        int index = 0;

        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                result[index] = fields[i][j];
                index++;
            }
        }
        return result;


    }

    /**
     * Calculates the index in the linear array of the fields from a row column pair
     *
     * @param row
     * @param col
     * @return index belonging to row column pair
     */
    public int index(int row, int col) {
        return row * DIM + col;
    }

    /**
     * Returns the [row, column] pair belonging to an index of the board
     *
     * @param index
     * @return [row, column] pair
     */
    public int[] getRowCol(int index) {
        int[] result = new int[2];
        result[0] = index / DIM; //row
        result[1] = index % DIM; //column
        return result;
    }

    /**
     * Checks if a field is valid
     *
     * @param row
     * @param column
     * @return true if the field is a valid field on the board
     */
    public boolean isValidField(int row, int column) {
        return index(row, column) >= 0 && index(row, column) < 81;
    }

    /**
     * Gets the color of the row, column pair belonging to the field
     *
     * @param row
     * @param column
     * @return the color of the field
     */
    public Color getField(int row, int column) {
        return fields[row][column];
    }

    public void setField(int row, int column, Color color) {
        fields[row][column] = color;
    }


    /**
     * Returns true if the field belonging to the row, column pair is empty
     *
     * @param row
     * @param column
     * @return true if the field is empty
     */
    public boolean isEmptyField(int row, int column) {
        return fields[row][column] == Color.EMPTY;
    }

    /**
     * Returns true if the field belonging to an index in the board is empty
     *
     * @param newIndex
     * @return true if the field is empty
     */
    public boolean isEmptyFieldIndex(int newIndex) {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if (index(i, j) == newIndex && fields[i][j] == Color.EMPTY) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Resets the board, clears the field
     */
    public void reset() {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                fields[i][j] = Color.EMPTY;
            }
        }
    }

    /**
     * This is used if the swaprule is invoked. It passes on the row/column of the marble mirrored in the diagonal.
     * Then it deletes the found mark
     *
     * @param board
     * @param color
     * @return array with [column, row] (previously [row, column]) belonging to the marble mirrored in the diagonal
     */
    public int[] invokeSwapRule(Board board, Color color) {
        int[] pair = new int[2];
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if (board.fields[i][j] == color.getOther(color)) {
                    pair[0] = j; //we assign column to row
                    pair[1] = i; //we assign row to column
                    board.fields[i][j] = Color.EMPTY; //delete the other mark
                    return pair;
                }
            }
        }
        return null;
    }

    /**
     * Used on single color copy board. Recursive algorithm that starts at the top, uses DFS to color "CHECKED" every field encountered.
     *
     * @param row
     * @param col
     * @param color
     */
    public void Fill(int row, int col, Color color) {
        if (row >= 0 && row < DIM && col >= 0 && col < DIM && oneColorBoard.fields[row][col].equals(color)) {
            oneColorBoard.fields[row][col] = Color.CHECKED;
            for (int k = 0; k < 6; k++) {
                Fill(row + x_axes[k], col + y_axes[k], color);
            }
        }
    }

    /**
     * Checks if the last row of the board contains a field with color "CHECKED" if so, there is a winner
     *
     * @param color
     * @return true if there is a winner
     */
    public boolean isGameOver(Color color) {
        oneColorBoard = new Board();
        if (color == Color.RED) {
            for (int j = 0; j < 9; j++) {
                this.oneColorBoard = singleColorCopy(color);
                Fill(0, j, color);
                for (int m = 0; m < 9; m++) {
                    if (oneColorBoard.fields[8][m] == Color.CHECKED) {
                        return true;
                    }
                }
            }
        } else if (color.equals(Color.BLUE)) {
            for (int j = 0; j < 9; j++) {
                oneColorBoard = singleColorCopy(color);
                if (oneColorBoard.fields[j][0].equals(color)) {
                    Fill(j, 0, color);
//                    System.out.println(oneColorBoard);
                    for (int m = 0; m < 9; m++) {
                        if (oneColorBoard.fields[m][8].equals(Color.CHECKED)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Boolean that returns true if there is a winner
     *
     * @return true if there is a winner
     */
    public boolean hasWinner() {
        if (this.isGameOver(Color.BLUE) || this.isGameOver((Color.RED))) {
            return true;
        }
        return false;
    }
    public final String displayColor(int i, int j) {
        if (this.fields[i][j] == Color.RED) {
            return ANSI_RED;
        }
        if (this.fields[i][j] == Color.BLUE) {
            return ANSI_BLUE;
        } else if (this.fields[i][j] == Color.CHECKED) {
            return ANSI_YELLOW;
        } else {
            return ANSI_WHITE;
        }
    }


    /**
     * Displays the board in string form
     *
     * @return String representation of the board
     */
    public String toString() {
        String s = "";
        String indent = " ";
        String topBorder = ANSI_RED + "---------------------------------------------------------------\n" + ANSI_RESET;
        s += topBorder;
        for (int i = 0; i < DIM; i++) {
            String row = indent;
            row += ANSI_BLUE + "\\ " + ANSI_RESET;
            for (int j = 0; j < DIM; j++) {
                if (index(i, j) < 10) {
                    row = row + displayColor(i, j) + "0" + (index(i, j)) + "  " + ANSI_RESET;
                } else {
                    row = row + displayColor(i, j) + (index(i, j)) + "  " + ANSI_RESET;
                }
                if (j < DIM - 1) {
                    row = row + "|  ";
                }
            }
            s = s + row + ANSI_BLUE + "\\" + ANSI_RESET + "\n";
            indent = " " + indent;
        }
        s = s + indent + topBorder;
        return s;
    }
}













