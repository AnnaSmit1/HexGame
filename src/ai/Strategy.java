package ai;

import general.Board;
import general.Color;

public interface Strategy {

    /**
     * Returns the name of the strategy
     * @return String of the name of the strategy
     */
    public String getName();

    /**
     * Returns the row, column pair of the move
     * @param board
     * @param color
     * @param swapAllowed
     * @return array with [row, column] pair of the move
     */
    public int[] getMove(Board board, Color color, boolean swapAllowed);


}
