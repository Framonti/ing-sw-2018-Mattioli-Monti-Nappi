package it.polimi.se2018;

public class PublicObjectiveCard extends ObjectiveCard {
    private int victoryPoint;

    //constructor
    public PublicObjectiveCard(String name, String description, int victoryPoint){
        super(name, description);
        this.victoryPoint = victoryPoint;
    }
}
