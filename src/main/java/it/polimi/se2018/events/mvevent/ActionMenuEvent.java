package it.polimi.se2018.events.mvevent;

import java.util.List;

/**
 * This Event holds informations about the actionMenu. Only used in the CLI
 * @author Framonti
 */

public class ActionMenuEvent extends MVEvent {

    private boolean isDiceMoved;
    private boolean isToolCardUsed;
    private List<String> toolCards;

    public ActionMenuEvent(boolean isDiceMoved, boolean isToolCardUsed, List<String> toolCards){

        super(6);
        this.isDiceMoved = isDiceMoved;
        this.isToolCardUsed = isToolCardUsed;
        this.toolCards = toolCards;
    }

    public boolean isDiceMoved() {

        return isDiceMoved;
    }

    public boolean isToolCardUsed() {

        return isToolCardUsed;
    }

    public List<String> getToolCards() {

        return toolCards;
    }
}
