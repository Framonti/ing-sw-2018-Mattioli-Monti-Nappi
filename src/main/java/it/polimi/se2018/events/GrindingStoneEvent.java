package it.polimi.se2018.events;

public class GrindingStoneEvent extends Event {

    private int dicePosition;

    public GrindingStoneEvent(String userInput) {
        try {
            dicePosition = Integer.parseInt(userInput);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }
        setId(10);
    }

    public int getDicePosition() {
        return dicePosition;
    }

    @Override
    public String toString() {
        return "GrindingStoneEvent";
    }
}
