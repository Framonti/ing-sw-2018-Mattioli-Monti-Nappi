package it.polimi.se2018.model;

import it.polimi.se2018.model.ObjectiveCard;

public class PublicObjectiveCard extends ObjectiveCard {

    //attributes
    private int victoryPoint; //This attribute will be 0 only for the card "Diagonali Colorate"

    //constructor
    public PublicObjectiveCard(String name, String description, int victoryPoint){
        super(name, description);
        this.victoryPoint = victoryPoint;
    }

    //gets card victory point
    public int getVictoryPoint (){
        return this.victoryPoint;
    }

    @Override
    //@return a String representing the card itself
    public String toString() {

        String toReturn = this.getName() + "\n" + this.getDescription() + "\n" + "Punti Vittoria: ";

        if(victoryPoint != 0)
            toReturn = toReturn.concat(String.valueOf(this.victoryPoint));
        else toReturn = toReturn.concat("#");

        return  toReturn;
    }
}
