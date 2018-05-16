package it.polimi.se2018.events;

public class GrindingStoneEvent {

    private int dicePosition;

    public GrindingStoneEvent(String userInput) {
        try {
            dicePosition = Integer.parseInt(userInput);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }
    }

    public int getDicePosition() {
        return dicePosition;
    }

    @Override
    public String toString() {
        return "GrindingStoneEvent";
    }
}
