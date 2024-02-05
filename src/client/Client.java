package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements Runnable {

    protected Socket socket;

    protected boolean ongoingGame = true;

    private InputStreamReader reader;

    private ClientListener clientListener;

    private OutputStreamWriter writer;

    private Thread thread;

    protected String username;

    public boolean isInQueue;

    /**
     * Creates a client, based on the given username
     * @param username
     */
    public Client(String username) {
        this.username = username;
    }

    /**
     * Method to connect the client to the server
     * @param address
     * @param port
     * @throws IOException
     */
    public void connect(InetAddress address, int port) throws IOException {
        try {
            socket = new Socket(address, port);
            clientListener = new ClientListener(this);
            thread = new Thread(this);
            thread.start();
            try {
                reader = new InputStreamReader(socket.getInputStream());
                writer = new OutputStreamWriter(socket.getOutputStream());
            } catch (IOException e) {
                System.out.println("ERROR: Cannot establish connection");
                close();
            }
        } catch (IOException e) {
            System.out.println("ERROR: Cannot establish connection");
        }
    }

    /**
     * Closes the connection to the server
     */
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Unable to close socket");
        }
    }

    /**
     * Sends the command 'LIST' to the server
     */
    public void list() {
        try {
            writer.write("LIST" + "\n");
            writer.flush();
        } catch (IOException e) {
            System.out.println("ERROR: Cannot give command LIST");
        }

    }

    /**
     * Sends the command 'HELLO' to the server with a name
     * @param name
     */
    public void hello(String name) {
        try {
            writer.write("HELLO~" + name + "\n");
            writer.flush();
        } catch (IOException e) {
            System.out.println("ERROR: Cannot give command HELLO");
        }

    }

    /**
     * Sends the command 'QUEUE' to the server
     */
    public void queue() {
        try {
            writer.write("QUEUE" + "\n");
            writer.flush();
        } catch (IOException e) {
            System.out.println("ERROR: Cannot give command QUEUE");
        }

    }

    /**
     * Sends the command 'move' to the server with the field corresponding to the move
     */
    int temp;
    public void move(int index) {
        try {
            writer.write("MOVE~" + index + "\n");
            writer.flush();
        } catch (IOException e) {
            System.out.println("ERROR: Cannot give command MOVE");
        }

    }

    /**
     * Sends an 'ERROR' command to the server with a description
     * @param description
     */
    public void error(int description) {
        try {
            writer.write("ERROR~" + description + "\n");
            writer.flush();
        } catch (IOException e) {
            System.out.println("ERROR: Cannot give command ERROR (the irony lol)");
        }
    }

    /**
     * Verifies what the server sends while running the client thread
     * and responds
     */
    @Override
    public void run() {
        BufferedReader bufferedReader = new BufferedReader(reader);
        while (true) {
            try {
                String command = bufferedReader.readLine();
                if (command == null) {
                    close();
                    return;
                }
                String[] splitLine = command.split("~");
                switch (splitLine[0]) {
                    case "HELLO":
                        String servername = splitLine[1];
                        clientListener.receivedHello(servername);
                        break;
                    case "LOGIN":
                        clientListener.receivedLogin();
                        break;
                    case "LIST":
                        String[] list = new String[splitLine.length - 1];
                        int j = 0;
                        for (int i = 1; i < splitLine.length; i++) {
                            list[j] = splitLine[i];
                            j++;
                        }
                        clientListener.receivedList(list);
                        System.out.println("\n");
                        break;
                    case "ALREADYLOGGEDIN":
                        clientListener.receivedAlreadyLoggedIn();
                        break;
                    case "MOVE":
                        int index = Integer.parseInt(splitLine[1]);
                        clientListener.receivedMove(index);
                        break;
                    case "ERROR":
                        break;
                    case "NEWGAME":
                        clientListener.displayMenu.interrupt();
                        ongoingGame = true;
                        String p1 = splitLine[1];
                        String p2 = splitLine[2];
                        clientListener.receivedNewGame(p1, p2);
                        if (p1.equals(username)) {
                            System.out.println("► Starting a new game...\n");
                            int[] pair = clientListener.players[0].getMove(clientListener.board, false);
                            move(clientListener.board.index(pair[0], pair[1]));
                        } else {
                            System.out.println("► Starting a new game, wait for your turn");
                        }
                        break;
                    case "GAMEOVER":
                        String description = splitLine[1];
                        String winner = null;
                        if (splitLine.length > 2) {
                            winner = splitLine[2];
                        }
                        clientListener.receivedGameOver(description, winner);
                        break;
                }
            } catch (IOException e) {
                System.out.println("ERROR: something went wrong");
                close();
                return;
            }


        }

    }
}
