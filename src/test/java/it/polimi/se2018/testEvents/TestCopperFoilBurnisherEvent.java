package it.polimi.se2018.testEvents;

import it.polimi.se2018.events.vcevent.*;
import it.polimi.se2018.model.Position;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestCopperFoilBurnisherEvent {

    /**
     * Tests the constructor and all the getter methods
     */
    @Test
    public void testGetters() {
        CopperFoilBurnisherEvent copperFoilBurnisherEvent = new CopperFoilBurnisherEvent("1 2 3 1");
        assertEquals(0, copperFoilBurnisherEvent.getInitialPosition().getX());
        assertEquals(1, copperFoilBurnisherEvent.getInitialPosition().getY());
        assertEquals(2, copperFoilBurnisherEvent.getFinalPosition().getX());
        assertEquals(0, copperFoilBurnisherEvent.getFinalPosition().getY());

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

    /**
     * Tests if the string returned is correct
     */
    @Test
    public void testToString() {
        CopperFoilBurnisherEvent copperFoilBurnisherEvent = new CopperFoilBurnisherEvent("1 2 3 4");
        assertEquals("CopperFoilBurnisherEvent", copperFoilBurnisherEvent.toString());
    }
}
