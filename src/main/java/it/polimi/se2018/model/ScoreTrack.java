package it.polimi.se2018.model;

import java.util.List;

/**
 * This class represents the score track
 * @author fabio
 */

public class ScoreTrack {

   private List<Player> players;

    //Constructor
    public ScoreTrack(List<Player> players) {
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
