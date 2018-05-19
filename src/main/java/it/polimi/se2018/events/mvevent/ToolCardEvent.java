package it.polimi.se2018.events.mvevent;

import java.util.List;

/**
 * This Event holds informations about the ToolCards
 * @author Framonti
 */
public class ToolCardEvent extends MVEvent{

    private List<String> toolCards;

    /**
     * Constructor
     * @param toolCards A List representing all the ToolCard in the game
     */
    public ToolCardEvent(List<String> toolCards){

        super(3);
        this.toolCards = toolCards;
    }

    /**
     * Gets the toolCard attribute
     * @return The toolCard attribute
     */
    public List<String> getToolCards() {

        return toolCards;
    }

}
