package it.polimi.se2018.events.vcevent;


import it.polimi.se2018.model.Position;

public class FluxRemoverPlaceDiceEvent extends VCEvent{
    private int diceValue;
    private Position dicePosition;
    public FluxRemoverPlaceDiceEvent(String userInput){
        super(14);
        try {
            String[] input = userInput.split("\\s+");
            diceValue = Integer.parseInt(input[0]) - 1;
            dicePosition = new Position(Integer.parseInt(input[1]) - 1, Integer.parseInt(input[2]) - 1);
        }
        catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Paramentri insufficienti");
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }
    }

    public int getDiceValue() {
        return diceValue;
    }

    public Position getDicePosition() {
        return dicePosition;
    }
}
