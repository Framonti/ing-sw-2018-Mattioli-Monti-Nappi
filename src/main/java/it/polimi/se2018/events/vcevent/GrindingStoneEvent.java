package it.polimi.se2018.events.vcevent;

/**
 * This event is generated when a player uses the Grinding Stone ToolCard
 * @author fabio
 */
public class GrindingStoneEvent extends VCEvent {

    private int dicePosition;

    /**
     * The Constructor analyzes the userInput and saves it into the class's attribute
     * @param userInput A String representing an user input
     * @throws IllegalArgumentException If the string does not contain valid parameters
     */
    public GrindingStoneEvent(String userInput) {

        super(10);
        try {
            dicePosition = Integer.parseInt(userInput) -1;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }

    }

    /**
     * Gets the dicePosition
     * @return The dicePosition
     */
    public int getDicePosition() {
        return dicePosition;
    }

}
