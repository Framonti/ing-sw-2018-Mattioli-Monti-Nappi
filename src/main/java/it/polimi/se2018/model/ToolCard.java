package it.polimi.se2018.model;

import it.polimi.se2018.events.mvevent.ToolCardEvent;

/**
 * This class represents a tool card
 * @author Daniele Mattioli
 */
public class ToolCard{
    private int favorPoint;  //number of favor points on the tool card
    private final Colour colour;
    private final String name;
    private final String description;
    private int id;

    /**
     * Constructor of the class. Attribute favorPoint is set to 0, because initially there are no
     * favor points placed on the tool card.
     * @param name tool card name
     * @param description tool card description
     * @param colour tool card colour
     */
    public ToolCard (String name, String description, Colour colour, int id){
        this.name = name;
        this.description = description;
        this.colour = colour;
        this.favorPoint = 0;
        this.id = id;
    }

    /**
     * Gets number of favor point on the tool card
     * @return number of favor point on the tool card
     */
    public int getFavorPoint(){
        return this.favorPoint;
    }

    /**
     * Gets tool card id
     * @return Tool card id
     */
    public int getId() {
        return id;
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
        ToolCardEvent toolCardEvent = new ToolCardEvent(GameSingleton.getInstance().toolCardsToString(), GameSingleton.getInstance().toolCardsToStringPath(),GameSingleton.getInstance().getFavorTokensOnToolCards());
        GameSingleton.getInstance().myNotify(toolCardEvent);
    }

    /**
     * @param isSinglePlayer If it's false it's added the price
     * @return A string representation of the ToolCard
     */
    public String toString(boolean isSinglePlayer) {
        String toReturn = getId() + ")\t" + getName() + "\n\t" + getDescription() + "\n\tColore: " + getColour().toString() + "\n";
        if (!isSinglePlayer)
            toReturn = toReturn.concat("\t" + "Prezzo: " + (getFavorPoint() == 0 ? "1" : "2") + "\n");
        return toReturn;
    }

    /**
     * Gets a tool card representation without its description and colour
     * @return Tool card representation without its description and colour
     */
    public String toStringAbbreviated(boolean isSinglePlayer) {
        String toReturn = getId() + ")\t" + getName() + "\n\t"  + "Prezzo: ";
        if (isSinglePlayer)
            toReturn = toReturn.concat("Dado " + getColour().toString());
        else
            toReturn = toReturn.concat((getFavorPoint() == 0 ? "1" : "2"));
        toReturn = toReturn.concat("\n");
        return  toReturn;
    }

    /**
     * Gets a String representing a path of an image for the GUI
     * @return A String that represent a path of an image that will be loaded by the GUI
     */
    public String toStringPath(){
        return "src/main/Images/ToolCard/ToolCard"+this.getId()+".jpg";
    }
}
