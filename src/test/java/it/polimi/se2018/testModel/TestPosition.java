package it.polimi.se2018.testModel;

import it.polimi.se2018.model.Position;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Tests for the Position Class
 * @author Framonti
 */
public class TestPosition {

    private Position p00 = new Position(0,0);
    private Position p01 = new Position(0,1);
    private Position p11 = new Position(1,1);
    private Position p10 = new Position(1,0);
    private Position p24 = new Position(2,4);
    private Position p13 = new Position(1,3);
    private Position p12 = new Position(1,2);
    private Position p21 = new Position(2,1);
    private Position p31 = new Position(3,1);
    private Position p32 = new Position(3,2);
    private Position p33 = new Position(3,3);
    private Position p23 = new Position(2,3);
    private Position p02 = new Position(0,2);
    private Position p03 = new Position(0,3);
    private Position p22 = new Position(2,2);

    //Support method
    private boolean containsAllEquals(List<Position> list1, List<Position> list2) {
        int i;
        int j;
        for(i = 0; i< list1.size();) {

            for(j = 0; j < list2.size(); j++) {

                if(list1.get(i).getY() == list2.get(j).getY() && list1.get(i).getX() == list2.get(j).getX()) {
                    i++;
                    j = 0;
                }
                if(i == list1.size())
                    return true;
            }
            if(j == list2.size())
                break;
        }
        return false;
    }

    /**
     * Checks that a Position is correctly created
     */
    @Test
    public void testPositionConstructedCorrectly() {

        try {
            assertEquals(2, p24.getX());
            assertEquals(4, p24.getY());
            assertEquals(1, p13.getX());
            assertEquals(3, p13.getY());
        }
        catch (IllegalArgumentException e){

            fail("Test failed");
        }
    }

    /**
     * Checks that the Position constructor raises an IllegalArgumentException
     */
    @Test
    public void testPositionConstructedIncorrectly(){

        Position invalidPosition;
        try{

            invalidPosition = new Position(-1, 7);
        }
        catch (IllegalArgumentException e) {

            assertEquals("Invalid position", e.getMessage());
        }
    }

    /**
     * Checks that the Position constructor raises an IllegalArgumentException
     */
    @Test
    public void testPositionConstructedIncorrectly2(){

        Position invalidPosition;
        try{

            invalidPosition = new Position(9, 1);
        }
        catch (IllegalArgumentException e) {

            assertEquals("Invalid position", e.getMessage());
        }
    }

    /**
     * Checks that the Position constructor raises an IllegalArgumentException
     */
    @Test
    public void testPositionConstructedIncorrectly3(){

        Position invalidPosition;
        try{

            invalidPosition = new Position(1, -9);
        }
        catch (IllegalArgumentException e) {

            assertEquals("Invalid position", e.getMessage());
        }
    }

    /**
     * Checks that the Position constructor raises an IllegalArgumentException
     */
    @Test
    public void testPositionConstructedIncorrectly4(){

        Position invalidPosition;
        try{

            invalidPosition = new Position(1, 9);
        }
        catch (IllegalArgumentException e) {

            assertEquals("Invalid position", e.getMessage());
        }
    }

    /**
     * Tests the getOrthogonalAdjacentPosition method
     */
    @Test
    public void testGetOrthogonalAdjacentPosition() {

        Position[] arrayPosition = {p01, p10};
        List<Position> positionList = p00.getOrthogonalAdjacentPositions();

        Position[] arrayPosition1 = {p21, p23, p12, p32};
        List<Position> positionList1 = p22.getOrthogonalAdjacentPositions();

        Position[] arrayPosition2 = {p01, p12, p03};
        List<Position> positionList2 = p02.getOrthogonalAdjacentPositions();

        assertTrue(containsAllEquals(positionList, Arrays.asList(arrayPosition)));
        assertTrue(containsAllEquals(positionList1, Arrays.asList(arrayPosition1)));
        assertTrue(containsAllEquals(positionList2, Arrays.asList(arrayPosition2)));
    }

    /**
     * Tests the getAdjacentPosition method
     */
    @Test
    public void testGetAdjacentPosition() {

        Position[] arrayPosition = {p01, p11, p10};
        List<Position> positionList = p00.getAdjacentPositions();

        Position[] arrayPosition1 = {p21, p23, p13, p12, p32, p11, p13, p31, p33};
        List<Position> positionList1 = p22.getAdjacentPositions();

        Position[] arrayPosition2 = {p01, p11, p12, p13, p03};
        List<Position> positionList2 = p02.getAdjacentPositions();

        assertTrue(containsAllEquals(positionList, Arrays.asList(arrayPosition)));
        assertTrue(containsAllEquals(positionList1, Arrays.asList(arrayPosition1)));
        assertTrue(containsAllEquals(positionList2, Arrays.asList(arrayPosition2)));
    }

}


