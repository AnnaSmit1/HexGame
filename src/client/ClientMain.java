package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientMain {

    private static Client client;
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("What is the address of the server: ");
        try {
            String address = reader.readLine();
            System.out.println("Give up the port address: ");
            int port = Integer.parseInt(reader.readLine());
            System.out.println("What do you want as your username: ");
            String username = reader.readLine();
            client = new Client(username);
            try {
                client.connect(InetAddress.getByName(address), port);
            } catch (UnknownHostException e) {
                System.out.println("ERROR: Wrong address, port or username");
            }
            client.hello("Anna's Client");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
