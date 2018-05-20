package it.polimi.se2018.events.vcevent;

import it.polimi.se2018.model.Position;

public class CorkBakedStraightedgeEvent extends VCEvent {
    private int indexInDraftPool;
    private Position finalPosition;

    public CorkBakedStraightedgeEvent(String userInput){

        super(9);
        try{
            String[] parameters = userInput.split("\\s+");
            indexInDraftPool = Integer.parseInt(parameters[0])-1;
            finalPosition = new Position (Integer.parseInt(parameters[1]) -1, Integer.parseInt(parameters[2])-1);
        }
        catch(IllegalArgumentException e){
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

}
