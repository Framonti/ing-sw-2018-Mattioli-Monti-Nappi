package it.polimi.se2018.events.mvevent;

import java.util.List;

/**
 * This Event holds informations about the DicePattern
 * @author Framonti
 */
public class DicePatternEvent extends MVEvent {

    private List<String> playerNames;
    private List<String> dicePatternsString;
    private List<List<String>> dicePatternsGUI;

    /**
     * Constructor
     * @param dicePatternsString The String-representation of all the DicePattern
     */
    public DicePatternEvent(List<String> dicePatternsString, List<String> playerNames, List<List<String>> dicePatternsGUI) {

        super(1);
        this.dicePatternsString = dicePatternsString;
        this.playerNames = playerNames;
        this.dicePatternsGUI = dicePatternsGUI;
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

    public List<List<String>> getDicePatternsGUI() {
        return dicePatternsGUI;
    }

}
