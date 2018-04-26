package it.polimi.se2018;

import java.util.ArrayList;

public class ScoreTrack {

    private ArrayList<Player> players;

    //Constructor
    public ScoreTrack(ArrayList<Player> players) {
        this.players = players;
    }

    //prints the score of each player
    public void showScoreTrack() {
        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getScore());
        }
    }

}
