package it.polimi.se2018.testEvents;

import it.polimi.se2018.events.vcevent.CorkBakedStraightedgeEvent;
import it.polimi.se2018.events.vcevent.GrindingStoneEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This class tests the GrindingStoneEvent Class
 * @author Framonti
 */
public class TestGrindingStoneEvent {

    /**
     * Tests the constructor and all the getter methods
     */
    @Test
    public void testGetters() {
        try{
            GrindingStoneEvent grindingStoneEvent = new GrindingStoneEvent("2");
            assertEquals(1, grindingStoneEvent.getDicePosition());
        }
        catch (IllegalArgumentException e){
            fail();
        }
    }

    /**
     * Tests if the constructor throws the correct exception
     */
    @Test
    public void testIllegalArgumentException() {
        try {
            GrindingStoneEvent grindingStoneEvent = new GrindingStoneEvent("notANumber");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Parametri non numerici o sbagliati", e.getMessage());
        }
    }
}
