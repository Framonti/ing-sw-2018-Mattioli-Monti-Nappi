package it.polimi.se2018.testEvents;

import it.polimi.se2018.events.vcevent.GlazingHammerEvent;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class tests GlazingHammerEvent's methods
 * @author fabio
 */
public class TestGlazingHammerEvent {

    /**
     * Tests if the string returned is correct
     */
    @Test
    public void testToString() {
        GlazingHammerEvent glazingHammerEvent = new GlazingHammerEvent();
        assertEquals("GlazingHammerEvent", glazingHammerEvent.toString());
    }
}
