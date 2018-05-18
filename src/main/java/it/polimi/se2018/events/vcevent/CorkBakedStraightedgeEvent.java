package it.polimi.se2018.events.vcevent;

import it.polimi.se2018.model.Position;

public class CorkBakedStraightedgeEvent extends Event {
    private int indexInDraftPool;
    private Position finalPosition;

    public CorkBakedStraightedgeEvent(String userInput){

        super(9);
        try{
            String[] parameters = userInput.split("\\s+");
            indexInDraftPool = Integer.parseInt(parameters[0]);
            int finalRow = Integer.parseInt(parameters[1]);
            int finalColumn = Integer.parseInt(parameters[2]);
            finalPosition = new Position (finalRow-1, finalColumn-1);
        }catch(IllegalArgumentException e){
            //Dire all'utente che ha sbagliato; si dovrebbe poter fare con un rilancio di eccezioni,
            // gestite dal controller, ma non sono sicuro
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Parametri insufficienti");
        }
    }

    public int getIndexInDraftPool() {
        return indexInDraftPool;
    }

    public Position getFinalPosition() {

        return finalPosition;
    }

    @Override
    public String toString() {
        return "CorkBakedStraightedgeEvent";
    }
}
