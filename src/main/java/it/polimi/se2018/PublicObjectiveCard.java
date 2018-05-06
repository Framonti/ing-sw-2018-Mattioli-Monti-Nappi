package it.polimi.se2018;

public class PublicObjectiveCard extends ObjectiveCard {

    //attributes
    private int victoryPoint;


    //constructor
    public PublicObjectiveCard(String name, String description, int victoryPoint){
        super(name, description);
        this.victoryPoint = victoryPoint;
    }


    //gets card victory point
    public int getVictoryPoint (){
        return this.victoryPoint;
    }
}
