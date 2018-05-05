package it.polimi.se2018;

import java.util.List;

//This class uses the default constructor
public class DicePattern
{
    private boolean firstDice = true;
    private Dice[][] diceMatrix = new Dice[4][5];

    //@return the dice in the required position.
    //It can return null
    public Dice getDice(Position position)
    {
        return diceMatrix[position.getX()][position.getY()];
    }

    public void setDice(Position position, Dice dice)
    {
        diceMatrix[position.getX()][position.getY()] = dice;
    }

    //Check if a position has a dice
    //@return true if there is no dice in the required position
    public  boolean isEmpty(Position position)
    {
        return diceMatrix[position.getX()][position.getY()] == null;
    }

    //Check if a position is on the edge of the DicePattern
    //@return true if firstDice is true and the required position is on one of the edge of diceMatrix
    public boolean checkEdge(Position position)
    {
        return (firstDice &&(position.getX() == 0 || position.getX() == (diceMatrix.length - 1) || position.getY() == 0 || position.getY() == (diceMatrix[0].length -1)));
    }

    //Check if there is a dice in any adjacent position
    //@return true if at least an adjacent position has a dice
    public boolean checkAdjacency(Position position)
    {
        List<Position> positionsToCheck = position.getAdjacentPositions(diceMatrix.length, diceMatrix[0].length);
        for(Position positions : positionsToCheck)
        {
            if(!isEmpty(positions)) return true;
        }
        return false;
    }

    //Check if an orthogonal adjacent position has a dice of the same colour of the @param "dice"
    //@return true if there is no dice with the same colour of the @param "dice"
    public boolean checkAdjacentColour(Position position, Dice dice)
    {
        List<Position> positionsToCheck = position.getOrthogonalAdjacentPositions(diceMatrix.length, diceMatrix[0].length);

        for(Position positions : positionsToCheck)
        {
            if(!(isEmpty(positions) && this.getDice(positions).getColour().equals(dice.getColour())))
                return false;
        }
        return true;
    }

    //Check if an orthogonal adjacent position has a dice of the same value of the @param "dice"
    //@return true if there is no dice with the same value of the @param "dice"
    public boolean checkAdjacentValue(Position position, Dice dice)
    {
        List<Position> positionsToCheck = position.getOrthogonalAdjacentPositions(diceMatrix.length, diceMatrix[0].length);
        for(Position positions : positionsToCheck)
        {
            if(!(isEmpty(positions) && this.getDice(positions).getValue() == (dice.getValue())))
                return false;
        }
        return true;
    }

    //Place a dice on matrixDice, only if all the restrictions are respected
    //@return true if the operation is successful
    public boolean placeDice(Position position, Dice dice)
    {
        if(firstDice && checkEdge(position))
        {
            this.firstDice = false;
            this.setDice(position, dice);
            return  true;
        }
        else if(!firstDice && checkAdjacency(position) && checkAdjacentColour(position, dice) && checkAdjacentValue(position, dice))
        {
            this.setDice(position, dice);
            return true;
        }
        else return false;
    }

    //Remove and @return a dice from the required position
    private Dice removeDice(Position position)
    {
        Dice toReturn = this.getDice(position);
        this.setDice(position, null);
        return toReturn;
    }

    //Move a dice from a position to another, without any check on the limitations
    //If is impossible to move the dice, the method @return false and nothing happens
    public boolean moveDice(Position initialPosition, Position finalPosition)
    {
        if(this.isEmpty(initialPosition))
            return false;
        else
        {
            if(!this.isEmpty(finalPosition))
                return false;
            else
            {
                Dice toMove = removeDice(initialPosition);
                this.setDice(finalPosition, toMove);
                return true;
            }
        }
    }

}