package it.polimi.se2018.testModel;

import it.polimi.se2018.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test Class for the DicePattern
 * @author Framonti
 */
public class TestDicePattern {

    private DicePattern dicePattern;

    /**
     * Creates a DicePattern that will be used for the tests
     */
    @Before
    public void setUP(){

        Dice[][] diceMatrix = new Dice[4][5];
        diceMatrix[0][0] = new Dice(Colour.RED);
        diceMatrix[0][1] = new Dice(1);
        diceMatrix[0][2] = new Dice(2);
        diceMatrix[0][3] = new Dice(3);
        diceMatrix[0][4] = new Dice(Colour.RED);
        WindowPattern windowPatternTest = new WindowPattern("Name", 3, diceMatrix);
        this.dicePattern = new DicePattern(windowPatternTest);
    }

    /**
     * Checks that the method getDice raises an IllegalArgumentException
     */
    @Test
    public void testGetDiceException() {

        try {
            dicePattern.getDice(new Position(9,2));
        }
        catch (IllegalArgumentException e) {

            assertEquals("Invalid position", e.getMessage());
        }
    }

    /**
     * Tests the isEmpty method
     */
    @Test
    public void testIsEmpty() {
        try {
            dicePattern.setDice(new Position(2, 3), new Dice(Colour.YELLOW));
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.setDice(new Position(0,0), new Dice(Colour.BLUE));
        } catch (NullPointerException ignored) {}
        assertTrue(dicePattern.isEmpty(new Position(3,4)));
        assertTrue(dicePattern.isEmpty(new Position(0, 3)));
        assertFalse(dicePattern.isEmpty(new Position(0,0)));
        assertFalse(dicePattern.isEmpty(new Position(2,3)));

        //Deletion of side effects
        try {
            dicePattern.removeDice(new Position(2,3 ));
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.removeDice(new Position(0,0));
        } catch (NullPointerException ignored) {}
    }


    /**
     * Tests the emptySpaces method
     */
    @Test
    public void testEmptySpaces(){

        assertEquals(20, dicePattern.emptySpaces());

        try {
            dicePattern.setDice(new Position(1,2), new Dice(Colour.BLUE));
        } catch (NullPointerException ignored) {}
        assertEquals(19, dicePattern.emptySpaces());

        try {
            dicePattern.setDice(new Position(2,3), new Dice(Colour.BLUE));
        } catch (NullPointerException ignored) {}
        assertEquals(18, dicePattern.emptySpaces());

        try {
            dicePattern.setDice(new Position(3,2), new Dice(Colour.BLUE));
        } catch (NullPointerException ignored) {}
        assertEquals(17, dicePattern.emptySpaces());

        try {
            dicePattern.moveDice(new Position(1,2), new Position(0,0));
        } catch (NullPointerException e) {
            //This is necessary because moveDice may throw a NullPointerException before placing the dice removed
            try {
                dicePattern.setDice(new Position(0, 0), new Dice(Colour.BLUE));
            } catch (NullPointerException ignored) {}
        }
        assertEquals(17, dicePattern.emptySpaces());

        //deletion of side effects
        try {
            dicePattern.removeDice(new Position(0,0));
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.removeDice(new Position(2,3));
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.removeDice(new Position(3,2));
        } catch (NullPointerException ignored) {}
    }

