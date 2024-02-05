package general.player;

import ai.EasyStrategy;
import ai.HardStrategy;
import ai.Strategy;
import general.Board;
import general.Color;

public class ComputerPlayer extends Player {

    private Strategy strategy;

    /**
     * Constructor of a computer player
     */
    public ComputerPlayer(Color color) {
        this(new EasyStrategy(), color);
    }

    /**
     * Creates a computer player with a strategy that can be chosen.
     *
     * @param strategy
     * @param color
     */
    public ComputerPlayer(Strategy strategy, Color color) {
        super(strategy.getName() + "-" + color, color);
        this.strategy = strategy;
    }

    /**
     * Gets the move of the strategy
     *
     * @param board
     * @return array with the [row, column] pair of the moves
     */
    @Override
    public int[] getMove(Board board, boolean swapAllowed) {
        return strategy.getMove(board, getColor(), swapAllowed);
    }

    /**
     * Gets the current strategy
     *
     * @return the strategy
     */

    public Strategy getStrategy() {
        return strategy;
    }

    /**
     * Sets a new Strategy
     *
     * @param newStrategy
     */
    public void setStrategy(Strategy newStrategy) {
        strategy = newStrategy;

    }
}

