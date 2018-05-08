package it.polimi.se2018.model;

import it.polimi.se2018.model.Colour;

public class ToolCard{
    private int favorPoint = 0; //number of favor points on the tool card
    private final Colour colour;
    private final String name;
    private final String description;

    //constructor
    public ToolCard (String name, String description, Colour colour){
        this.name = name;
        this.description = description;
        this.colour = colour;
    }

    //returns the number of favor points on the tool card
    public int getFavorPoint(){
        return this.favorPoint;
    }

    //returns tool card colour
    public Colour getColour (){ return this.colour;}

    //returns tool card name
    public String getName() { return this.name;}

    //return tool card description
    public String getDescription() {return this.description;}

    //increase favorPoint value by n
    public void increaseFavorPoint (int n){
        this.favorPoint += n;
    }

}
