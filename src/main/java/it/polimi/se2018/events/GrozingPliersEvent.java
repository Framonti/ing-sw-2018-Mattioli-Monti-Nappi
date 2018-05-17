package it.polimi.se2018.events;

public class GrozingPliersEvent extends Event {

    private int diceIndexInDraftPool;
    private int choice;

    public GrozingPliersEvent(String userInput){
        try{
            String[] parameters = userInput.split("\\s+");
            diceIndexInDraftPool = Integer.parseInt(parameters[0]);
            choice = Integer.parseInt(parameters[1]);
        }catch(IllegalArgumentException e){
            //Dire all'utente che ha sbagliato; si dovrebbe poter fare con un rilancio di eccezioni,
            // gestite dal controller, ma non sono sicuro
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Parametri insufficienti");
        }
        setId(1);
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

    @Override
    public String toString() {
        return "GrozingPliersEvent";
    }


}
