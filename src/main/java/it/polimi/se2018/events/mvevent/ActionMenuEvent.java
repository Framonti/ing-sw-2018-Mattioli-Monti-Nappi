package it.polimi.se2018.events.mvevent;

public class ActionMenuEvent extends MVEvent {

    private boolean isDiceMoved;
    private boolean isToolCardUsed;

    public ActionMenuEvent(Boolean isDiceMoved, boolean isToolCardUsed){

        super(10);
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
