package it.polimi.se2018.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a support class for bidimensional arrays.
 * The attribute x represents the row index, while the attribute y the column index
 * @author Framonti
 */
public class Position implements Serializable{

    static final long serialVersionUID = 23L;

    private final int x;
    private final int y;

    /**
     * Constructor
     * It raises an IllegalArgumentException if the position don't respect the limitation of the bidimensional arrays of the game
     * @param x The row index
     * @param y The column index
     */
    public Position(int x, int y) {

        int rows = 4;
        int columns = 5;
        if (x < 0 || x > (rows -1) || y < 0 || y > (columns - 1))
            throw new IllegalArgumentException("Invalid position");
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the row of this position
     * @return The X attribute
     */
    public int getX() {

        return this.x;
    }

    /**
     * Gets the column of this position
     * @return The Y attribute
     */
    public int getY() {

        return this.y;
    }

    /**
     * Crate a List of Position that share an edge with a Position object
     * @return A List of positions that share am edge with a Position object
     */
    public List<Position> getOrthogonalAdjacentPositions() {

        int maxLimitX = 4;
        int maxLimitY = 5;
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

    /**
     * Crate a List of Position that share an edge or an angle with a Position object
     * @return a List of positions that share with this position an edge OR an angle
     */
    public List<Position> getAdjacentPositions() {

        int maxLimitX = 4;
        int maxLimitY = 5;
        List<Position> toReturn = this.getOrthogonalAdjacentPositions();

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
