package it.polimi.se2018.events;

import it.polimi.se2018.model.Position;

/**
 * @author fabio
 */
public class RunnerPliersEvent {

    private int diceIndex;
    private Position position;

    //Just creates a new PlaceDiceEvent, the only difference is that the controller must setLap(1)
    public RunnerPliersEvent(String userInput) {
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

    public int getDiceIndex() {
        return diceIndex;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "RunnerPliersEvent";
    }
}
