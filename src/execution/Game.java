package execution;

import general.Board;
import general.Color;
import general.player.Player;

import java.io.InputStreamReader;
import java.util.Scanner;

public class Game {

    Scanner scanner = new Scanner(new InputStreamReader(System.in));
    public static final int NUMBER_PLAYERS = 2;

    public Board board;

    public Player[] players;

    private int turnPlayer;

    public boolean canDoSwap = true;

    private int playernumber;

    public int turnofplayer = 2;


    /**
     * Creates the game
     * @param p0
     * @param p1
     * @param board
     */
    public Game(Player p0, Player p1, Board board) {
        this.board = board;
        players = new Player[NUMBER_PLAYERS];
        players[0] = p0;
        players[1] = p1;
        turnofplayer = 2;
    }

    /**
     * Resets the game
     */
    private void reset() {
        board.reset();
        turnofplayer = 2;
        canDoSwap = true;
    }

    /**
     * Displays the current board
     */
    private void currentBoardDisplay() {
        System.out.println("\n► The current board: \n\n" + board.toString() + "\n\n");
    }

    /**
     * Gets the move from the player and puts it on the board
     * @param swapAllowed
     */
    private void turn(boolean swapAllowed) {
        int[] pair = players[playernumber].getMove(board, swapAllowed);
        board.setField(pair[0], pair[1], players[playernumber].getColor());
    }

    /**
     * Gets the move from the server and puts it on the board
     * @param index
     * @param board
     * @param color
     */
    public void turnFromServer(int index, Board board, Color color) {
        int[] pair;
        if (index == 81) {
            pair = board.invokeSwapRule(board, color);
        } else {
            pair = board.getRowCol(index);
        }
        board.setField(pair[0], pair[1], color);
    }

    /**
     * Gets the current player
     * @return
     */
    public Player getCurrentPlayer() {
        if (turnofplayer % 2 == 0) {
            return players[0];
        } else {
            return players[1];
        }
    }

    /**
     * Plays the game
     */

    private void play() {
            playernumber = 0;
        while (!board.hasWinner()) {
            currentBoardDisplay();
            if (players[playernumber] == players[0]) {
                turn(false);
                playernumber++;
            }
            else if (canDoSwap) {
                turn(canDoSwap);
                canDoSwap = false;
                playernumber = 0;
            } else {
                turn(false);
                playernumber = 0;

                }
            }
        }

    /**
     * Starts the game
     */
    public void start() {
        boolean ongoingGame = true;
        while (ongoingGame) {
            reset();
            play();
            printResult();
            System.out.println("\n► Do you want to play again? [y/n]? ");
            String line = scanner.nextLine();
            if (line.equals("n")) {
                ongoingGame = false;
            }


        }
        System.out.println(board.toString());
    }

    private void printResult() {
        Player winner;
        if (board.isGameOver(players[0].getColor())) {
            winner = players[0];
        } else {
            winner = players[1];
        }
        System.out.println(board.toString());
        System.out.println("Player " + winner.getName() + " with color " + winner.getColor().toString() + " has won! Congrats!");
    }



}
