package it.polimi.se2018.events.vcevent;

import it.polimi.se2018.model.Position;

/**
 * @author Daniele Mattioli
 */
public class FluxBrushPlaceDiceEvent extends VCEvent{
    private Position finalPosition;

    public FluxBrushPlaceDiceEvent(String userInput){
        super(13);
        try {
            String[] parameters = userInput.split("\\s+");
            finalPosition = new Position(Integer.parseInt(parameters[0])-1, Integer.parseInt(parameters[1])-1);
        }
        catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Parametri insufficienti");
        }
    }

    public Position getFinalPosition() {
        return finalPosition;
    }
}
