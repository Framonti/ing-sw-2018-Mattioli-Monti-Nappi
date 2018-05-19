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
            int row = Integer.parseInt(parameters[1]);
            int column = Integer.parseInt(parameters[2]);
            finalPosition = new Position(row-1, column-1);
        }catch(IllegalArgumentException e){
            //Dire all'utente che ha sbagliato; si dovrebbe poter fare con un rilancio di eccezioni,
            // gestite dal controller, ma non sono sicuro
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
