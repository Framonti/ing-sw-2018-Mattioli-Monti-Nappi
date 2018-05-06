package it.polimi.se2018;

public class PrivateObjectiveCard extends ObjectiveCard {
    private final Colour colour;

    //constructor
    public PrivateObjectiveCard( String name, String description,Colour colour){
        super(name, description);
        this.colour = colour;
    }

    //returns public objective card colour
    public Colour getColour(){
        return this.colour;
    }

}