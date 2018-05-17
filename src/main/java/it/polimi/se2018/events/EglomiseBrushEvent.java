package it.polimi.se2018.events;

import it.polimi.se2018.model.Position;

/**
 * used by tool card 2, 3, 4, 9
 */
public class EglomiseBrushEvent extends Event {
    private Position initialPosition;
    private Position finalPosition;
    public EglomiseBrushEvent(String userInput){
        try{
            String[] parameters = userInput.split("\\s+");
            int initialRow = Integer.parseInt(parameters[0]);
            int initialColumn = Integer.parseInt(parameters[1]);
            initialPosition = new Position(initialRow-1, initialColumn-1);
            int finalRow = Integer.parseInt(parameters[2]);
            int finalColumn = Integer.parseInt(parameters[3]);
            finalPosition = new Position (finalRow-1, finalColumn-1);
        }catch(IllegalArgumentException e){
            //Dire all'utente che ha sbagliato; si dovrebbe poter fare con un rilancio di eccezioni,
            // gestite dal controller, ma non sono sicuro
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Parametri insufficienti");
        }
        setId(2);
    }

    public Position getInitialPosition() {
        return initialPosition;
    }


    public Position getFinalPosition() {

        return finalPosition;
    }
    @Override
    public String toString() {
        return "EglomiseBrushEvent";
    }

}
