package it.polimi.se2018;

import it.polimi.se2018.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

            fail("Test passed");
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
    public void testCheckAdjacentColour()
    {
        dicePattern.setDice(new Position(1,1), new Dice(Colour.BLUE));
        dicePattern.setDice(new Position(2,2), new Dice(Colour.RED));

        assertFalse(dicePattern.checkAdjacentColour(new Position(1,2 ), new Dice(Colour.RED)));
        assertTrue(dicePattern.checkAdjacentColour(new Position(1,2 ), new Dice(Colour.GREEN)));
        assertTrue(dicePattern.checkAdjacentColour(new Position(0,0), new Dice(Colour.YELLOW)));

        //deletion of side effects
        dicePattern.removeDice(new Position(1,1));
        dicePattern.removeDice(new Position(2,2));
    }




}
