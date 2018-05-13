package it.polimi.se2018.model;

/**
 * This class represents a public objective card
 * @author Daniele Mattioli
 */
public class PublicObjectiveCard extends ObjectiveCard {


    private int victoryPoint; //This attribute will be 0 only for the card "Diagonali Colorate"

    /**
     * Constructor of the class
     * @param name public objective card name
     * @param description public objective card description
     * @param victoryPoint public objective card victory points
     */
    public PublicObjectiveCard(String name, String description, int victoryPoint){
        super(name, description);
        this.victoryPoint = victoryPoint;
    }

    /**
     * Gets public objective card victory point
     * @return public objective card victory point
     */

    public int getVictoryPoint (){
        return this.victoryPoint;
    }

    @Override
    //@return a String representing the card itself
    public String toString() {

        String toReturn = "Nome: " + this.getName() + "\n" + "Descrizione: " + this.getDescription() + "\n" + "Punti Vittoria: ";

        if(victoryPoint != 0)
            toReturn = toReturn.concat(String.valueOf(this.victoryPoint));
        else toReturn = toReturn.concat("#");

        return  toReturn;
    }
}
