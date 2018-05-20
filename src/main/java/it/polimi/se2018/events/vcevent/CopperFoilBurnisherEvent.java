package it.polimi.se2018.events.vcevent;

import it.polimi.se2018.model.Position;

public class CopperFoilBurnisherEvent extends VCEvent {

    private Position initialPosition;
    private Position finalPosition;

    public CopperFoilBurnisherEvent(String userInput) {

        super(3);
        try {
            String[] parameters = userInput.split("\\s+");
            initialPosition = new Position(Integer.parseInt(parameters[0]) - 1, Integer.parseInt(parameters[1])- 1);
            finalPosition = new Position(Integer.parseInt(parameters[2]) - 1, Integer.parseInt(parameters[3]) - 1);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Parametri insufficienti");
        }
    }

    public Position getInitialPosition() {

        return initialPosition;
    }

    public Position getFinalPosition() {

        return finalPosition;
    }

}