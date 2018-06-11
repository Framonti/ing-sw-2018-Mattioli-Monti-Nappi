package it.polimi.se2018.model;

//TODO eliminare
/**
 * This class represents the score marker of each player. This will be used only in the gui.
 * @author fabio
 */

public class ScoreMarker {

    //attributes
    private Player currentPlayer;

    //constructor
    public ScoreMarker(Player player) {
        currentPlayer = player;
    }

    //getter
    public Player getCurrentPlayer() { return currentPlayer; }
}
