package it.polimi.se2018.events.mvevent;

import java.util.List;

/**
 * This class represents the event generated when the player must choose 1 from 4 windowPatterns
 * @author fabio
 */
public class WindowPatternsEvent extends MVEvent {

    List<String> windowPatterns;

    /**
     * The constructor sets the event id and the list of strings representing the 4 windowPatterns
     * @param windowPatterns It's the list of strings representing the 4 windowPattens
     */
    public WindowPatternsEvent(List<String> windowPatterns) {
        super(-1);
        this.windowPatterns = windowPatterns;
    }

    /**
     * @return The list of strings representing the 4 windowPatterns
     */
    public List<String> getWindowPatterns() {
        return windowPatterns;
    }

}
