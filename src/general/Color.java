package general;

public enum Color {

    /**
     * The colors of the marbles that are used
     */
    RED, BLUE, EMPTY, CHECKED;

    /**
     * Gets the other color than the parameter
     * @param color
     * @return the other color
     */
    public Color getOther(Color color) {
        if (color == RED) {
            return BLUE;
        } else if (color == BLUE) {
            return RED;
        } else if (color == CHECKED) {
            return EMPTY;
        } else {
            return CHECKED;
        }
    }
}
