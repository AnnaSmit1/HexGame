package client;

import ai.EasyStrategy;
import ai.HardStrategy;
import ai.Strategy;
import execution.Game;
import general.Board;
import general.Color;
import general.player.ComputerPlayer;
import general.player.HumanPlayer;
import general.player.Player;
import general.player.ServerPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ClientListener {

    private Client client;

    private InputStreamReader reader;

    protected Thread displayMenu;

    private OutputStreamWriter writer;

    private Strategy strategy;

    protected Game game;

    protected Player[] players = new Player[2];

    public Board board;

    private boolean isComputerPlayer = false;

    private int[] pairReceived;

    private boolean strategyIsEasy;

    /**
     * Creates a clietnlistener for a client
     *
     * @param client
     */
    public ClientListener(Client client) {
        this.client = client;
        try {
            reader = new InputStreamReader(client.socket.getInputStream());
            writer = new OutputStreamWriter(client.socket.getOutputStream());
            pairReceived = new int[2];
        } catch (IOException e) {
            System.out.println("ERROR: Unable to connect");
        }
    }

    /**
     * When server sends Hello back sends Login to server
     *
     * @param name of the server
     */
    public void receivedHello(String name) {
        try {
            writer.write("LOGIN~" + client.username + "\n");
            writer.flush();
        } catch (IOException e) {
            System.out.println("ERROR: couldn't send LOGIN to the server");
        }

    }

    /**
     * When server sends 'ALREADYLOGGEDIN' it prints a message
     * and continues to the method that handles when the user is logged in
     */
    public void receivedAlreadyLoggedIn() {
        System.out.println("► The user is already logged in");
        receivedLogin();
    }

    /**
     * When server sends LOGIN back, meaning it was succesful, prints menu to the client
     */
    public void receivedLogin() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("► Do you want to play as yourself or as a Bot? [me/bot] ");
        String line = null;
        try {
            line = reader.readLine().toLowerCase();
        } catch (IOException e) {
            System.out.println("ERROR: Couldn't read the answer correctly");
        }
        if (!line.equals("me")) {
            isComputerPlayer = true;
            String strategyAnswer;
            System.out.println("► What difficulty do you want the AI to be? [easy/hard] ");
            try {
                strategyAnswer = reader.readLine().toLowerCase();
                if (strategyAnswer.equals("easy")) {
                    strategyIsEasy = true;
//                    strategy = new EasyStrategy();
                } else if (strategyAnswer.equals("hard")) {
                    strategyIsEasy = false;
                    strategy = new HardStrategy();
                } else {
                    System.err.println("ERROR: Unknown input!");
                    receivedLogin();
                }
            } catch (IOException e) {
                System.out.println("ERROR: Couldn't read the answer correctly");
            }
        } else {
            isComputerPlayer = false;
        }

        displayMenu = new
                Thread(() ->
        {
            System.out.println("► Menu of commands: list, queue, list, move, help");
            System.out.println("► list - display a list of users logged in");
            System.out.println("► queue - enter the queue to play the game");
            System.out.println("► queue queue - leave the queue to not play the game");
            System.out.println("► move - enter the move on the board when in game");
            System.out.println("► help - get the help menu");
            while (client.ongoingGame) {
                String answer = null;
                try {
                    if (!reader.ready()) {
                        continue;
                    }
                    answer = reader.readLine().toLowerCase();
                } catch (IOException e) {
                    System.out.println("ERROR");
                }
                switch (answer) {
                    case "list":
                        client.list();
                        break;
                    case "queue":
                        if (client.isInQueue == true) {
                            client.queue();
                            System.out.println("► Removed from queue");
                            client.isInQueue = false;
                        } else {
                            client.queue();
                            client.isInQueue = true;
                            System.out.println("► Joined the queue!");
                        }
                        break;
                    case "help":
                        System.out.println("► Menu of commands: list, queue, list, move, help");
                        System.out.println("► list - display a list of users logged in");
                        System.out.println("► queue - enter the queue to play the game");
                        System.out.println("► queue - entered again after first queue command to leave the queue to not play the game");
                        System.out.println("► move - enter the move on the board when in game");
                        System.out.println("► help - get THIS menu");
                        break;
                    case "queue queue":
                        client.isInQueue = false;
                        break;
                    default:
                        System.out.println("ERROR: Unknown command");
                        break;
                }
            }
        });
        displayMenu.start();
    }


    /**
     * Used when server sends 'LIST' command with a list of users
     *
     * @param list
     */
    public void receivedList(String[] list) {
        for (String clientname : list) {
            System.out.println("► " + clientname);
        }
    }

    /**
     * Used when server sends a 'NEWGAME' command, with the names of the
     * first and secondplayer
     *
     * @param firstPlayer
     * @param secondPlayer
     */
    public void receivedNewGame(String firstPlayer, String secondPlayer) {
        System.out.println("► A new game is starting...");
        if (client.isInQueue) {
            if (firstPlayer.equals(client.username)) { //TODO NOT SURE ABOUT THIS
                if (!isComputerPlayer) {
                    players[0] = new HumanPlayer(firstPlayer, Color.RED);
                } else {
                    if (strategyIsEasy) {
                        players[0] = new ComputerPlayer(Color.RED);
                    } else {
                        players[0] = new ComputerPlayer(strategy, Color.RED);
                    }
                }
                players[1] = new ServerPlayer(secondPlayer, Color.BLUE);
            } else {
                players[0] = new ServerPlayer(firstPlayer, Color.RED);
                if (!isComputerPlayer) {
                    players[1] = new HumanPlayer(firstPlayer, Color.BLUE);
                } else {
                    if (strategyIsEasy) {
                        players[1] = new ComputerPlayer(Color.BLUE);
                    } else {
                        players[1] = new ComputerPlayer(strategy, Color.BLUE);
                    }
                }
            }
            board = new Board();
            game = new Game(players[0], players[1], board);
            client.isInQueue = false;
            game.turnofplayer = 2;
        } else {
            System.out.println("You should first get in queue!");
        }
    }

    /**
     * Used when server sends the 'MOVE' command, and places it on the board
     *
     * @param index
     */
    public void receivedMove(int index) {
        game.turnFromServer(index, board, game.getCurrentPlayer().getColor());
        game.turnofplayer++;
        System.out.println(board.toString());
        if (!(game.getCurrentPlayer() instanceof ServerPlayer)) {
            if (game.turnofplayer == 3) {
                client.move(81);
            } else {
                pairReceived = players[game.turnofplayer % 2].getMove(board, false);
                client.move(board.index(pairReceived[0], pairReceived[1]));
            }
        }
    }

    /**
     * Used when server sends 'GAMEOVER' command and shows the winners
     * and asks for a newgame
     *
     * @param description
     * @param winner
     * @throws IOException
     */
    public void receivedGameOver(String description, String winner) throws IOException {
        client.ongoingGame = false;
        displayMenu.interrupt();
        System.out.println("The game ended because of a " + description);
        if (winner != null) {
            System.out.println("Player " + winner + " has won!");
        }
        System.out.println("Do you want to play again? [y/n]\n");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            System.out.println("ERROR: Couldn't read line");
            System.out.println(e.getMessage());
        }
        client.ongoingGame = true;
        if (line.equals("y")) {
            new Thread(this::receivedLogin).start();
        } else {
            client.close();
        }

    }
}
