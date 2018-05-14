package it.polimi.se2018;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.RoundTrack;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for the RoundTrack class
 * @author fabio
 */
public class TestRoundTrack {

    private RoundTrack roundTrack;
    private Dice dice;
    private List<Dice> dices;

    /**
     * Initializes a new round track, dice and a list of dices
     */
    @Before
    public void setUp() {
        roundTrack = new RoundTrack();
        dice = new Dice(Colour.PURPLE);
        dices = new ArrayList<>();
    }

    /**
     * Tests if the IndexOutOfBoundException is thrown in the getList method
     */
    @Test
    public void testGetListIndexOutOfBoundException() {
        boolean exc = false;
        try {
            roundTrack.getList(1);
        }
        catch (IndexOutOfBoundsException e) {
            exc = true;
        }
        assertTrue(exc);
    }

    /**
     * Tests if the IndexOutOfBoundException is thrown in the getDice method
     */
    @Test
    public void testGetDiceIndexOutOfBoundException() {
        boolean exc = false;
        roundTrack.addDices(dices);
        try {
            roundTrack.getDice(0, 3);
        }
        catch (IndexOutOfBoundsException e) {
            exc = true;
        }
        assertTrue(exc);
    }

    /**
     * Tests if the dice added is correct
     */
    @Test
    public void testAddDice() {
        roundTrack.addDices(dices);
        roundTrack.addDice(0, dice);

        assertEquals(dice, roundTrack.getDice(0, 0));
    }

    /**
     * Tests if the list of dices added is correct
     */
    @Test
    public void testAddDices() {
        dices.add(dice);
        roundTrack.addDices(dices);

        assertEquals(dice, roundTrack.getDice(0, 0));
    }

    /**
     * Initializes other 2 dices, roll them and adds them to the round track in different rounds
     * Checks that the sum of their value is correct
     */
    @Test
    public void testSumDiceValueTrue() {
        Dice dice2 = new Dice(Colour.PURPLE);
        Dice dice3 = new Dice(Colour.PURPLE);
        dice.roll();
        dice2.roll();
        dice3.roll();
        List<Dice> dices2 = new ArrayList<>();
        List<Dice> dices3 = new ArrayList<>();

        dices.add(dice);
        dices.add(dice3);
        dices2.add(dice2);
        dices3.add(dice2);
        dices3.add(dice3);

        roundTrack.addDices(dices);
        roundTrack.addDices(dices2);
        roundTrack.addDices(dices3);

        assertEquals(dice.getValue() + 2*dice2.getValue() + 2*dice3.getValue(), roundTrack.sumDiceValue());
    }

    /**
     * Adds a dice to the round track and tests if the String returned is correct
     */
    @Test
    public void testToString() {
        assertEquals("", roundTrack.roundToString(0));

        dice.roll();
        dices.add(dice);
        roundTrack.addDices(dices);

        assertEquals(dice.toString() + "\t", roundTrack.roundToString(0));
    }

}