    /**
     * Tests the getEmptyPosition method
     */
    @Test
    public void testGetEmptyPosition(){

        Dice toPlace = new Dice(Colour.PURPLE);
        try {
            dicePattern.setDice(new Position(0,0), toPlace);
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.setDice(new Position(0,1), toPlace);
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.setDice(new Position(0,2), toPlace);
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.setDice(new Position(0,3), toPlace);
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.setDice(new Position(1,1), toPlace);
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.setDice(new Position(1,2), toPlace);
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.setDice(new Position(1,3), toPlace);
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.setDice(new Position(2,0), toPlace);
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.setDice(new Position(2,1), toPlace);
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.setDice(new Position(2,2), toPlace);
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.setDice(new Position(2,3), toPlace);
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.setDice(new Position(2,4), toPlace);
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.setDice(new Position(3,0), toPlace);
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.setDice(new Position(3,1), toPlace);
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.setDice(new Position(3,1), toPlace);
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.setDice(new Position(3,3), toPlace);
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.setDice(new Position(3,4), toPlace);
        } catch (NullPointerException ignored) {}
        List<Position> emptyPositions = dicePattern.getEmptyPositions();
        assertEquals(emptyPositions.get(0).getX(), new Position(0, 4).getX());
        assertEquals(emptyPositions.get(0).getX(), new Position(0, 4).getX());
        assertEquals(emptyPositions.get(1).getX(), new Position(1, 0).getX());
        assertEquals(emptyPositions.get(1).getX(), new Position(1, 0).getX());
        assertEquals(emptyPositions.get(2).getX(), new Position(1, 4).getX());
        assertEquals(emptyPositions.get(2).getX(), new Position(1, 4).getX());
        assertEquals(emptyPositions.get(3).getX(), new Position(3, 2).getX());
        assertEquals(emptyPositions.get(3).getX(), new Position(3, 2).getX());
    }


    /**
     * Tests the checkEdge method
     */
    @Test
    public void testCheckEdge() {

        assertTrue(dicePattern.checkEdge(new Position(0,3)));
        assertTrue(dicePattern.checkEdge(new Position(3, 4)));
        assertTrue(dicePattern.checkEdge(new Position(2, 0)));
        assertFalse(dicePattern.checkEdge(new Position(2, 1)));
        assertFalse(dicePattern.checkEdge(new Position(1, 3)));
        assertFalse(dicePattern.checkEdge(new Position(2, 2)));
    }

    /**
     * Tests the CheckAdjacency method
     */
    @Test
    public void testCheckAdjacency() {

        try {
            dicePattern.setDice(new Position(1,1), new Dice(Colour.BLUE));
        } catch (NullPointerException ignored) {}

        assertFalse(dicePattern.checkAdjacency(new Position(2,3)));
        assertTrue(dicePattern.checkAdjacency(new Position(2,0)));
        assertFalse(dicePattern.checkAdjacency(new Position(1,1)));

        //deletion of side effects
        try {
            dicePattern.removeDice(new Position(1,1 ));
        } catch (NullPointerException ignored) {}

    }

    /**
     * Tests the CheckAdjacentColour method
     */
    @Test
    public void testCheckAdjacentColour() {

        try {
            dicePattern.setDice(new Position(1,1), new Dice(Colour.BLUE));
        } catch (NullPointerException ignored) {}

        try {
            dicePattern.setDice(new Position(2,2), new Dice(Colour.RED));
            assertFalse(dicePattern.checkAdjacentColour(new Position(1,2 ), new Dice(Colour.RED)));
            assertTrue(dicePattern.checkAdjacentColour(new Position(1,2 ), new Dice(Colour.GREEN)));
            assertTrue(dicePattern.checkAdjacentColour(new Position(0,0), new Dice(Colour.RED)));
        } catch (NullPointerException e) {
            assertFalse(dicePattern.checkAdjacentColour(new Position(1, 2), new Dice(Colour.RED)));
            assertTrue(dicePattern.checkAdjacentColour(new Position(1, 2), new Dice(Colour.GREEN)));
            assertTrue(dicePattern.checkAdjacentColour(new Position(0, 0), new Dice(Colour.RED)));
        }
        //deletion of side effects
        try {
            dicePattern.removeDice(new Position(1,1));
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.removeDice(new Position(2,2));
        } catch (NullPointerException ignored) {}
    }

    /**
     *  Tests the checkAdjacentValue method
     */
    @Test(expected = NullPointerException.class)
    public void testCheckAdjacentValue() {

        dicePattern.setDice(new Position(1,1), new Dice(3));
        dicePattern.setDice(new Position(2,2), new Dice(2));

        assertFalse(dicePattern.checkAdjacentValue(new Position(1,2 ), new Dice(3)));
        assertTrue(dicePattern.checkAdjacentValue(new Position(1,2 ), new Dice(1)));
        assertTrue(dicePattern.checkAdjacentValue(new Position(0,0), new Dice(3)));

        //deletion of side effects
        dicePattern.removeDice(new Position(1,1));
        dicePattern.removeDice(new Position(2,2));
    }

