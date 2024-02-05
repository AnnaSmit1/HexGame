package general.player;

import general.Board;
import general.Color;

public class ServerPlayer extends Player{


    /**
     * Creates a player object
     *
     * @param name
     * @param color
     */
    public ServerPlayer(String name, Color color) {
        super(name, color);
    }

    /**
     * Just here to have a serverplayer class properly working
     * @param board
     * @param swapAllowed
     * @return
     */
    @Override
    public int[] getMove(Board board, boolean swapAllowed) {
        return null;
    }
}
