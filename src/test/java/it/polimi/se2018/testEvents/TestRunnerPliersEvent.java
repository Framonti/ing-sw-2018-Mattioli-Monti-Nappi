package it.polimi.se2018.testEvents;

import it.polimi.se2018.events.vcevent.RunnerPliersEvent;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class tests RunnerPliersEvent's methods
 * @author fabio
 */
public class TestRunnerPliersEvent {

    /**
     * Tests the constructor and the getter methods
     */
    @Test
    public void testGetters() {
        RunnerPliersEvent runnerPliersEvent = new RunnerPliersEvent("1 2 3");
        assertEquals(0, runnerPliersEvent.getDiceIndex());
        assertEquals(1, runnerPliersEvent.getPosition().getX());
        assertEquals(2, runnerPliersEvent.getPosition().getY());
    }

    /**
     * Tests if the constructor throws the correct exception
     */
    @Test
    public void testIllegalArgumentException() {
        try {
            new RunnerPliersEvent("2 1");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Parametri insufficienti", e.getMessage());
        }
        try {
            new RunnerPliersEvent("ciao");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Parametri non numerici o sbagliati", e.getMessage());
        }
    }

}
