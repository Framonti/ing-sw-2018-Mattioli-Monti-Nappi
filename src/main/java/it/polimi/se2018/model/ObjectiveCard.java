package it.polimi.se2018.model;

/**
 * This abstract class represents an objective card: its subclasses are PublicObjectiveCard and PrivateObjectiveCard
 * @author Daniele Mattioli
 */

public abstract class ObjectiveCard {

    private String name;
    private String description;

    /**
     * Constructor of the class
     * @param name objective card name
     * @param description objective card description
     */
    public ObjectiveCard (String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * gets objective card name
     * @return objective card name
     */
    public String getName (){
        return this.name;
    }

    /**
     * gets objective card description
     * @return objective card description
     */
    public String getDescription(){
        return this.description;
    }



}