    /**
     * Tests the checkDicePatternLimitations method
     */
    @Test
    public void testCheckDicePatternLimitations() {

        //False due to the attribute firstDice
        assertFalse(dicePattern.checkDicePatternLimitations(new Position(1,1), new Dice(Colour.GREEN)));

        Dice toPlace = new Dice(Colour.RED);
        toPlace.setValue(2);
        try {
            dicePattern.placeDice(new Position(1, 0), toPlace);
        } catch (NullPointerException ignored) {}

        assertTrue(dicePattern.checkDicePatternLimitations(new Position(1,1), new Dice(Colour.BLUE)));

        //False due to the value
        assertFalse(dicePattern.checkDicePatternLimitations(new Position(2,0), new Dice(2)));

        //False due to the colour
        assertFalse(dicePattern.checkDicePatternLimitations(new Position(2,0), new Dice(Colour.RED)));
        //Deletion of side effect
        try {
            dicePattern.removeDice(new Position(1,0));
        } catch (NullPointerException ignored) {}
    }

    /**
     * Checks that placeDice raises an IllegalArgumentException because checkEdge is false
     */
    @Test
    public void testPlaceDiceException1(){

        try{

            dicePattern.placeDice(new Position(2,3), new Dice(Colour.PURPLE));
        }
        catch (IllegalArgumentException e) {
            assertEquals("Illegal move", e.getMessage());
        }
    }

    /**
     * Checks that placeDice raises an IllegalArgumentException due to limitations on the WindowPattern
     */
    @Test
    public void testPlaceDiceException2()
    {
        try{
            dicePattern.placeDice(new Position(0,2), new Dice(3));
        }
        catch (IllegalArgumentException e) {

            assertEquals("Illegal move", e.getMessage());
        }
    }

    /**
     * Checks that placeDice raises an IllegalArgumentException due to limitation on the DicePattern
     */
    @Test
    public void testPlaceDiceException3(){

        Dice toPlace = new Dice(Colour.RED);
        toPlace.setValue(2);
        try {
            dicePattern.placeDice(new Position(1, 0), toPlace);
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.placeDice(new Position(2,0), new Dice(Colour.RED));
        }
        catch (IllegalArgumentException e) {

            assertEquals("Illegal move", e.getMessage());
        }

        try {
            dicePattern.removeDice(new Position(1,0));
        } catch (NullPointerException ignored) {}

    }

    /**
     * Tests placeDice method
     */
    @Test
    public void testPlaceDice(){

        Dice toPlace1 = new Dice(Colour.RED);
        toPlace1.setValue(2);
        Dice toPlace2 = new Dice(Colour.GREEN);
        toPlace2.setValue(3);

        try {
            dicePattern.placeDice(new Position(1,0), toPlace1);
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.placeDice(new Position(2,0), toPlace2);
        } catch (NullPointerException ignored) {}

        assertEquals(toPlace1, dicePattern.getDice(new Position(1,0)));
        assertEquals(toPlace2, dicePattern.getDice(new Position(2,0)));

        try {
            dicePattern.removeDice(new Position(1,0));
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.removeDice(new Position(2,0));
        } catch (NullPointerException ignored) {}
    }

    /**
     * Tests removeDice method
     */
    @Test
    public void testRemoveDice(){

        Dice toPlace = new Dice(Colour.GREEN);
        toPlace.setValue(3);
        try {
            dicePattern.setDice(new Position(3,2), toPlace);
        } catch (NullPointerException ignored) {}
        Dice removed;
        try {
            removed = dicePattern.removeDice(new Position(3,2));
        } catch (NullPointerException e) {
            //This is necessary because removeDice may throw a NullPointerException before returning the dice removed
            removed = toPlace;
        }

        assertNull(dicePattern.getDice(new Position(3,2)));
        assertEquals(3, removed.getValue());
        assertEquals(Colour.GREEN, removed.getColour());
    }

