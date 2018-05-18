package it.polimi.se2018.testEvents;

import it.polimi.se2018.events.vcevent.WindowPatternChoiceEvent;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class tests WindowPatternChoiceEvent's methods
 * @author fabio
 */
public class TestWindowPatternChoiceEvent {

    /**
     * Tests the constructor and all the getter methods
     */
    @Test
    public void testGetters() {
        WindowPatternChoiceEvent windowPatternChoiceEvent = new WindowPatternChoiceEvent("1");
        assertEquals(0, windowPatternChoiceEvent.getChoice());
    }

    /**
     * Tests if the constructor throws the correct exception
     */
    @Test
    public void testIllegalArgumentException() {
        try {
            WindowPatternChoiceEvent windowPatternChoiceEvent = new WindowPatternChoiceEvent("");
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
        WindowPatternChoiceEvent windowPatternChoiceEvent = new WindowPatternChoiceEvent("3");
        assertEquals("WindowPatternChoiceEvent", windowPatternChoiceEvent.toString());
    }
}
