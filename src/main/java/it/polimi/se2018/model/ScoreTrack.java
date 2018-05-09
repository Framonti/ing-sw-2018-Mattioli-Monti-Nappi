package it.polimi.se2018.model;

import it.polimi.se2018.model.Player;

import java.util.ArrayList;

public class ScoreTrack {

   private ArrayList<Player> players;

    //Constructor
    public ScoreTrack(ArrayList<Player> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        String tmp = "";
        for (Player player : players)
            tmp = tmp.concat(player.getName() + ": " + player.getScore() + "\n");
        return tmp;
    }
}
