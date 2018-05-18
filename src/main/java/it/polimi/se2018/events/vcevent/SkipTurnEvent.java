package it.polimi.se2018.events.vcevent;

public class SkipTurnEvent extends Event {

    public SkipTurnEvent() {
        super(100);
    }

    @Override
    public String toString(){
        return "SkipTurnEvent";
    }
}
