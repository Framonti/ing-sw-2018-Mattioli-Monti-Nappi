package it.polimi.se2018.testModel;

import it.polimi.se2018.PublicObjectiveCardDeckBuilder;
import it.polimi.se2018.model.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the PublicObjectiveCardBuilder Class
 * @author Framonti
 */
public class TestPublicObjectiveCardDeckBuilder {

    private final String filePath = "src/main/java/it/polimi/se2018/xml/PublicObjectiveCard.xml";
    private PublicObjectiveCardDeckBuilder publicObjectiveCardDeckBuilder;

    private boolean checkPublicObjectiveCard(PublicObjectiveCard publicObjectiveCard){

        return  (publicObjectiveCard.getName().equals("Colori diversi - Riga") && publicObjectiveCard.getDescription().equals("Righe senza colori ripetuti"))||
                (publicObjectiveCard.getName().equals("Colori diversi - Colonna") && publicObjectiveCard.getDescription().equals("Colonne senza colori ripetuti"))||
                (publicObjectiveCard.getName().equals("Sfumature diverse - Riga") && publicObjectiveCard.getDescription().equals("Righe senza sfumature ripetute"))||
                (publicObjectiveCard.getName().equals("Sfumature diverse - Colonna")&& publicObjectiveCard.getDescription().equals("Colonne senza sfumature ripetute")) ||
                (publicObjectiveCard.getName().equals("Sfumature Chiare") && publicObjectiveCard.getDescription().equals("Set di 1 & 2 ovunque")||
                (publicObjectiveCard.getName().equals("Sfumature Medie") && publicObjectiveCard.getDescription().equals("Set di 3 & 4 ovunque"))||
                (publicObjectiveCard.getName().equals("Sfumature Scure") && publicObjectiveCard.getDescription().equals("Set di 5 & 6 ovunque"))||
                (publicObjectiveCard.getName().equals("Sfumature Diverse") && publicObjectiveCard.getDescription().equals("Set di dadi di ogni valore ovunque"))||
                (publicObjectiveCard.getName().equals("Varietà di Colore") && publicObjectiveCard.getDescription().equals("Set di dadi di ogni colore ovunque"))||
                (publicObjectiveCard.getName().equals("Diagonali Colorate")&& publicObjectiveCard.getDescription().equals("Numero di dadi dello stesso colore diagonalmente adiacenti")));
    }

    @Before
    public void setUp(){

        publicObjectiveCardDeckBuilder = new PublicObjectiveCardDeckBuilder(filePath);
    }


    /**
     * Tests that every PrivateObjectiveCard is correctly created
     */
    @Test
    public void testGetPublicObjectiveCardDeck(){

        Deck<PublicObjectiveCard> publicObjectiveCardDeck = publicObjectiveCardDeckBuilder.getPublicObjectiveCardDeck();
        List<PublicObjectiveCard> publicObjectiveCards = publicObjectiveCardDeck.mixAndDistribute(10);

        for(PublicObjectiveCard publicObjectiveCard : publicObjectiveCards){

            assertTrue(checkPublicObjectiveCard(publicObjectiveCard));
        }


    }
}