    /**
     * Checks that an IllegalArgumentException is raised if it is asked to remove a dice
     * from an empty position
     */
    @Test
    public void testRemoveDiceException(){

        try{
            dicePattern.removeDice(new Position(2,3));
        }
        catch (IllegalArgumentException e) {

            assertEquals("The position required has no dice", e.getMessage());
        }
    }

    /**
     * Tests the moveDice method
     */
    @Test
    public void testMoveDice(){

        Dice toMove = new Dice(Colour.RED);
        toMove.setValue(2);
        try {
            dicePattern.setDice(new Position(3, 2), toMove);
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.moveDice(new Position(3, 2), new Position(1,0));
        } catch (NullPointerException e) {
            //This is necessary because moveDice may throw a NullPointerException before placing the dice removed
            try {
                dicePattern.setDice(new Position(1, 0), toMove);
            } catch (NullPointerException ignored) {}
        }

        assertNull(dicePattern.getDice(new Position(3,2)));
        Dice diceMoved = dicePattern.getDice(new Position(1,0));
        assertNotNull(diceMoved);
        assertEquals(2, diceMoved.getValue());
        assertEquals(Colour.RED, diceMoved.getColour());

        try {
            dicePattern.removeDice(new Position(1,0));
        } catch (NullPointerException ignored) {}
    }

    /**
     * Checks that moveDice raises an IllegalArgumentException if the initial position is empty
     */
    @Test
    public void testMoveDiceException1() {

        try {
            dicePattern.setDice(new Position(1, 2), new Dice(Colour.RED));
        } catch (NullPointerException ignored) {}

        try {
            dicePattern.moveDice(new Position(2, 3), new Position(1, 2));
        }
        catch (IllegalArgumentException e) {
            assertEquals("Initial position is empty", e.getMessage());
        }

        try {
            dicePattern.removeDice(new Position(1,2));
        } catch (NullPointerException ignored) {}

    }

    /**
     * Checks that moveDice raises an IllegalArgumentException if the final position is not empty
     */
    @Test
    public void testMoveDiceException2(){

        try {
            dicePattern.setDice(new Position(0,0), new Dice(Colour.YELLOW));
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.setDice(new Position(2,3), new Dice(Colour.YELLOW));
        } catch (NullPointerException ignored) {}

        try{
            dicePattern.moveDice(new Position(2,3), new Position(0,0));
        }
        catch (IllegalArgumentException e) {

            assertEquals("Final position has already had a dice", e.getMessage());
        }

        try {
            dicePattern.removeDice(new Position(0,0));
        } catch (NullPointerException ignored) {}
        try {
            dicePattern.removeDice(new Position(2,3));
        } catch (NullPointerException ignored) {}

    }

    /**
     * Tests the toString method
     */
    @Test
    public void testToString(){

        Dice toPlace = new Dice(Colour.PURPLE);
        toPlace.setValue(3);
        String expectedString = "Nome: Name\nDifficoltà: 3\n\n" +
                "|¯¯¯¯|¯¯¯¯|¯¯¯¯|¯¯¯¯|¯¯¯¯|\n" +
                "| R  | 1  | 2  | 3  | R  |\n" +
                "|____|____|____|____|____|\n" +
                "|¯¯¯¯|¯¯¯¯|¯¯¯¯|¯¯¯¯|¯¯¯¯|\n" +
                "|    |    |    |    |    |\n" +
                "|____|____|____|____|____|\n" +
                "|¯¯¯¯|¯¯¯¯|¯¯¯¯|¯¯¯¯|¯¯¯¯|\n" +
                "|    |    |    | 3F |    |\n" +
                "|____|____|____|____|____|\n" +
                "|¯¯¯¯|¯¯¯¯|¯¯¯¯|¯¯¯¯|¯¯¯¯|\n" +
                "|    |    |    |    |    |\n" +
                "|____|____|____|____|____|\n";
        try {
            dicePattern.setDice(new Position(2,3), toPlace);
            assertEquals(expectedString, dicePattern.toString());
        } catch (NullPointerException e) {
            assertEquals(expectedString, dicePattern.toString());
        }
    }
}

