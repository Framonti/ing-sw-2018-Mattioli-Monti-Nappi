package it.polimi.se2018.events.vcevent;

/**
 * This event is generated when a player uses the Grozing Pliers ToolCard
 * @author Daniele Mattioli
 */
public class GrozingPliersEvent extends VCEvent {

    private int diceIndexDraftPool;
    private int choice;

    /**
     * The Constructor analyzes the userInput and saves it into the class's attributes
     * @param userInput A String representing an user input
     * @throws IllegalArgumentException If the string does not contain valid parameters
     */
    public GrozingPliersEvent(String userInput){
        super(1);
        try{
            String[] parameters = userInput.split("\\s+");
            diceIndexDraftPool = Integer.parseInt(parameters[0]) -1;
            choice = Integer.parseInt(parameters[1]) -1;
        }
        catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Parametri insufficienti");
        }
    }

    /**
     * Gets the diceIndexDraftPool
     * @return The diceIndexDraftPool attribute
     */
    public int getDiceIndexDraftPool(){

        return diceIndexDraftPool;
    }

    /**
     * Gets the choice
     * @return The choice
     */
    public int getChoice(){
        return choice;
    }

}
