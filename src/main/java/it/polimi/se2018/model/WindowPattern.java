package it.polimi.se2018.model;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Position;

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

    //controlls if the dice respects the colour and value restrictions of the pattern
    public boolean checkCell(Position position, Dice dice) {
        return checkCellValueRestriction(position, dice) && checkCellColourRestriction(position, dice);
    }

    //secondo me serve un metodo che controlli che la restrizione sul colore sia rispettata e uno che controlli che quella sul valore sia rispettata
    public boolean checkCellColourRestriction(Position position, Dice dice){
        return (windowPattern[position.getX()][position.getY()].getColour().equals(dice.getColour()));
    }

    public boolean checkCellValueRestriction(Position position, Dice dice){
        return (windowPattern[position.getX()][position.getY()].getValue() == dice.getValue());
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

    @Override
    //@return a String that represent the WindowPattern, already formatted
    //If there is a colour limitation, the colour abbreviation is displayed
    //If there is a value limitation, a number is displayed
    //"O" is displayed when there is neither a colour nor a value limitation
    public String toString() {
        int row;
        int column;
        String toReturn = name + "\n" + "Difficoltà: " + String.valueOf(this.difficultyNumber) +  "\n";

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
