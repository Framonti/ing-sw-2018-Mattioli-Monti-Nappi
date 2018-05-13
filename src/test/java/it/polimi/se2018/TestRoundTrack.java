package it.polimi.se2018;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.RoundTrack;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestRoundTrack {

    private RoundTrack roundTrack;
    private Dice dice;
    private List<Dice> dices;

    @Before
    public void roundTrackBuilder() {
        roundTrack = new RoundTrack();
        dice = new Dice(Colour.PURPLE);
        dices = new ArrayList<>();
    }

    @Test
    public void testAddDiceTrue() {
        roundTrack.addDices(dices);
        roundTrack.addDice(0, dice);

        assertEquals(dice, roundTrack.getDice(0, 0));
    }

    @Test
    public void testAddDiceFalse() {
        roundTrack.addDices(dices);
        roundTrack.addDice(0, dice);
        Dice dice2 = new Dice(Colour.GREEN);
        roundTrack.addDice(0, dice2);

        assertNotEquals(dice, roundTrack.getDice(0, 1));
    }

    @Test
    public void testAddDicesTrue() {
        dices.add(dice);
        roundTrack.addDices(dices);

        assertEquals(dice, roundTrack.getDice(0, 0));
    }

    @Test
    public void testAddDicesFalse() {
        Dice dice2 = new Dice(Colour.GREEN);
        dices.add(dice);
        dices.add(dice2);
        roundTrack.addDices(dices);

        assertNotEquals(dice, roundTrack.getDice(0, 1));
    }

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

    @Test
    public void testSumDiceValueFalse() {
        Dice dice2 = new Dice(Colour.PURPLE);
        Dice dice3 = new Dice(Colour.PURPLE);
        dice.roll();
        dice2.roll();
        dice3.roll();
        List<Dice> dices2 = new ArrayList<>();
        List<Dice> dices3 = new ArrayList<>();

        dices.add(dice2);
        dices.add(dice3);
        dices2.add(dice);
        dices3.add(dice);
        dices3.add(dice3);

        roundTrack.addDices(dices);
        roundTrack.addDices(dices2);
        roundTrack.addDices(dices3);

        assertNotEquals(2*dice.getValue() + dice2.getValue() + dice3.getValue(), roundTrack.sumDiceValue());
    }
}
