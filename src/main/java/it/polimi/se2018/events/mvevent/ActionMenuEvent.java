package it.polimi.se2018.events.mvevent;

import java.util.List;

/**
 * This Event holds information about the state of a turn for a player
 * @author Framonti
 */

public class ActionMenuEvent extends MVEvent {

    private boolean isDiceMoved;
    private boolean isToolCardUsed;
    private List<String> toolCards;

    /**
     * Constructor
     * @param isDiceMoved States if a player has already placed a dice on his DicePattern
     * @param isToolCardUsed States if a player has already used a ToolCard
     * @param toolCards The ToolCards currently ingame
     */
    public ActionMenuEvent(boolean isDiceMoved, boolean isToolCardUsed, List<String> toolCards){

        super(6);
        this.isDiceMoved = isDiceMoved;
        this.isToolCardUsed = isToolCardUsed;
        this.toolCards = toolCards;
    }

    /**
     * Gets the isDiceMoved attribute
     * @return The isDiceMoved attribute
     */
    public boolean isDiceMoved() {

        return isDiceMoved;
    }

    /**
     * Gets the isToolCardUsed attribute
     * @return The isToolCardUsed attribute
     */
    public boolean isToolCardUsed() {

        return isToolCardUsed;
    }

    /**
     * Gets the ToolCard currently ingame
     * @return The ToolCard currently ingame
     */
    public List<String> getToolCards() {

        return toolCards;
    }
}
