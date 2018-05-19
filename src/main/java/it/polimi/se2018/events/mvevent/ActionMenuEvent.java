package it.polimi.se2018.events.mvevent;

/**
 * This Event holds informations about the actionMenu. Only used in the CLI
 * @author Framonti
 */
public class ActionMenuEvent extends MVEvent {

    private boolean isDiceMoved;
    private boolean isToolCardUsed;

    public ActionMenuEvent(boolean isDiceMoved, boolean isToolCardUsed){

        super(6);
        this.isDiceMoved = isDiceMoved;
        this.isToolCardUsed = isToolCardUsed;
    }

    public boolean isDiceMoved() {

        return isDiceMoved;
    }

    public boolean isToolCardUsed() {

        return isToolCardUsed;
    }
}
