package it.polimi.se2018.testEvents;

import it.polimi.se2018.events.vcevent.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the FluxBrushEvent
 * @author Framonti
 */

public class TestFluxBrushEvent {

    /**
     * Tests the constructor and all the getter methods
     */
    @Test
    public void testGetters() {
        try {

            FluxBrushEvent fluxBrushEvent = new FluxBrushEvent("2 2 3");
            assertEquals(1, fluxBrushEvent.getDiceIndexInDraftPool());
            assertEquals(1, fluxBrushEvent.getFinalPosition().getX());
            assertEquals(2, fluxBrushEvent.getFinalPosition().getY());
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
            FluxBrushEvent fluxBrushEvent = new FluxBrushEvent("1 3");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Parametri insufficienti", e.getMessage());
        }
        try {
            FluxBrushEvent fluxBrushEvent = new FluxBrushEvent("1 ciao 4 3");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Parametri non numerici o sbagliati", e.getMessage());
        }
    }

}
