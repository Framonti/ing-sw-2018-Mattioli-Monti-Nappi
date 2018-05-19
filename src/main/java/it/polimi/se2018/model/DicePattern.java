package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds informations about the dice placed by a player on his WindowPattern.
 * It offers limitations checks when a player want to place a Dice
 * @author Framonti
 */
public class DicePattern {

    private boolean firstDice = true;   //true until a dice is placed on the diceMatrix
    private Dice[][] diceMatrix = new Dice[4][5];
    private WindowPattern windowPattern;

    /**
     * Constructor
     * @param windowPattern The WindowPatter linked to this DicePattern
     */
    public DicePattern(WindowPattern windowPattern) {

        this.windowPattern = windowPattern;
    }

    /**
     * Gets the dice in the required position
     * @return The dice in the required Position. It can return null
     */
    public Dice getDice(Position position) {

        return diceMatrix[position.getX()][position.getY()];
    }

    //setter
    public void setDice(Position position, Dice dice) {

        diceMatrix[position.getX()][position.getY()] = dice;
    }

    /**
     * Checks if a position has a dice
     * @return True if there is no dice in the required position
     */
    public  boolean isEmpty(Position position) {

        return diceMatrix[position.getX()][position.getY()] == null;
    }

    /**
     * Counts the number of empty cells on this DicePattern
     * @return The number of empty cells on this DicePattern
     */
    public int emptySpaces(){

        int partialSum = 0;
        int rows = 4;
        int columns = 5;
        Position p1;

        //Checks if every cell is empty or has a dice on it
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {

                p1 = new Position(i, j);
                if(this.isEmpty(p1))
                    partialSum++;
            }
        }
        return partialSum;
    }

    /**
     * Checks if a position is on the edge of the DicePattern and if
     * no dice has already been placed on the DicePattern before
     * @return True if the position required is on one of the edge of diceMatrix and there are no other dice placed on the DicePattern before
     * */
    public boolean checkEdge(Position position) {

        //compare the param position with every position on an edge
        return (firstDice &&(position.getX() == 0 || position.getX() == (diceMatrix.length - 1) ||
                position.getY() == 0 || position.getY() == (diceMatrix[0].length -1)));
    }

    /**
     * Checks if there is a dice in any adjacent position
     * @return True if at least an adjacent position has a dice
     */
    public boolean checkAdjacency(Position position) {

        List<Position> positionsToCheck = position.getAdjacentPositions();
        for(Position positions : positionsToCheck) {

            if(!isEmpty(positions)) return true;
        }
        return false;
    }

    /**
     * Checks if an orthogonal adjacent position has a dice of the same colour of the dice required
     * @return True if there is no dice with the same colour of the dice required
     * */
    public boolean checkAdjacentColour(Position position, Dice dice)
    {
        List<Position> positionsToCheck = position.getOrthogonalAdjacentPositions();
        for(Position positions : positionsToCheck) {

            if(!isEmpty(positions) && this.getDice(positions).getColour().equals(dice.getColour())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if an orthogonal adjacent position has a dice of the same value of the dice required
     * @return True if there is no dice with the same colour of the dice required
     */
    public boolean checkAdjacentValue(Position position, Dice dice)
    {
        List<Position> positionsToCheck = position.getOrthogonalAdjacentPositions();
        for(Position positions : positionsToCheck) {

            if(!isEmpty(positions) && this.getDice(positions).getValue() == (dice.getValue()))
                return false;
        }
        return true;
    }

    /**
     * Checks if all the DicePattern limitations are respected
     * @param position The position a player wants to place a dice on
     * @param dice The dice a player wants to place
     * @return True if all the limitations are respected
     */
    public boolean checkDicePatternLimitations(Position position, Dice dice) {

        if(checkEdge(position))
            return  true;
        else return checkAdjacency(position) && checkAdjacentColour(position, dice) && checkAdjacentValue(position, dice);
    }

    /**
     * Places a dice on matrixDice, only if all the restrictions are met.
     * @param dice The dice a player wants to place
     * @param position The position a player wants to place a dice on
     * @throws IllegalArgumentException if the dice can't be placed
     */
    public void placeDice(Position position, Dice dice) {

        if(checkDicePatternLimitations(position, dice) && windowPattern.checkCell(position, dice)) {

            setDice(position, dice);
            firstDice = false;
        }
        else throw new IllegalArgumentException("Illegal move");
    }

    /**
     * Removes and returns a dice from the required position
     * @return A dice from the required position.
     * @throws IllegalArgumentException if the position required has no dice
     */
    public Dice removeDice(Position position) {

        if(isEmpty(position))
            throw new IllegalArgumentException("The position required has no dice");
        Dice toReturn = this.getDice(position);
        this.setDice(position, null);
        return toReturn;
    }

    /**
     * Moves a dice from a position to another, checking that the initial position has a dice and that
     * the final position is empty
     * @throws IllegalArgumentException if the initial position has a dice or the final position is empty
     */
    public void moveDice(Position initialPosition, Position finalPosition) {

        if(this.isEmpty(initialPosition))
            throw new IllegalArgumentException("Initial position is empty");
        else {
            if(!this.isEmpty(finalPosition))
                throw new IllegalArgumentException("Final position has already had a dice");
            else {
                Dice toMove = removeDice(initialPosition);
                this.setDice(finalPosition, toMove);
            }
        }
    }

    /**
     * Creates a String representing the whole DicePattern, including the WindowPattern below it,
     * properly formatted
     * @return A String representing the whole DicePattern, including the WindowPattern below it
     * */
    @Override
    public String toString(){

        int row = 4;
        int column = 5;
        String toReturn = "Nome: " + windowPattern.getName() + "\n" + "Difficoltà: " + windowPattern.getDifficultyNumber()+ "\n";

        for (int i = 0; i < row; i++){
            for (int j = 0; j < column; j++){
                //The method concat the WindowPattern only if there isn't a dice on the DicePattern
                //id est DiceMatrix[i][j] is null
                if(diceMatrix[i][j] == null){
                    if (windowPattern.getWindowPattern()[i][j] == null)
                        toReturn = toReturn.concat("O\t");
                    else toReturn = toReturn.concat(windowPattern.getWindowPattern()[i][j].toString() + "\t");
                }
                else toReturn = toReturn.concat(diceMatrix[i][j].toString() + "\t");
            }
            toReturn = toReturn.concat("\n");
        }
        return toReturn;
    }

    public List<String> dicePatternToString(){
        ArrayList<String> list = new ArrayList<>();
        list.add(this.toString());
        return list;
    }

}