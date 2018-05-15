package it.polimi.se2018;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.PrivateObjectiveCard;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestPrivateObjectiveCard {

    @Test
    public void testToString(){
        PrivateObjectiveCard privateObjectiveCard1 = new PrivateObjectiveCard("nome1", "descrizione1", Colour.PURPLE);
        PrivateObjectiveCard privateObjectiveCard2 = new PrivateObjectiveCard("nome2", "descrizione2", Colour.GREEN);
        assertEquals("nome1\ndescrizione1", privateObjectiveCard1.toString());
        assertEquals("nome2\ndescrizione2", privateObjectiveCard2.toString());
    }

}
