package it.polimi.se2018;

public enum Colour
{
    YELLOW("#e8cf14", "Y"),
    GREEN("#0ba363", "G"),
    RED("#de3929", "R"),
    PURPLE("#a1488e", "P"),
    BLUE("#2da8bb", "B");

    private final String colourHEX;
    private final String abbreviation;

    Colour(String colorHex, String abbreviation)
    {
        this.colourHEX = colorHex;
        this.abbreviation = abbreviation;
    }

    public static Colour getColourFromString(String colourString)
    {
        switch(colourString)
        {
            case "YELLOW": return Colour.YELLOW;
            case "RED" : return  Colour.RED;
            case "PURPLE" : return  Colour.PURPLE;
            case "BLUE" : return  Colour.BLUE;
            case "GREEN" : return  Colour.GREEN;
            default : System.out.println("errore scelta dado");
        }
        return null;
    }


    String getColourHEX()
    {
        return this.colourHEX;
    }

    String getAbbreviation()
    {
        return this.abbreviation;
    }
}
