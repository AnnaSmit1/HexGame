package general.player;

import general.Board;
import general.Color;

import java.io.InputStreamReader;
import java.util.Scanner;

public class HumanPlayer extends Player {

    Scanner scanner = new Scanner(new InputStreamReader(System.in));

    /**
     * Creates a player object
     *
     * @param name
     * @param color
     */
    public HumanPlayer(String name, Color color) {
        super(name, color);
    }

    /**
     * Gets a valid move from the player
     *
     * @param board
     * @return array: [row, column] pair that indicates the move of the player
     */
    @Override
    public int[] getMove(Board board, boolean swapAllowed) {
        int[] pair = new int[2];
        if (swapAllowed == true) {
            String question = "► " + getColor().toString() + ": " + getName() + " it's your turn!\nDo you want to invoke the swap rule? [y/n]";
            System.out.println(question);
            String answer = scanner.nextLine();
            if (!answer.equals("y") && !answer.equals("n")) { //If answer isn't up to standards poke them till they answer correctly
                while (!answer.equals("y") && !answer.equals("n")) {
                    question = "► " + getColor().toString() + ": " + getName() + " it's your turn!\nDo you want to invoke the swap rule? [y/n]";
                    System.out.println(question);
                    answer = scanner.nextLine();
                }
            }
            if (answer.equals("y")) {
                pair = board.invokeSwapRule(board, getColor());
                return pair;
            }
        }
        String prompt = "► " + getColor().toString() + ": " + getName() + " it's your turn! Please provide the index of the field you would like to mark. ";
        System.out.println(prompt);
        int choice = scanner.nextInt();
        pair = board.getRowCol(choice);
        boolean validField = board.isValidField(pair[0], pair[1]);
        if (validField) {
            if (board.isEmptyField(pair[0], pair[1])) {
                return pair;
            } else {
                validField = false;
            }
        }
        while (!validField) {
            System.out.println("► ERROR: field " + choice + " is not a valid choice.");
            System.out.println(prompt);
            choice = scanner.nextInt();
            pair = board.getRowCol(choice);
            validField = board.isValidField(pair[0], pair[1]);
            if (validField) {
                if (board.isEmptyField(pair[0], pair[1])) {
                    return pair;
                } else {
                    validField = false;
                }
            }
        }
        return pair;
    }

}

