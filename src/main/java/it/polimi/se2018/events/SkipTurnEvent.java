package it.polimi.se2018.events;

public class SkipTurnEvent extends Event {

    public SkipTurnEvent() {
        setId(100);
    }

    @Override
    public String toString(){
        return "SkipTurnEvent";
    }
}
