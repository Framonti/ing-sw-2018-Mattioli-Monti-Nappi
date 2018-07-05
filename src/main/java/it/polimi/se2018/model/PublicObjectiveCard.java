package it.polimi.se2018.model;

/**
 * This class represents a public objective card and it's a subclass of ObjectiveCard
 * @author Daniele Mattioli
 */
public class PublicObjectiveCard extends ObjectiveCard {

    private int victoryPoint;

    /**
     * Constructor of the class
     * @param name public objective card name
     * @param description public objective card description
     * @param victoryPoint public objective card victory points
     */
    public PublicObjectiveCard(String name, String description, int victoryPoint){
        super(name, description);
        this.victoryPoint = victoryPoint;
    }

    @Override
    public String toString() {

        String toReturn = "Nome: " + this.getName() + "\n" + "Descrizione: " + this.getDescription() + "\n" + "Punti Vittoria: ";

        if(victoryPoint != 0)
            toReturn = toReturn.concat(String.valueOf(this.victoryPoint)+"\n");
        else toReturn = toReturn.concat("#\n");

        return  toReturn;
    }

    /**
     * Gets a String representing a path for the GUI representation of this card
     * @return A String representing a path for the GUI representation
     */
    public String toStringPath(){
        String toReturn = "src/main/Images/PublicObjectiveCard/";
        String toConcat = this.getName().replaceAll("\\s","");
        toReturn = toReturn.concat(toConcat) + ".jpg";
        return toReturn;
    }
}
