package it.polimi.se2018.testEvents;

import it.polimi.se2018.events.vcevent.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test class for the CopperFoilBurnisherEvent
 * @author Framonti
 */
public class TestCopperFoilBurnisherEvent {

    /**
     * Tests the constructor and all the getter methods
     */
    @Test
    public void testGetters() {
        try {
            CopperFoilBurnisherEvent copperFoilBurnisherEvent = new CopperFoilBurnisherEvent("1 2 3 1");
            assertEquals(0, copperFoilBurnisherEvent.getInitialPosition().getX());
            assertEquals(1, copperFoilBurnisherEvent.getInitialPosition().getY());
            assertEquals(2, copperFoilBurnisherEvent.getFinalPosition().getX());
            assertEquals(0, copperFoilBurnisherEvent.getFinalPosition().getY());
        }
        catch (IllegalArgumentException e){
            fail();
        }

    }

    /**
     * Tests if the constructor throws the correct exceptions
     */
    @Test
    public void testIllegalArgumentException() {
        try {
            CopperFoilBurnisherEvent copperFoilBurnisherEvent = new CopperFoilBurnisherEvent("1 3 3");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Parametri insufficienti", e.getMessage());
        }

        try {
            CopperFoilBurnisherEvent copperFoilBurnisherEvent = new CopperFoilBurnisherEvent("1 ciao 4");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Parametri non numerici o sbagliati", e.getMessage());
        }
    }
}
