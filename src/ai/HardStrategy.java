package ai;

import general.Board;
import general.Color;

public class HardStrategy implements Strategy {
    public static final int DIM = 9;

    public boolean doSwap = true;

    /**
     * Returns the name of the strategy
     *
     * @return the name of the strategy
     */
    @Override
    public String getName() {
        return "Hard";
    }

    /**
     * Determines a move on the board, always invokes swaprule if allowed
     *
     * @param board
     * @param color
     * @return a move on the board
     */
    @Override
    public int[] getMove(Board board, Color color, boolean swapAllowed) {
        int[] pair = new int[2];
        if (swapAllowed) {
            pair = board.invokeSwapRule(board, color);
            return pair;
        }
        int min = 0;
        int max = 9;
        while (true) {
            int row = (int) (Math.random() * (max - min)) + min;
            int col = (int) (Math.random() * (max - min)) + min;
            if (board.isEmptyField(row, col)) {
                pair[0] = row;
                pair[1] = col;
                return pair;
            }
        }
    }
}

