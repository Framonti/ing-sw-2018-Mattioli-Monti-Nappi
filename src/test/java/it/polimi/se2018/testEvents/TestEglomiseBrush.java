package it.polimi.se2018.testEvents;

import it.polimi.se2018.events.vcevent.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the EglomiseBrushEvent
 * @author Framonti
 */
public class TestEglomiseBrush {

    /**
     * Tests the constructor and all the getter methods
     */
    @Test
    public void testGetters() {
        try {

            EglomiseBrushEvent eglomiseBrushEvent = new EglomiseBrushEvent("1 2 2 3");
            assertEquals(0, eglomiseBrushEvent.getInitialPosition().getX());
            assertEquals(1, eglomiseBrushEvent.getInitialPosition().getY());
            assertEquals(1, eglomiseBrushEvent.getFinalPosition().getX());
            assertEquals(2, eglomiseBrushEvent.getFinalPosition().getY());
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
            EglomiseBrushEvent eglomiseBrushEvent = new EglomiseBrushEvent("1 3 3");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Parametri insufficienti", e.getMessage());
        }

        try {
            EglomiseBrushEvent eglomiseBrushEvent = new EglomiseBrushEvent("1 ciao 4 3");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Parametri non numerici o sbagliati", e.getMessage());
        }
    }
}


