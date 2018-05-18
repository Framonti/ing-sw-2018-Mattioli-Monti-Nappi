package it.polimi.se2018.events.mvevent;

/**
 * This Event holds informations about the DicePattern
 * @author Framonti
 */
public class DicePatternEvent extends MVEvent {

    private String dicePatternString;

    /**
     * Constructor
     * @param dicePatternString The String-representation of a DicePattern
     */
    public DicePatternEvent(String dicePatternString) {

        super(1);
        this.dicePatternString = dicePatternString;
    }

    /**
     * Gets the dicePatternString attribute
     * @return The dicePatternString attribute
     */
    public String  getDicePatternString(){

        return this.dicePatternString;
    }
}
