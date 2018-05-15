package it.polimi.se2018;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.PrivateObjectiveCard;
import it.polimi.se2018.model.PublicObjectiveCard;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestPublicObjectiveCard {

    @Test
    public void testToString(){
        PublicObjectiveCard publicObjectiveCard1 = new PublicObjectiveCard("nome1", "descrizione1",5);
        PublicObjectiveCard publicObjectiveCard2 = new PublicObjectiveCard("nome2", "descrizione2",0);

        assertEquals("Nome: nome1\nDescrizione: descrizione1\nPunti Vittoria: 5", publicObjectiveCard1.toString());
        assertEquals("Nome: nome2\nDescrizione: descrizione2\nPunti Vittoria: #", publicObjectiveCard2.toString());
    }
}
