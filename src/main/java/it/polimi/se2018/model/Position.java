package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.List;

/** This is a support class for bidimensional arrays.
 * The attribute x represents the row index, while the attribute y the column index
 * @author Framonti
 */
public class Position
{
    private final int x;
    private final int y;

    //Constructor
    /**
    * It raises an IllegalArgumentException if the position don't respect the limitation of the bidimensional arrays of the game */
    public Position(int x, int y) {

        int rows = 4;
        int colums = 5;
        if (x < 0 || x > (rows -1) || y < 0 || y > (colums - 1))
            throw new IllegalArgumentException("Invalid position");
        this.x = x;
        this.y = y;
    }

    //Getters
    public int getX() {

        return this.x;
    }

    public int getY() {

        return this.y;
    }

    /**Crate a List of Position that share an edge with a Position object
    *@return A List of positions that share am edge with a Position object*/
    public List<Position> getOrthogonalAdjacentPositions(int maxLimitX, int maxLimitY) {

        List<Position> toReturn = new ArrayList<>();

        if(this.y > 0)
            toReturn.add(new Position(this.x, this.y-1));

        if(this.y < maxLimitY-1)
            toReturn.add(new Position(this.x, this.y+1));

        if(this.x > 0)
            toReturn.add(new Position(this.x - 1, this.y));

        if(this.x < maxLimitX-1)
            toReturn.add(new Position(this.x + 1, this.y));

        return toReturn;
    }

    /**Crate a List of Position that share an edge or an angle with a Position object
    //@return a List of positions that share with this position an edge OR an angle */
    public List<Position> getAdjacentPositions(int maxLimitX, int maxLimitY) {

        List<Position> toReturn = this.getOrthogonalAdjacentPositions(maxLimitX, maxLimitY);

        if(this.x > 0) {
            if(this.y > 0)
                toReturn.add(new Position(this.x - 1, this.y-1));
            if(this.y < maxLimitY-1)
                toReturn.add(new Position(this.x - 1, this.y + 1));
        }

        if(this.x < maxLimitX-1) {
            if(this.y > 0)
                toReturn.add(new Position(this.x + 1, this.y -1));
            if(this.y < maxLimitY-1)
                toReturn.add(new Position(this.x + 1, this.y+1));
        }

        return toReturn;
    }
}