package it.polimi.se2018.events.mvevent;

public class FluxBrushChoiceEvent extends MVEvent{

    String dice;
    public FluxBrushChoiceEvent(String dice) {
        super(13);
        this.dice = dice;
    }

    public String getDice() {
        return dice;
    }
}
