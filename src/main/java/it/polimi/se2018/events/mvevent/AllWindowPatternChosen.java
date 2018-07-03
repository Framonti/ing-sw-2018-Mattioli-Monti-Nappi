package it.polimi.se2018.events.mvevent;

/**
 * This event is sent when all the players have chosen their WindowPattern
 * @author fabio
 */
public class AllWindowPatternChosen extends MVEvent{

    private int turnTimer;

    /**
     * @param turnTimer The duration of a turn
     */
    public AllWindowPatternChosen(int turnTimer){
        super(40);
        this.turnTimer = turnTimer;
    }

    /**
     * Gets the turnTimer
     * @return The turnTimer
     */
    public int getTurnTimer(){
        return turnTimer;
    }
}
