package it.polimi.se2018;

import java.util.ArrayList;
import java.util.List;

public class Position
{
    private final int x;
    private final int y;

    //Constructor
    public Position(int x, int y) {

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

    //@return a List of positions that share with "this position" an edge
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

    //@return a List of positions that share with this position an edge OR an angle
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
