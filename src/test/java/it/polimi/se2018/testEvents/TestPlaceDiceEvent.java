package it.polimi.se2018.testEvents;

import it.polimi.se2018.events.vcevent.PlaceDiceEvent;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the PlaceDiceEvent class
 * @author Framonti
 */
public class TestPlaceDiceEvent {

    /**
     * Tests that a PlaceDiceEvent object is correctly constructed
     */
    @Test
    public void testPlaceDiceEventConstructor() {

        try{
            PlaceDiceEvent placeDiceEventToTest = new PlaceDiceEvent("2 3 4");
            assertEquals(1, placeDiceEventToTest.getDiceIndexDraftPool());
            assertEquals(2, placeDiceEventToTest.getPositionToPlace().getX());
            assertEquals(3, placeDiceEventToTest.getPositionToPlace().getY());
        }
        catch (IllegalArgumentException e){
            fail();
        }
    }

    /**
     * Checks that PlaceDiceEvent raises an IllegalArgumentException if there aren't enough arguments
     */
    @Test
    public void testPlaceDiceEventConstructorException1(){

        try{
            PlaceDiceEvent placeDiceEventToTest = new PlaceDiceEvent("2 3");
        }
        catch (IllegalArgumentException e) {

            assertEquals("Parametri insufficienti", e.getMessage());
        }
    }

    /**
     * Checks that PlaceDiceEvent raises an IllegalArgumentException if some parameters are illegal
     */
    @Test
    public void testPlaceDiceEventConstructorException2(){

        try{
            PlaceDiceEvent placeDiceEventToTest = new PlaceDiceEvent("2 3 8");
        }
        catch (IllegalArgumentException e) {

            assertEquals("Parametri non numerici o sbagliati", e.getMessage());
        }
    }

    /**
     * Checks that PlaceDiceEvent raises an IllegalArgumentException if some parameters are not number
     */
    @Test
    public void testPlaceDiceEventConstructorException3(){

        try{
            PlaceDiceEvent placeDiceEventToTest = new PlaceDiceEvent("2 notNumber 2");
        }
        catch (IllegalArgumentException e) {

            assertEquals("Parametri non numerici o sbagliati", e.getMessage());
        }
    }
    
}
