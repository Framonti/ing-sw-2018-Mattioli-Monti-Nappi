package it.polimi.se2018.model;

/**
 * This class represents a tool card
 * @author Daniele Mattioli
 */
public class ToolCard{
    private int favorPoint;  //number of favor points on the tool card
    private final Colour colour;
    private final String name;
    private final String description;

    /**
     * Constructor of the class. Attribute favorPoint is set to 0, because initially there are no
     * favor points placed on the tool card.
     * @param name tool card name
     * @param description tool card description
     * @param colour tool card colour
     */
    public ToolCard (String name, String description, Colour colour){
        this.name = name;
        this.description = description;
        this.colour = colour;
        this.favorPoint = 0;
    }

    /**
     * Gets number of favor point on the tool card
     * @return number of favor point on the tool card
     */
    public int getFavorPoint(){
        return this.favorPoint;
    }


    /**
     * Gets tool card colour
     * @return tool card colour
     */
    public Colour getColour (){ return this.colour;}

    /**
     * Gets tool card name
     * @return tool card name
     */
    public String getName() { return this.name;}

    /**
     * Gets tool card description
     * @return tool card description
     */
    public String getDescription() {return this.description;}

    /**
     * Increases favorPoint value by n
     * @param n value to be added to the current value of favorPoint
     */
    public void increaseFavorPoint (int n){
        this.favorPoint += n;
    }

    @Override
    public String toString() {
        return "Nome: " + getName() + "\n" + "Descrizione: " + getDescription() + "\n" + "Prezzo: " + (getFavorPoint() == 0 ? "1" : "2") +
                "\nColore: " + getColour().toString();
    }
}
