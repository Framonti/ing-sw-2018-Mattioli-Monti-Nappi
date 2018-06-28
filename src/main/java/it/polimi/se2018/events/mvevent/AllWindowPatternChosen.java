package it.polimi.se2018.events.mvevent;

/**
 *
 * @author fabio
 */
public class AllWindowPatternChosen extends MVEvent{

    private int turnTimer;

    public AllWindowPatternChosen(int turnTimer){
        super(40);
        this.turnTimer = turnTimer;
    }

    public int getTurnTimer(){
        return turnTimer;
    }
}
