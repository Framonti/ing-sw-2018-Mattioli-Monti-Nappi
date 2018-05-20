package it.polimi.se2018.testEvents;

import it.polimi.se2018.events.vcevent.GrozingPliersEvent;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * This class tests GrozingPliersEvent Class
 * @author Framonti
 */
public class TestGrozingPliersEvent {

    /**
     * Tests the constructor and all the getter methods
     */
    @Test
    public void testGetters() {
        try{
            GrozingPliersEvent grozingPliersEvent = new GrozingPliersEvent("2 3");
            assertEquals(1, grozingPliersEvent.getDiceIndexDraftPool());
            assertEquals(2, grozingPliersEvent.getChoice());

        }
        catch (IllegalArgumentException e){
            fail();
        }
    }

    /**
     * Tests if the constructor throws the correct exceptions
     */
    @Test
    public void testIllegalArgumentException() {
        try {
            GrozingPliersEvent grozingPliersEvent = new GrozingPliersEvent("2");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Parametri insufficienti", e.getMessage());
        }
        try {
            GrozingPliersEvent grozingPliersEvent = new GrozingPliersEvent("1 notANumber");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Parametri non numerici o sbagliati", e.getMessage());
        }
    }
}
