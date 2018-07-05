package it.polimi.se2018.events.mvevent;

/**
 * This event holds information about the effect of the FluxBrush ToolCard
 * @author fabio
 */
public class FluxBrushChoiceEvent extends MVEvent{

    private String dice;

    /**
     * Constructor
     * @param dice A representation of the dice
     */
    public FluxBrushChoiceEvent(String dice) {
        super(13);
        this.dice = dice;
    }

    /**
     * Gets the dice
     * @return A String representing a dice
     */
    public String getDice() {
        return dice;
    }
}
