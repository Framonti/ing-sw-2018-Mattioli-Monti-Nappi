package it.polimi.se2018;

public abstract class ObjectiveCard {
    private String name;
    private String description;
    private int victoryPoint;

    //constructor
    public ObjectiveCard (String name, String description) {
        this.name = name;
        this.description = description;
        this.victoryPoint = 0;
    }

    //returns card name
    public String getName (){
        return this.name;
    }

    //returns card description
    public String getDescription(){
        return this.description;
    }

    //sets card victory point
    public void setVictoryPoint( int victoryPoint){
        this.victoryPoint = victoryPoint;
    }

    //gets card victory point
    public int getVictoryPoint (){
        return this.victoryPoint;
    }

}
