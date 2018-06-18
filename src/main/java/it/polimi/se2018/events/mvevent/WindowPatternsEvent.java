package it.polimi.se2018.events.mvevent;

import java.util.List;

/**
 * This class represents the event generated when the player must choose 1 from 4 windowPatterns
 * @author fabio
 */
public class WindowPatternsEvent extends MVEvent {

    private List<String> windowPatternsCLI;
    private List<String> windowPatternsGUI;
    private String privateObjectiveCard;
    private String privateObjectiveCardPath;

    /**
     * The constructor sets the event id and the list of strings representing the 4 windowPatterns
     * @param windowPatternsCLI It's the list of strings representing the 4 windowPattens
     */
    public WindowPatternsEvent(List<String> windowPatternsCLI, List<String> windowPatternsGUI, String privateObjectiveCard, String privateObjectiveCardPath) {
        super(-1);
        this.windowPatternsCLI = windowPatternsCLI;
        this.windowPatternsGUI = windowPatternsGUI;
        this.privateObjectiveCard = privateObjectiveCard;
        this.privateObjectiveCardPath = privateObjectiveCardPath;
    }

    /**
     * @return The list of strings representing the 4 windowPatterns
     */
    public List<String> getWindowPatterns() {
        return windowPatternsCLI;
    }

    /**
     * @return A List of String representing 4 path that the GUI will load
     */
    public List<String> getWindowPatternsGUI() {
        return windowPatternsGUI;
    }

    /**
     * @return A String representing a PrivateObjectiveCard
     */
    public String getPrivateObjectiveCard() {
        return privateObjectiveCard;
    }

    /**
     * @return A String representing a path that the GUI will load
     */
    public String getPrivateObjectiveCardPath() {
        return privateObjectiveCardPath;
    }
}
