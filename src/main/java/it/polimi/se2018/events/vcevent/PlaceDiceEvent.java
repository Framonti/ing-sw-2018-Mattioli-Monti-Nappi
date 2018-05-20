package it.polimi.se2018.events.vcevent;

import it.polimi.se2018.model.Position;

/**
 * This class represents one of the core event in the game:
 * getting a dice from the DraftPool and putting it on the DicePattern
 * @author Framonti
 */
public class PlaceDiceEvent extends VCEvent {

    private int diceIndexDraftPool;
    private Position positionToPlace;

    /**
     * Construct a PlaceDiceEvent object
     * @param userInput The values given by an user
     * @throws IllegalArgumentException if the value given by the user aren't number or if they represent invalid values
     * @throws IndexOutOfBoundsException if the user don't give enough parameters
     */
    public PlaceDiceEvent(String userInput){
        super(99);

        try{

            String[] parameters = userInput.split("\\s+");

            diceIndexDraftPool = Integer.valueOf(parameters[0]) -1;
            positionToPlace = new Position(Integer.parseInt(parameters[1])-1, Integer.parseInt(parameters[2])-1);
        }
        catch(IllegalArgumentException e){

            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }
        catch (IndexOutOfBoundsException e){
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
     * Gets the positionToPlace
     * @return The positionToPlace
     */
    public Position getPositionToPlace() {

        return positionToPlace;
    }

}
