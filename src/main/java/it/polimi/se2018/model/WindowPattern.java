package it.polimi.se2018.model;


/**
 * Class that represents the patterns of the WindowPatternCards
 * @author fabio
 */

public class WindowPattern {

    private String name;
    private int difficultyNumber;
    private Dice[][] pattern;

    //Constructor
    public WindowPattern(String name, int difficultyNumber, Dice[][] windowPattern) {
        this.name = name;
        this.difficultyNumber = difficultyNumber;
        this.pattern = windowPattern;
    }

    //Getters
    public String getName() {
        return name;
    }

    public int getDifficultyNumber() {
        return difficultyNumber;
    }

    public Dice[][] getWindowPattern() {
        return pattern;
    }

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
    
}
