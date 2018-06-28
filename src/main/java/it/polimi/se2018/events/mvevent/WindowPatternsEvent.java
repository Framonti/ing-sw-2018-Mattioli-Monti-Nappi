package it.polimi.se2018.events.mvevent;

import java.util.List;

/**
 * This class represents the event generated when the player must choose 1 from 4 windowPatterns
 * @author fabio
 */
public class WindowPatternsEvent extends MVEvent {

    private List<String> windowPatternsCLI;
    private List<String> windowPatternsGUI;
    private List<String> privateObjectiveCards;
    private List<String> privateObjectiveCardsPath;

    /**
     * The constructor sets the event id and the list of strings representing the 4 windowPatterns
     * @param windowPatternsCLI It's the list of strings representing the 4 windowPattens
     * @param privateObjectiveCards A String representation of one or two (singleplayer) PrivateObjectiveCard for the CLI
     * @param privateObjectiveCardsPath A String representation of one or two (singleplayer) PrivateObjectiveCard for the GUI
     * @param windowPatternsGUI A List of string representing path for the GUI
     */
    public WindowPatternsEvent(List<String> windowPatternsCLI, List<String> windowPatternsGUI, List<String> privateObjectiveCards, List<String> privateObjectiveCardsPath) {
        super(-1);
        this.windowPatternsCLI = windowPatternsCLI;
        this.windowPatternsGUI = windowPatternsGUI;
        this.privateObjectiveCards = privateObjectiveCards;
        this.privateObjectiveCardsPath = privateObjectiveCardsPath;
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
    public List<String> getPrivateObjectiveCards() {
        return privateObjectiveCards;
    }

    /**
     * @return A String representing a path that the GUI will load
     */
    public List<String> getPrivateObjectiveCardsPath() {
        return privateObjectiveCardsPath;
    }
}
