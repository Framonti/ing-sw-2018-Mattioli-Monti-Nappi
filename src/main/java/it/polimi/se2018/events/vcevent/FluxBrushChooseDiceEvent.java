package it.polimi.se2018.events.vcevent;

/**
 * This event is generated when a player uses the FluxBrush ToolCard
 * @author Daniele Mattioli
 */
public class FluxBrushChooseDiceEvent extends VCEvent {

    private int diceIndexInDraftPool;

    /**
     * The Constructor analyzes the userInput and saves it into the class's attribute
     * @param userInput A String representing an user input
     * @throws IllegalArgumentException If the string does not contain valid parameters
     */
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

    /**
     * * Gets the indexInDraftPool
     * @return The indexInDraftPool
     */
    public int getDiceIndexInDraftPool() {
        return diceIndexInDraftPool;
    }
}
