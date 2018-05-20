package it.polimi.se2018.events.vcevent;

public class GrozingPliersEvent extends VCEvent {

    private int diceIndexInDraftPool;
    private int choice;

    public GrozingPliersEvent(String userInput){
        super(1);
        try{
            String[] parameters = userInput.split("\\s+");
            diceIndexInDraftPool = Integer.parseInt(parameters[0]) -1;
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

        return diceIndexInDraftPool;
    }

    public int getChoice(){
        return choice;
    }

}
