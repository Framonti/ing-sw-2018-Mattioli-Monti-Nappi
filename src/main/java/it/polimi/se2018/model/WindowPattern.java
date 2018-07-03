package it.polimi.se2018.model;

/**
 * Class that represents the patterns of the WindowPatternCards
 * @author fabio
 */

public class WindowPattern {

    private String name;
    private int difficultyNumber;
    private Dice[][] pattern;

    /**
     * This constructor is called during the setup of the game, using information held in an xml file
     * @param name The name of the WindowPattern
     * @param difficultyNumber The difficulty associated with the WindowPattern (from 3 to 6)
     * @param windowPattern A Matrix of Dice
     */
    public WindowPattern(String name, int difficultyNumber, Dice[][] windowPattern) {
        this.name = name;
        this.difficultyNumber = difficultyNumber;
        this.pattern = windowPattern;
    }

    /**
     * Gets the name of the windowPattern
     * @return The name of the WindowPattern
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the difficulty number associated with the WindowPattern
     * @return The difficulty number associated with the WindowPattern
     */
    public int getDifficultyNumber() {
        return difficultyNumber;
    }

    /**
     * Gets the Matrix representing the windowPattern
     * @return A Bidimensional array of Dice representing the WindowPattern
     */
    public Dice[][] getWindowPattern() {
        return pattern;
    }

    /**
     * Gets the dice of a given position
     * @param position The position of the dice
     * @return The Dice on the position
     */
    private Dice getDice(Position position) {
        return pattern[position.getX()][position.getY()];
    }


    /**
     * Checks if the dice respects the colour and value restrictions of the pattern
     * @param position Represents the position of the cell that has to be checked
     * @param dice Represents the dice that has to be placed in that cell
     * @return True if the dice can be placed, false otherwise
     */
    public boolean checkCell(Position position, Dice dice) {
        if(getDice(position) == null)
            return true;
        else if(getDice(position).getColour() == null)
            return checkCellValueRestriction(position, dice);
        else
            return checkCellColourRestriction(position, dice);
    }

    /**
     * Checks if the colour restriction is complied with the cell's colour
     * @param position Represents the position of the cell that has to be checked
     * @param dice Represents the dice that has to be placed in that cell
     * @return True if the dice can be placed, false otherwise
     */
    public boolean checkCellColourRestriction(Position position, Dice dice) {
        return getDice(position) == null || (getDice(position).getColour() == null || getDice(position).getColour().equals(dice.getColour()));
    }

    /**
     * Checks if the value restriction is complied with the cell's colour
     * @param position Represents the position of the cell that has to be checked
     * @param dice Represents the dice that has to be placed in that cell
     * @return True if the dice can be placed, false otherwise
     */
    public boolean checkCellValueRestriction(Position position, Dice dice) {
        return getDice(position) == null || (getDice(position).getValue() == 0 || getDice(position).getValue() == dice.getValue());
    }

    @Override
    public String toString() {
        int row;
        int column;
        String toReturn = "Nome: " + name + "\n" + "Difficoltà: " + this.difficultyNumber +  "\n\n";

        //Adds graphic elements for the CLI
        for (row = 0; row < 4; row++) {
            toReturn = toReturn.concat("|¯¯¯¯|¯¯¯¯|¯¯¯¯|¯¯¯¯|¯¯¯¯|\n");
            for (column = 0; column < 5; column++) {
                toReturn = toReturn.concat("| ");
                Position position = new Position(row, column);
                if (getDice(position) != null){
                    toReturn = toReturn.concat(getDice(position).toString()) + "  ";
                }
                else toReturn = toReturn.concat("   ");
            }
            toReturn = toReturn.concat("|\n" + "|____|____|____|____|____|\n");
        }

        return toReturn;
    }

    /**
     * Gets a string representing a path for the GUI
     * @return A String representing a path that will be loaded by the GUI
     */
    public String toStringPath(){
        String toReturn = "src/main/Images/WindowPattern/";
        String toConcat = this.name.replaceAll("\\s","");
        toReturn = toReturn.concat(toConcat) + ".png";
        return toReturn;
    }

}
