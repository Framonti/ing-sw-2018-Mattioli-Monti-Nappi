package it.polimi.se2018.model;

/**
 * This class represents a private objective card and it's a subclass of ObjectiveCard
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
    public String toString() {

        return this.getName() + "\n" + this.getDescription()+"\n";
    }

    /**
     * Gets a String representing a path for the GUI representation of this card
     * @return A String representing a path for the GUI representation
     */
    public String toStringPath(){
        return "src/main/Images/PrivateObjectiveCard/Sfumature"+this.getColour().getAbbreviation()+".jpg";
    }
}
