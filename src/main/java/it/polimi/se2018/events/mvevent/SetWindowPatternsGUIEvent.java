package it.polimi.se2018.events.mvevent;

import java.util.List;

/**
 * This event is used for setting the windowPattern and their favor token to the players in the GUI
 */
public class SetWindowPatternsGUIEvent extends MVEvent {

    private List<String> windowPatternsGUI;
    private List<String> favorTokensNumber;

    public SetWindowPatternsGUIEvent(List<String> windowPatternsGUI, List<String> favorTokensNumber){
        super(15);
        this.windowPatternsGUI = windowPatternsGUI;
        this.favorTokensNumber = favorTokensNumber;
    }

    /**
     * Gets a List of String representing path for loading windowPatters
     * @return A List of String representing path for loading windowPatters
     */
    public List<String> getWindowPatternsGUI() {
        return windowPatternsGUI;
    }

    /**
     * Gets a List of String representing the favor tokens associated to the windowPatterns
     * @return A List of String representing the favor tokens associated to the windowPatterns
     */
    public List<String> getFavorTokensNumber() {
        return favorTokensNumber;
    }
}
