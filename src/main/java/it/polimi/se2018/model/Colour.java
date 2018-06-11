package it.polimi.se2018.model;

/**
 * This enumeration represents a colour. It is used for creating dices
 * @author Framonti
 */
public enum Colour {

    YELLOW("G"),
    GREEN("V"),
    RED("R"),
    PURPLE("F"),
    BLUE("B");

    private final String abbreviation;

    /**
     * Constructor
     * @param abbreviation  A single letter for a quick representation of the colour itself
     */
    Colour( String abbreviation) {

        this.abbreviation = abbreviation;
    }

    /**
     * Creates a Colour from a string with its name
     * @param colourString A colour name
     * @return A colour object with the same colour as the string
     */
    public static Colour getColourFromString(String colourString) {

        switch(colourString) {

            case "YELLOW": return Colour.YELLOW;
            case "RED" : return  Colour.RED;
            case "PURPLE" : return  Colour.PURPLE;
            case "BLUE" : return  Colour.BLUE;
            case "GREEN" : return  Colour.GREEN;
            default : throw new IllegalArgumentException("Illegal Colour");
        }
    }

    /**
     * Gets the colour abbreviation
     * @return This abbreviation
     */
    public String getAbbreviation() {

        return this.abbreviation;
    }

    @Override
    public String toString() {
        switch (this) {
            case YELLOW: return "Giallo";
            case GREEN: return "Verde";
            case RED: return "Rosso";
            case BLUE: return  "Blu";
            case PURPLE: return "Viola";
            default: return "Colore mancante";
        }
    }
}
