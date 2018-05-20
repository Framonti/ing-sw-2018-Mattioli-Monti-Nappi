package it.polimi.se2018.testEvents;

import it.polimi.se2018.events.vcevent.CorkBakedStraightedgeEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This class tests WindowPatternChoiceEvent's methods
 * @author Framonti
 */
public class TestCorkBakedStraightEdgeEvent {

    /**
     * Tests the constructor and all the getter methods
     */
    @Test
    public void testGetters() {
        try{
            CorkBakedStraightedgeEvent corkBakedStraightEdgeEvent = new CorkBakedStraightedgeEvent("1 2 4");
            assertEquals(0, corkBakedStraightEdgeEvent.getIndexInDraftPool());
            assertEquals(1, corkBakedStraightEdgeEvent.getFinalPosition().getX());
            assertEquals(3, corkBakedStraightEdgeEvent.getFinalPosition().getY());
        }
        catch (IllegalArgumentException e){
            fail();
        }

    }

    /**
     * Tests if the constructor throws the correct exception
     */
    @Test
    public void testIllegalArgumentException() {
        try {
            CorkBakedStraightedgeEvent corkBakedStraightEdgeEvent = new CorkBakedStraightedgeEvent("1 3");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Parametri insufficienti", e.getMessage());
        }
        try {
            CorkBakedStraightedgeEvent corkBakedStraightEdgeEvent = new CorkBakedStraightedgeEvent("1 ciao 4");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Parametri non numerici o sbagliati", e.getMessage());
        }
    }
}
