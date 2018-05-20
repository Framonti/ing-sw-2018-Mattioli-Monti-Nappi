package it.polimi.se2018.events.vcevent;

public class GrindingStoneEvent extends VCEvent {

    private int dicePosition;

    public GrindingStoneEvent(String userInput) {

        super(10);
        try {
            dicePosition = Integer.parseInt(userInput) -1;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }

    }

    public int getDicePosition() {
        return dicePosition;
    }

}
