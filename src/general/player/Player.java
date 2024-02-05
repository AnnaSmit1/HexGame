package general.player;

import general.Board;
import general.Color;

public abstract class Player {

    private String name;

    private Color color;

    /**
     * Creates a player object
     */

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Returns the name of the player
     * @return players name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the color of the player
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Gets the move from the player
     * @param board
     */
    public abstract int[] getMove(Board board, boolean swapAllowed);

    /**
     * Makes the move on the board
     * @param board
     * @param color
     */
    public void makeMove(Board board, Color color) {
        int[] choice = getMove(board, false);
        board.setField(choice[0], choice[1], color);
    }

}
