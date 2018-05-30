package it.polimi.se2018.testEvents;

import it.polimi.se2018.events.vcevent.FluxRemoverChooseDiceEvent;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class tests FluxRemoverChooseDiceEvent's methods
 * @author fabio
 */
public class TestFluxRemoverChooseDiceEvent {

    /**
     * Tests the constructor and all the getter methods
     */
    @Test
    public void testGetters() {
        FluxRemoverChooseDiceEvent fluxRemoverChooseDiceEvent = new FluxRemoverChooseDiceEvent("1");
        assertEquals(0, fluxRemoverChooseDiceEvent.getDiceIndex());
    }

    /**
     * Tests if the constructor throws the correct exceptions
     */
    @Test
    public void testIllegalArgumentException() {
        try {
            new FluxRemoverChooseDiceEvent(" ");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Paramentri insufficienti", e.getMessage());
        }

        try {
            new FluxRemoverChooseDiceEvent("ciao 4");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Parametri non numerici o sbagliati", e.getMessage());
        }
    }

}
