package it.polimi.se2018.testEvents;

import it.polimi.se2018.events.vcevent.TapWheelEvent;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class tests TapWheelEvent's methods
 * @author fabio
 */
public class TestTapWheelEvent {

    /**
     * Tests the constructor and the getter methods in the case of moving 2 dices
     */
    @Test
    public void testGettersWithTwoDices() {
        TapWheelEvent tapWheelEvent = new TapWheelEvent("1 1 2 2 3 3 4 1 2 5");
        assertEquals(0, tapWheelEvent.getRoundIndex());
        assertEquals(0, tapWheelEvent.getDiceIndex());
        assertEquals(1, tapWheelEvent.getFirstDicePosition().getX());
        assertEquals(1, tapWheelEvent.getFirstDicePosition().getY());
        assertEquals(2, tapWheelEvent.getNewFirstDicePosition().getX());
        assertEquals(2, tapWheelEvent.getNewFirstDicePosition().getY());
        assertEquals(3, tapWheelEvent.getSecondDicePosition().getX());
        assertEquals(0, tapWheelEvent.getSecondDicePosition().getY());
        assertEquals(1, tapWheelEvent.getNewSecondDicePosition().getX());
        assertEquals(4, tapWheelEvent.getNewSecondDicePosition().getY());
    }

    /**
     * Tests the constructor and the getter methods in the case of moving 1 dice
     */
    @Test
    public void testGettersWithOneDice() {
        TapWheelEvent tapWheelEvent = new TapWheelEvent("1 1 2 2 3 3");
        assertEquals(0, tapWheelEvent.getRoundIndex());
        assertEquals(0, tapWheelEvent.getDiceIndex());
        assertEquals(1, tapWheelEvent.getFirstDicePosition().getX());
        assertEquals(1, tapWheelEvent.getFirstDicePosition().getY());
        assertEquals(2, tapWheelEvent.getNewFirstDicePosition().getX());
        assertEquals(2, tapWheelEvent.getNewFirstDicePosition().getY());
    }

}
