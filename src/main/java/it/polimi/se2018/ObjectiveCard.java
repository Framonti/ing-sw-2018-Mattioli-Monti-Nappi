package it.polimi.se2018;

public abstract class ObjectiveCard {

    //attributes
    private String name;
    private String description;


    //constructor
    public ObjectiveCard (String name, String description) {
        this.name = name;
        this.description = description;
    }


    //returns card name
    public String getName (){
        return this.name;
    }

    //returns card description
    public String getDescription(){
        return this.description;
    }



}
