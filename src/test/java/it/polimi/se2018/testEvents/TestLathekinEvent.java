package it.polimi.se2018.testEvents;

import it.polimi.se2018.events.vcevent.EglomiseBrushEvent;
import it.polimi.se2018.events.vcevent.LathekinEvent;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test class for the EglomiseBrushEvent
 * @author Framonti
 */
public class TestLathekinEvent {

    /**
     * Tests the constructor and all the getter methods
     */
    @Test
    public void testGetters() {
        try {

            LathekinEvent lathekinEvent = new LathekinEvent("1 2 2 3 3 1 4 5");
            assertEquals(0, lathekinEvent.getInitialPosition1().getX());
            assertEquals(1, lathekinEvent.getInitialPosition1().getY());
            assertEquals(1, lathekinEvent.getFinalPosition1().getX());
            assertEquals(2, lathekinEvent.getFinalPosition1().getY());
            assertEquals(2, lathekinEvent.getInitialPosition2().getX());
            assertEquals(0, lathekinEvent.getInitialPosition2().getY());
            assertEquals(3, lathekinEvent.getFinalPosition2().getX());
            assertEquals(4, lathekinEvent.getFinalPosition2().getY());
        }
        catch (IllegalArgumentException e) {
            fail();
        }
    }

    /**
     * Tests if the constructor throws the correct exceptions
     */
    @Test
    public void testIllegalArgumentException() {
        try{
            LathekinEvent lathekinEvent = new LathekinEvent("1 3 3 2 1 4");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Parametri insufficienti", e.getMessage());
        }

        try {
            LathekinEvent lathekinEvent = new LathekinEvent("1 4 3 6 1 1 1 1");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Parametri non numerici o sbagliati", e.getMessage());
        }
    }
}
