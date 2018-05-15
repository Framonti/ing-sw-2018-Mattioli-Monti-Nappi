package it.polimi.se2018.model;

/**
 * This enumeration represents a colour. It is used for creating dices
 * @author Framonti
 */
public enum Colour {

    YELLOW("#e8cf14", "G"),
    GREEN("#0ba363", "V"),
    RED("#de3929", "R"),
    PURPLE("#a1488e", "F"),
    BLUE("#2da8bb", "B");

    private final String colourHEX;
    private final String abbreviation;

    /**
     * Constructor
     * @param colorHex A String that represents the hexadecimal code for the colour
     * @param abbreviation  A single letter for a quick representation of the colour itself
     */
    Colour(String colorHex, String abbreviation) {

        this.colourHEX = colorHex;
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
     * Gets the ColourHex attribute
     * @return This colourHex
     */
    public String getColourHEX() {

        return this.colourHEX;
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
