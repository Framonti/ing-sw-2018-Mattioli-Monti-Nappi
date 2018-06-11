package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the score track
 * @author fabio
 */


//TODO eliminare

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
//TODO ???? forse non serve
    public List<Integer> getPlayersScores(){

        List<Integer> playersScores = new ArrayList<>();
        for(Player player : players){
            playersScores.add(player.getScore());
        }
        return playersScores;
    }
}
