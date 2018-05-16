package it.polimi.se2018.events;

import it.polimi.se2018.model.Position;

public class TapWheelEvent {

    private int roundIndex;
    private int diceIndex;
    private Position firstDicePosition;
    private Position secondDicePosition;
    private Position newFirstDicePosition;
    private Position newSecondDicePosition;

    public TapWheelEvent(String userInput) {
        try {
            String[] input = userInput.split("\\s+");
            roundIndex = Integer.parseInt(input[0]);
            diceIndex = Integer.parseInt(input[1]);
            firstDicePosition = new Position(Integer.parseInt(input[2]), Integer.parseInt(input[3]));
            if(input.length == 10) {
                secondDicePosition = new Position(Integer.parseInt(input[4]), Integer.parseInt(input[5]));
                newFirstDicePosition = new Position(Integer.parseInt(input[6]), Integer.parseInt(input[7]));
                newSecondDicePosition = new Position(Integer.parseInt(input[8]), Integer.parseInt(input[9]));
            } else {
                newFirstDicePosition = new Position(Integer.parseInt(input[4]), Integer.parseInt(input[5]));
            }
        }
        catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Paramentri insufficienti");
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }
    }

    public int getRoundIndex() {
        return roundIndex;
    }

    public int getDiceIndex() {
        return diceIndex;
    }

    public Position getFirstDicePosition() {
        return firstDicePosition;
    }

    public Position getSecondDicePosition() {
        return secondDicePosition;
    }

    public Position getNewFirstDicePosition() {
        return newFirstDicePosition;
    }

    public Position getNewSecondDicePosition() {
        return newSecondDicePosition;
    }
}
