package it.polimi.se2018;

public class ToolCard{
    private int favorPoint = 0; //number of favorPoint on the tool card
    private final Colour colour;
    private final String name;
    private final String description;

    //constructor
    public ToolCard (String name, String description, Colour colour){
        this.name = name;
        this.description = description;
        this.colour = colour;
    }

    public int getFavorPoint(){
        return this.favorPoint;
    }

    public Colour getColour (){ return this.colour;}

    public String getName() { return this.name;}

    public String getDescription() {return this.description;}

    //increase favorPoint value by n
    public void increaseFavorPoint (int n){
        this.favorPoint += n;
    }




}
