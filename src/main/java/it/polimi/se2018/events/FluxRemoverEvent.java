package it.polimi.se2018.events;

import it.polimi.se2018.model.Position;

public class FluxRemoverEvent {

    private int diceIndex;
    private Position dicePosition;
    private int yellowDiceValue;
    private int redDiceValue;
    private int greenDiceValue;
    private int purpleDiceValue;
    private int blueDiceValue;

    public FluxRemoverEvent(String userInput) {
        try {
            String[] input = userInput.split("\\s+");
            diceIndex = Integer.parseInt(input[0]);
            dicePosition = new Position(Integer.parseInt(input[1]), Integer.parseInt(input[2]));
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

    public int getDiceIndex() {
        return diceIndex;
    }

    public Position getDicePosition() {
        return dicePosition;
    }

    public int getYellowDiceValue() {
        return yellowDiceValue;
    }

    public int getRedDiceValue() {
        return redDiceValue;
    }

    public int getGreenDiceValue() {
        return greenDiceValue;
    }

    public int getPurpleDiceValue() {
        return purpleDiceValue;
    }

    public int getBlueDiceValue() {
        return blueDiceValue;
    }
}
