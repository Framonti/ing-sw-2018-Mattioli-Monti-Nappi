package it.polimi.se2018.events.vcevent;



/**
 * @author Daniele Mattioli
 */
public class FluxBrushChooseDiceEvent extends VCEvent {

    private int diceIndexInDraftPool;

    public FluxBrushChooseDiceEvent(String userInput){
        super(6);
        try {
            String[] parameters = userInput.split("\\s+");
            diceIndexInDraftPool = Integer.parseInt(parameters[0]) -1;
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
}
