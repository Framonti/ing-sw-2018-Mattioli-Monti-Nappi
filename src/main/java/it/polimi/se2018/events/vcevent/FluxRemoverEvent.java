package it.polimi.se2018.events.vcevent;

import it.polimi.se2018.model.Position;

/**
 * This class represents the event related to the toolCard Flux Remover
 * @author fabio
 */
public class FluxRemoverEvent extends Event {

    private int diceIndex;
    private Position dicePosition;
    private int yellowDiceValue;
    private int redDiceValue;
    private int greenDiceValue;
    private int purpleDiceValue;
    private int blueDiceValue;

    /**
     * Constructor of the class
     * @param userInput It's the string that contains the parameters
     * @throws IllegalArgumentException If the string does not contain valid parameters
     */
    public FluxRemoverEvent(String userInput) {
        super(11);
        try {
            String[] input = userInput.split("\\s+");
            diceIndex = Integer.parseInt(input[0]) - 1;
            dicePosition = new Position(Integer.parseInt(input[1]) - 1, Integer.parseInt(input[2]) - 1);
            yellowDiceValue = Integer.parseInt(input[3]);
            redDiceValue = Integer.parseInt(input[4]);
            greenDiceValue = Integer.parseInt(input[5]);
            purpleDiceValue = Integer.parseInt(input[6]);
            blueDiceValue = Integer.parseInt(input[7]);
        }
        catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Paramentri insufficienti");
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
     * @return The dicePosition attribute
     */
    public Position getDicePosition() {
        return dicePosition;
    }

    /**
     * @return The yellowDiceValue attribute
     */
    public int getYellowDiceValue() {
        return yellowDiceValue;
    }

    /**
     * @return The redDiceValue attribute
     */
    public int getRedDiceValue() {
        return redDiceValue;
    }

    /**
     * @return The greenDiceValue attribute
     */
    public int getGreenDiceValue() {
        return greenDiceValue;
    }

    /**
     * @return The purpeDiceValue attribute
     */
    public int getPurpleDiceValue() {
        return purpleDiceValue;
    }

    /**
     * @return The blueDiceValue attribute
     */
    public int getBlueDiceValue() {
        return blueDiceValue;
    }

    /**
     * @return The name of this class
     */
    @Override
    public String toString() {
        return "FluxRemoverEvent";
    }
}
