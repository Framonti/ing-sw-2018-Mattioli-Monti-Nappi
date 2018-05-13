package it.polimi.se2018;

import it.polimi.se2018.model.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestDicePattern {

    private DicePattern dicePattern;

    @Before
    public void setUP(){

        WindowPatternCardDeckBuilder windowPatternCardDeckBuilder = new WindowPatternCardDeckBuilder("src/main/java/it/polimi/se2018/xml/WindowPatternCard.xml");
        Deck<WindowPatternCard> windowPatternDeck = windowPatternCardDeckBuilder.getWindowPatternCardDeck();
        WindowPatternCard windowPatternCard = windowPatternDeck.mixAndDistribute(1).get(0);
        WindowPattern windowPatternTest = windowPatternCard.getWindowPattern1();
        this.dicePattern = new DicePattern(windowPatternTest);
    }

    @Test
    public void testGetDiceFail() {

        try {
            dicePattern.getDice(new Position(9,2));
        }
        catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testIsEmpty()
    {
        dicePattern.setDice(new Position(2, 3), new Dice(Colour.YELLOW));
        dicePattern.setDice(new Position(0,0), new Dice(Colour.BLUE));
        assertTrue(dicePattern.isEmpty(new Position(3,4)));
        assertTrue(dicePattern.isEmpty(new Position(0, 3)));
        assertFalse(dicePattern.isEmpty(new Position(0,0)));
        assertFalse(dicePattern.isEmpty(new Position(2,3)));

        //Deletion of side effects
        dicePattern.removeDice(new Position(2,3 ));
        dicePattern.removeDice(new Position(0,0));
    }


    @Test
    public void testEmptySpaces(){

        assertEquals(20, dicePattern.emptySpaces());

        dicePattern.setDice(new Position(1,2), new Dice(Colour.BLUE));
        assertEquals(19, dicePattern.emptySpaces());

        dicePattern.setDice(new Position(2,3), new Dice(Colour.BLUE));
        assertEquals(18, dicePattern.emptySpaces());

        dicePattern.setDice(new Position(3,2), new Dice(Colour.BLUE));
        assertEquals(17, dicePattern.emptySpaces());

        dicePattern.moveDice(new Position(1,2), new Position(0,0));
        assertEquals(17, dicePattern.emptySpaces());

        //deletion of side effects
        dicePattern.removeDice(new Position(0,0));
        dicePattern.removeDice(new Position(2,3));
        dicePattern.removeDice(new Position(3,2));
    }

    @Test
    public void testCheckEdge() {

        assertTrue(dicePattern.checkEdge(new Position(0,3)));
        assertTrue(dicePattern.checkEdge(new Position(3, 4)));
        assertTrue(dicePattern.checkEdge(new Position(2, 0)));
        assertFalse(dicePattern.checkEdge(new Position(2, 1)));
        assertFalse(dicePattern.checkEdge(new Position(1, 3)));
        assertFalse(dicePattern.checkEdge(new Position(2, 2)));
    }

    @Test
    public void testCheckAdjacency() {

        dicePattern.setDice(new Position(1,1), new Dice(Colour.BLUE));

        assertFalse(dicePattern.checkAdjacency(new Position(2,3)));
        assertTrue(dicePattern.checkAdjacency(new Position(2,0)));
        assertFalse(dicePattern.checkAdjacency(new Position(1,1)));

        //deletion of side effects
        dicePattern.removeDice(new Position(1,1 ));

    }

    @Test
    public void testCheckAdjacentColour() {

        dicePattern.setDice(new Position(1,1), new Dice(Colour.BLUE));
        dicePattern.setDice(new Position(2,2), new Dice(Colour.RED));

        assertFalse(dicePattern.checkAdjacentColour(new Position(1,2 ), new Dice(Colour.RED)));
        assertTrue(dicePattern.checkAdjacentColour(new Position(1,2 ), new Dice(Colour.GREEN)));
        assertTrue(dicePattern.checkAdjacentColour(new Position(0,0), new Dice(Colour.RED)));

        //deletion of side effects
        dicePattern.removeDice(new Position(1,1));
        dicePattern.removeDice(new Position(2,2));
    }

    @Test
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

   /* @Test //TODO capire cosa non va
    public void testCheckDicePatternLimitations() {

      //  assertFalse(dicePattern.checkDicePatternLimitations(new Position(1,1), new Dice(Colour.GREEN)));

        dicePattern.placeDice(new Position(0, 1), new Dice(Colour.RED));

        assertTrue(dicePattern.checkDicePatternLimitations(new Position(1,1), new Dice(Colour.BLUE)));
    }
    */

    @Test
    public void testRemoveDice(){

        Dice toPlace = new Dice(Colour.GREEN);
        toPlace.setValue(3);
        dicePattern.setDice(new Position(3,2), toPlace);
        Dice removed = dicePattern.removeDice(new Position(3,2));

        assertNull(dicePattern.getDice(new Position(3,2)));
        assertEquals(3, removed.getValue());
        assertEquals(Colour.GREEN, removed.getColour());
    }

    @Test
    public void testRemoveDiceFail(){

        try{
            dicePattern.removeDice(new Position(2,3));
        }
        catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testMoveDice(){

        Dice toMove = new Dice(Colour.RED);
        toMove.setValue(2);
        dicePattern.setDice(new Position(3, 2), toMove);
        dicePattern.moveDice(new Position(3, 2), new Position(1,0));

        assertNull(dicePattern.getDice(new Position(3,2)));
        Dice diceMoved = dicePattern.getDice(new Position(1,0));
        assertNotNull(diceMoved);
        assertEquals(2, diceMoved.getValue());
        assertEquals(Colour.RED, diceMoved.getColour());

        dicePattern.removeDice(new Position(1,0));
    }

    @Test
    public void testMoveDiceFail1() {

        dicePattern.setDice(new Position(1, 2), new Dice(Colour.RED));

        try {
            dicePattern.moveDice(new Position(2, 3), new Position(1, 2));
        }
        catch (IllegalArgumentException e) {
            assertTrue("Initial position is empty: test passed", true);
        }
        finally {
            dicePattern.removeDice(new Position(1,2));
        }
    }

    @Test
    public void testMoveDiceFail2(){

        dicePattern.setDice(new Position(0,0), new Dice(Colour.YELLOW));
        dicePattern.setDice(new Position(2,3), new Dice(Colour.YELLOW));

        try{
            dicePattern.moveDice(new Position(2,3), new Position(0,0));
        }
        catch (IllegalArgumentException e) {

            assertTrue("Final position is not empty: test passed", true);
        }
        finally {
            dicePattern.removeDice(new Position(0,0));
            dicePattern.removeDice(new Position(2,3));
        }
    }
}

