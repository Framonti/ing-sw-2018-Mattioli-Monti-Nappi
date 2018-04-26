package it.polimi.se2018;

public abstract class Card {
    private String name;
    private String description;
    private int victoryPoint;

    //constructor
    public Card (String name, String description) {
        this.name = name;
        this.description = description;
        this.victoryPoint = 0;
    }


    public String getName (){
        return this.name;
    }


    public String getDescription(){
        return this.description;
    }


    public void setVictoryPoint( int victoryPoint){
        this.victoryPoint = victoryPoint;
    }

    public int getVictoryPoint (){
        return this.victoryPoint;
    }

}
