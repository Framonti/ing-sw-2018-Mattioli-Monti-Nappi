package it.polimi.se2018.events;

public class FluxBrushEvent {
    private int diceIndexInDraftPool;

    public FluxBrushEvent(String userInput){
        try {
            String[] parameters = userInput.split("\\s+");
            diceIndexInDraftPool = Integer.parseInt(parameters[0]);
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
}
