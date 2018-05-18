package it.polimi.se2018.testEvents;

import it.polimi.se2018.events.vcevent.FluxRemoverEvent;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class tests FluxRemoverEvent's methods
 * @author fabio
 */
public class TestFluxRemoverEvent {

    /**
     * Tests the constructor and all the getter methods
     */
    @Test
    public void testGetters() {
        FluxRemoverEvent fluxRemoverEvent = new FluxRemoverEvent("1 1 3 3 5 1 2 6");
        assertEquals(0, fluxRemoverEvent.getDiceIndex());
        assertEquals(0, fluxRemoverEvent.getDicePosition().getX());
        assertEquals(2, fluxRemoverEvent.getDicePosition().getY());
        assertEquals(3, fluxRemoverEvent.getYellowDiceValue());
        assertEquals(5, fluxRemoverEvent.getRedDiceValue());
        assertEquals(1, fluxRemoverEvent.getGreenDiceValue());
        assertEquals(2, fluxRemoverEvent.getPurpleDiceValue());
        assertEquals(6, fluxRemoverEvent.getBlueDiceValue());
    }

    /**
     * Tests if the constructor throws the correct exceptions
     */
    @Test
    public void testIllegalArgumentException() {
        try {
            FluxRemoverEvent fluxRemoverEvent = new FluxRemoverEvent("1 3 3 5 1 2 6");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Paramentri insufficienti", e.getMessage());
        }

        try {
            FluxRemoverEvent fluxRemoverEvent = new FluxRemoverEvent("1 ciao 4");
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
        FluxRemoverEvent fluxRemoverEvent = new FluxRemoverEvent("1 1 1 1 1 1 1 1");
        assertEquals("FluxRemoverEvent", fluxRemoverEvent.toString());
    }
}
