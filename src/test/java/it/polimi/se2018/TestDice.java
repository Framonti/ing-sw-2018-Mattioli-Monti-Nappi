package it.polimi.se2018;

import it.polimi.se2018.model.Dice;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestDice {

    @Test
    public void testSubOneTrue(){
        Dice dice = new Dice(3);

        assertTrue(dice.subOne());
        assertEquals(2, dice.getValue());
    }

    @Test
    public void testSubOneFalse(){
        Dice dice = new Dice(1);

        assertFalse(dice.subOne());
        assertEquals(1, dice.getValue());
    }

    @Test
    public void testAddOneTrue(){
        Dice dice = new Dice(4);

        assertTrue(dice.addOne());
        assertEquals(5, dice.getValue());
    }

    @Test
    public void testAddOneFalse(){
        Dice dice = new Dice(6);

        assertFalse(dice.addOne());
        assertEquals(6, dice.getValue());
    }

    @Test
    public void testTurn() {

        Dice dice1 = new Dice(1);
        Dice dice6 = new Dice(6);

        dice1.turn();
        dice6.turn();

        assertEquals(6, dice1.getValue());
        assertEquals(1, dice6.getValue());
    }

}
