package it.polimi.se2018.testModel;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.PrivateObjectiveCard;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Test for PrivateObjectiveCard class
 * @author Daniele Mattioli
 */
public class TestPrivateObjectiveCard {

    /**
     * Tests toString method
     */
    @Test
    public void testToString(){
        PrivateObjectiveCard privateObjectiveCard1 = new PrivateObjectiveCard("nome1", "descrizione1", Colour.PURPLE);
        PrivateObjectiveCard privateObjectiveCard2 = new PrivateObjectiveCard("nome2", "descrizione2", Colour.GREEN);
        assertEquals("nome1\ndescrizione1\n", privateObjectiveCard1.toString());
        assertEquals("nome2\ndescrizione2\n", privateObjectiveCard2.toString());
    }

    /**
     * Tests toStringPath method
     */
    @Test
    public void testToStringPath(){
        PrivateObjectiveCard privateObjectiveCard1 = new PrivateObjectiveCard("nome1", "descrizione1", Colour.PURPLE);
        PrivateObjectiveCard privateObjectiveCard2 = new PrivateObjectiveCard("nome2", "descrizione2", Colour.GREEN);
        assertEquals("src/main/Images/PrivateObjectiveCard/Sfumature"+privateObjectiveCard1.getColour().getAbbreviation()+".jpg", privateObjectiveCard1.toStringPath());
        assertEquals("src/main/Images/PrivateObjectiveCard/Sfumature"+privateObjectiveCard2.getColour().getAbbreviation()+".jpg", privateObjectiveCard2.toStringPath());

    }


}
