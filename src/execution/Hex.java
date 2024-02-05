package execution;

import ai.HardStrategy;
import ai.Strategy;
import general.Board;
import general.Color;
import general.player.ComputerPlayer;
import general.player.HumanPlayer;
import general.player.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Hex {

    public static void main(String[] args) {
        String p0 = "", p1 = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("► Welcome! What are your names? EG:\nYelena\nKate\n=====OR=====\nYelena\n[-easy/-hard]\n");
        try {
            p0 = reader.readLine();
            p1 = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("► Something went wrong with name reading! UH OH :(");
        }
        Player player1 = new HumanPlayer(p0, Color.RED);
        if (p1.equals("-easy")) {
            ComputerPlayer computerPlayer = new ComputerPlayer(Color.BLUE);
            Game game = new Game(player1, computerPlayer, new Board());
            game.start();
        }
        if (p1.equals("-hard")) {
            Strategy hardStrategy = new HardStrategy();
            ComputerPlayer computerPlayer = new ComputerPlayer(hardStrategy, Color.BLUE);
            Game game = new Game(player1, computerPlayer, new Board());
            game.start();
        } else {
            Player player2 = new HumanPlayer(p1, Color.BLUE);
            Game game = new Game(player1, player2, new Board());
            game.start();
        }

    }
}
