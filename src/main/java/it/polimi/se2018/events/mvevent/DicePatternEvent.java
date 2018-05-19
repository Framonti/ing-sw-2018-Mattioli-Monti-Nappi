package it.polimi.se2018.events.mvevent;

import java.util.List;

/**
 * This Event holds informations about the DicePattern
 * @author Framonti
 */
public class DicePatternEvent extends MVEvent {

    private List<String> playerNames;
    private List<String> dicePatternsString;

    /**
     * Constructor
     * @param dicePatternsString The String-representation of all the DicePattern
     */
    public DicePatternEvent(List<String> dicePatternsString, List<String> playerNames) {

        super(1);
        this.dicePatternsString = dicePatternsString;
        this.playerNames = playerNames;
    }

    /**
     * Gets the dicePatternString attribute
     * @return The dicePatternString attribute
     */
    public List<String> getDicePatternsString(){

        return this.dicePatternsString;
    }

    /**
     * Gets the players' names
     * @return The playerNames attribute
     */
    public List<String> getPlayerNames() {

        return playerNames;
    }
}
