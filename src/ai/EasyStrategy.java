package ai;

import general.Board;
import general.Color;

public class EasyStrategy implements Strategy {
    /**
     * Returns the name of the strategy
     *
     * @return name of the strategy
     */
    @Override
    public String getName() {
        return "Easy";
    }

    /**
     * Determines a move on the board, never allows swap rule
     *
     * @param board
     * @param color
     * @return a move on the board
     */
    @Override
    public int[] getMove(Board board, Color color, boolean swapAllowed) {
        int[] pair = new int[2];
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
