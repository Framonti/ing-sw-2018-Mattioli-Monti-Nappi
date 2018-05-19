package it.polimi.se2018.events.mvevent;

import java.util.List;

/**
 * This Event holds informations about the DicePattern
 * @author Framonti
 */
public class DicePatternEvent extends MVEvent {

    private List<String> dicePatternsString;

    /**
     * Constructor
     * @param dicePatternsString The String-representation of all the DicePattern
     */
    public DicePatternEvent(List<String> dicePatternsString) {

        super(1);
        this.dicePatternsString = dicePatternsString;
    }

    /**
     * Gets the dicePatternString attribute
     * @return The dicePatternString attribute
     */
    public List<String> getDicePatternsString(){

        return this.dicePatternsString;
    }
}
