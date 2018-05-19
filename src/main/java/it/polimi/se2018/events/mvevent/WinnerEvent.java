package it.polimi.se2018.events.mvevent;

/**
 * This event holds information about the winner
 * @author Daniele Mattioli
 */
public class WinnerEvent {

    private String winner;

    /**
     * Constructor of the class
     * @param winner String representation of the winner
     */
    public WinnerEvent(String winner){
        this.winner = winner;
    }


    /**
     * Gets winner attribute
     * @return winner attribute
     */
    public String getWinner() {
        return winner;
    }
}
