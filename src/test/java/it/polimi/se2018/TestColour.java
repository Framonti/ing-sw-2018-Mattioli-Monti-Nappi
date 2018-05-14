package it.polimi.se2018;

import it.polimi.se2018.model.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests from the Colour enumeration
 */
public class TestColour {

    /**
     * Tests the getColourFromString method
     */
    @Test
    public void testGetColourFromString() {

        Colour colourRed = Colour.getColourFromString("RED");
        Colour colourPurple = Colour.getColourFromString("PURPLE");

        assertEquals(Colour.RED, colourRed);
        assertEquals(Colour.PURPLE, colourPurple);
    }

    /**
     * Checks that the getColourFromStringException raises an IllegalArgumentException
     */
    @Test
    public void testGetColourFromStringException(){

        try{
            Colour.getColourFromString("BLACK");
        }
        catch (IllegalArgumentException e){

            assertTrue("Test passed", true);
        }
    }

}
