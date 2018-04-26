package it.polimi.se2018;

public class PrivateObjectiveCard extends Card {
    private int victoryPoint; // point victory value of the private objective card
    private final Colour colour;

    //constructor
    public PrivateObjectiveCard( String name, String description,Colour colour){
        super(name, description);
        this.colour = colour;
    }

    public Colour getColour(){
        return this.colour;
    }
}
