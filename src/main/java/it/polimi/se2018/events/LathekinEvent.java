package it.polimi.se2018.events;

import it.polimi.se2018.model.Position;

public class LathekinEvent {
    private Position initialPosition1;
    private Position finalPosition1;
    private Position initialPosition2;
    private Position finalPosition2;

    public LathekinEvent(String userInput){
        try{
            String[] parameters = userInput.split("\\s+");
            int initialRow = Integer.parseInt(parameters[0]);
            int initialColumn = Integer.parseInt(parameters[1]);
            initialPosition1 = new Position(initialRow-1, initialColumn-1);
            int finalRow = Integer.parseInt(parameters[2]);
            int finalColumn = Integer.parseInt(parameters[3]);
            finalPosition1 = new Position (finalRow-1, finalColumn-1);
            initialRow = Integer.parseInt(parameters[4]);
            initialColumn = Integer.parseInt(parameters[5]);
            initialPosition2 = new Position ( initialRow-1, initialColumn-1);
            finalRow = Integer.parseInt(parameters[6]);
            finalColumn = Integer.parseInt(parameters[7]);
            finalPosition2 = new Position ( finalRow-1, finalColumn-1);
        }catch(IllegalArgumentException e){
            //Dire all'utente che ha sbagliato; si dovrebbe poter fare con un rilancio di eccezioni,
            // gestite dal controller, ma non sono sicuro
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Parametri insufficienti");
        }
    }

    public Position getInitialPosition1() {
        return initialPosition1;
    }

    public Position getFinalPosition1() {
        return finalPosition1;
    }

    public Position getInitialPosition2() {
        return initialPosition2;
    }

    public Position getFinalPosition2() {
        return finalPosition2;
    }

    @Override
    public String toString() {
        return "LathekinEvent";
    }
}
