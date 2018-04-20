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



    String getColourHEX()
    {
        return this.colourHEX;
    }

    String getAbbreviation()
    {
        return this.abbreviation;
    }
}
