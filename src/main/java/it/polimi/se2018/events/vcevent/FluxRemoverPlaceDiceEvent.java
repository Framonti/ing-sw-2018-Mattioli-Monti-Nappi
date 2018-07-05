package it.polimi.se2018.events.vcevent;

import it.polimi.se2018.model.Position;

/**
 *  This event is generated when a player uses the Flux Remover ToolCard, after picking a dice from the DraftPool
 *  @author Daniele Mattioli
 */
public class FluxRemoverPlaceDiceEvent extends VCEvent{

    private int diceValue;
    private Position dicePosition;

    /**
     * The Constructor analyzes the userInput and saves it into the class's attributes
     * @param userInput A String representing an user input
     * @throws IllegalArgumentException If the string does not contain valid parameters
     */
    public FluxRemoverPlaceDiceEvent(String userInput){
        super(14);
        try {
            String[] input = userInput.split("\\s+");
            diceValue = Integer.parseInt(input[0]);
            dicePosition = new Position(Integer.parseInt(input[1]) - 1, Integer.parseInt(input[2]) - 1);
        }
        catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Paramentri insufficienti");
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }
    }

    /**
     * Gets the diceValue
     * @return The diceValue
     */
    public int getDiceValue() {
        return diceValue;
    }

    /**
     * Gets the dicePosition
     * @return The dicePosition
     */
    public Position getDicePosition() {
        return dicePosition;
    }
}
