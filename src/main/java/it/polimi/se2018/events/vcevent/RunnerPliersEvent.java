package it.polimi.se2018.events.vcevent;

import it.polimi.se2018.model.Position;

/**
 * This class represents the event related to the toolCard Runner Pliers
 * @author fabio
 */
public class RunnerPliersEvent extends VCEvent {

    private int diceIndex;
    private Position position;

    /**
     * Constructor of the class
     * @param userInput It's the string that contains the parameters
     * @throws IllegalArgumentException If the string does not contain valid parameters
     */
    public RunnerPliersEvent(String userInput) {
        super(8);
        try {
            String[] input = userInput.split("\\s+");
            diceIndex = Integer.parseInt(input[0]) - 1;
            position = new Position(Integer.parseInt(input[1]) - 1, Integer.parseInt(input[2]) - 1);
        }
        catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Parametri insufficienti");
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }
    }

    /**
     * @return The diceIndex attribute
     */
    public int getDiceIndex() {
        return diceIndex;
    }

    /**
     * @return The position attribute
     */
    public Position getPosition() {
        return position;
    }

}
