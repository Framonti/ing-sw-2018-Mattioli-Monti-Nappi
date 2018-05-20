package it.polimi.se2018.events.vcevent;

import it.polimi.se2018.model.Position;

public class FluxBrushEvent extends VCEvent {
    private int diceIndexInDraftPool;
    private Position finalPosition;

    public FluxBrushEvent(String userInput){
        super(6);
        try {
            String[] parameters = userInput.split("\\s+");
            diceIndexInDraftPool = Integer.parseInt(parameters[0]) -1;
            finalPosition = new Position(Integer.parseInt(parameters[1])-1, Integer.parseInt(parameters[2])-1);
        }
        catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Parametri insufficienti");
        }
    }

    public int getDiceIndexInDraftPool() {
        return diceIndexInDraftPool;
    }

    public Position getFinalPosition() {
        return finalPosition;
    }

}
