package it.polimi.se2018.events.mvevent;

public class FluxRemoverChoiceEvent extends MVEvent {

    private String dice;
    public FluxRemoverChoiceEvent(String dice){
        super(14);
        this.dice = dice;

    }

    public String getDice() {
        return dice;
    }
}
