package it.polimi.se2018.model;

/**
 * This class represents a private objective card
 * @author  Daniele Mattioli
 */
public class PrivateObjectiveCard extends ObjectiveCard {
    private final Colour colour;

    /**
     * Constructor of the class.
     * @param name private objective card name
     * @param description private objective card description
     * @param colour private objective card  colour
     */
    public PrivateObjectiveCard(String name, String description,Colour colour){
        super(name, description);
        this.colour = colour;
    }

    /**
     * Gets private objective card colour
     * @return private objective card colour
     */
    public Colour getColour(){
        return this.colour;
    }

    @Override
    //@return a String representing the card itself
    public String toString() {

        return this.getName() + "\n" + this.getDescription();
    }
}
