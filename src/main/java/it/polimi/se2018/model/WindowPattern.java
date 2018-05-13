package it.polimi.se2018.model;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Position;

/**
 * Class that represents the patterns of the WindowPatternCards
 * @author fabio
 */

public class WindowPattern {

    private String name;
    private int difficultyNumber;
    private Dice[][] windowPattern;

    //Constructor
    public WindowPattern(String name, int difficultyNumber, Dice[][] windowPattern) {
        this.name = name;
        this.difficultyNumber = difficultyNumber;
        this.windowPattern = windowPattern;
    }

    //Getters
    public String getName() {
        return name;
    }

    public int getDifficultyNumber() {
        return difficultyNumber;
    }

    public Dice[][] getWindowPattern() {
        return windowPattern;
    }

    /**
     * Checks if the dice respects the colour and value restrictions of the pattern
     * @param position Represents the position of the cell that has to be checked
     * @param dice Represents the dice that has to be placed in that cell
     * @return True if the dice can be placed, false otherwise
     */
    public boolean checkCell(Position position, Dice dice) {
        return checkCellValueRestriction(position, dice) && checkCellColourRestriction(position, dice);
    }

    /**
     * Checks if the colour restriction is complied with the cell's colour
     * @param position Represents the position of the cell that has to be checked
     * @param dice Represents the dice that has to be placed in that cell
     * @return True if the dice can be placed, false otherwise
     */
    public boolean checkCellColourRestriction(Position position, Dice dice){
        return (windowPattern[position.getX()][position.getY()] == null || windowPattern[position.getX()][position.getY()].getColour().equals(dice.getColour()));
    }

    /**
     * Checks if the value restriction is complied with the cell's colour
     * @param position Represents the position of the cell that has to be checked
     * @param dice Represents the dice that has to be placed in that cell
     * @return True if the dice can be placed, false otherwise
     */
    public boolean checkCellValueRestriction(Position position, Dice dice){
        return (windowPattern[position.getX()][position.getY()] == null || windowPattern[position.getX()][position.getY()].getValue() == dice.getValue());
    }

    @Override
    public String toString() {
        int row;
        int column;
        String toReturn = "Nome: " + name + "\n" + "Difficoltà: " + this.difficultyNumber +  "\n";

        for (row = 0; row < 4; row++) {
            for (column = 0; column < 5; column++) {
                if (windowPattern[row][column] == null)
                    toReturn = toReturn.concat("O\t");
                else toReturn = toReturn.concat(windowPattern[row][column].toString() + "\t");
            }
            toReturn = toReturn.concat("\n");
        }

        return toReturn;
    }
    
}
