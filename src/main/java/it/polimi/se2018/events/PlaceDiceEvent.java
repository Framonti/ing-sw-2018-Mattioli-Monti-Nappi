package it.polimi.se2018.events;

import it.polimi.se2018.model.Position;

/**
 * This class represents one of the core event in the game:
 * getting a dice from the DraftPool and putting it on the DicePattern
 * @author Framonti
 */
public class PlaceDiceEvent extends Event {

    private int diceIndexDraftPool;
    private Position positionToPlace;

    /**
     * Construct a PlaceDiceEvent object
     * @param userInput The values given by an user
     * @throws IllegalArgumentException if the value given by the user aren't number or if they represent invalid values
     * @throws IndexOutOfBoundsException if the user don't give enough parameters
     */
    public PlaceDiceEvent(String userInput){

        try{

            String[] parameters = userInput.split("\\s+");
            diceIndexDraftPool = Integer.valueOf(parameters[0]);
            int row = Integer.parseInt(parameters[1]);
            int column = Integer.parseInt(parameters[2]);
            positionToPlace = new Position(row-1, column-1);
        }
        catch(IllegalArgumentException e){
            //Dire all'utente che ha sbagliato; si dovrebbe poter fare con un rilancio di eccezioni,
            // gestite dal controller, ma non sono sicuro
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }
        catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Parametri insufficienti");
        }
        setId(99);
    }

    /**
     * Gets the diceIndexDraftPool
     * @return The diceIndexDraftPool attribute
     */
    public int getDiceIndexDraftPool(){

        return diceIndexDraftPool;
    }

    /**
     * Gets the positionToPlace
     * @return The positionToPlace
     */
    public Position getPositionToPlace() {

        return positionToPlace;
    }

    @Override
    public String toString() {
        return "PlaceDiceEvent";
    }
}
