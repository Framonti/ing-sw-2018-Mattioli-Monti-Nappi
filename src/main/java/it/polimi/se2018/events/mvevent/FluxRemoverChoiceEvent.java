package it.polimi.se2018.events.mvevent;

/**
 * This event holds information about the FluxRemoveChoice effect
 * @author Daniele Mattioli
 */
public class FluxRemoverChoiceEvent extends MVEvent {

    private String dice;

    /**
     * Constructor
     * @param dice A representation of the dice that has changed
     */
    public FluxRemoverChoiceEvent(String dice){

        super(14);
        this.dice = dice;
    }

    /**
     * Gets the dice
     * @return A representation of a dice
     */
    public String getDice() {
        return dice;
    }
}
