package it.polimi.se2018.testEvents;

import it.polimi.se2018.events.vcevent.LensCutterEvent;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This class tests LensCutter Class
 * @author Framonti
 */
public class TestLensCutter {

    /**
     * Tests the constructor and all the getter methods
     */
    @Test
    public void testGetters() {
        try{
            LensCutterEvent lensCutterEvent = new LensCutterEvent("2 3 3");

            assertEquals(1, lensCutterEvent.getDiceIndexInDraftPool());
            assertEquals(2, lensCutterEvent.getDiceIndexInRoundTrack());
            assertEquals(2, lensCutterEvent.getRoundIndex());
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
            LensCutterEvent lensCutterEvent = new LensCutterEvent("2 5");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Parametri insufficienti", e.getMessage());
        }
        try {
            LensCutterEvent grozingPliersEvent = new LensCutterEvent("1 notANumber 4");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Parametri non numerici o sbagliati", e.getMessage());
        }
    }
}
