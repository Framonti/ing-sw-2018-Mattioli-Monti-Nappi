package it.polimi.se2018;

import it.polimi.se2018.model.Dice;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the Dice Class
 */
public class TestDice {

    /**
     * Tests the subOne method
     */
    @Test
    public void testSubOne(){
        Dice dice = new Dice(3);
        dice.subOne();

        assertEquals(2, dice.getValue());
    }

    /**
     * Checks that the subOne method raises an IllegalArgumentException if the dice's value is 1
     */
    @Test
    public void testSubOneException(){
        Dice dice = new Dice(1);

        try{
            dice.subOne();
        }
        catch (IllegalArgumentException e) {
            assertEquals("Dice's value can't be decreased", e.getMessage());
        }
        finally {
            assertEquals(1, dice.getValue());
        }
    }

    /**
     * Tests the addOne method
     */
    @Test
    public void testAddOne(){
        Dice dice = new Dice(4);
        dice.addOne();

        assertEquals(5, dice.getValue());
    }

    /**
     * Checks that the addOne method raises an IllegalArgumentException if the dice's value is 6
     */
    @Test
    public void testAddOneException(){
        Dice dice = new Dice(6);
        try{
            dice.addOne();
        }
        catch (IllegalArgumentException e) {
            assertEquals("Dice's value can't be increased", e.getMessage());
        }
        finally {
            assertEquals(6, dice.getValue());
        }
    }

    /**
     * Test the turn method
     */
    @Test
    public void testTurn() {

        Dice dice1 = new Dice(1);
        Dice dice6 = new Dice(6);

        dice1.turn();
        dice6.turn();

        assertEquals(6, dice1.getValue());
        assertEquals(1, dice6.getValue());
    }

    /**
     * Checks that setValue raises an IllegalArgumentException if the parameter is more than 6
     */
    @Test
    public void testSetValueException(){

        Dice dice = new Dice(2);
        try {
            dice.setValue(7);
        }
        catch (IllegalArgumentException e) {

            assertEquals("Invalid value", e.getMessage());
        }
        finally {
            assertEquals(2, dice.getValue());
        }
    }

}
