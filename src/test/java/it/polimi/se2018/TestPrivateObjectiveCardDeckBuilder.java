package it.polimi.se2018;

import it.polimi.se2018.model.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the PrivateObjectiveCardDeckBuilder Class
 * @author Framonti
 */
public class TestPrivateObjectiveCardDeckBuilder {
    private final String filePath = "src/main/java/it/polimi/se2018/xml/PrivateObjectiveCard.xml";
    private PrivateObjectiveCardDeckBuilder privateObjectiveCardDeckBuilder;

    private boolean checkPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard){

        return  (privateObjectiveCard.getName().equals("Sfumature Rosse") && privateObjectiveCard.getDescription().equals("Somma dei valori su tutti i dadi rossi")) ||
                (privateObjectiveCard.getName().equals("Sfumature Verdi") && privateObjectiveCard.getDescription().equals("Somma dei valori su tutti i dadi verdi")) ||
                (privateObjectiveCard.getName().equals("Sfumature Gialle") && privateObjectiveCard.getDescription().equals("Somma dei valori su tutti i dadi gialli")) ||
                (privateObjectiveCard.getName().equals("Sfumature Blu") && privateObjectiveCard.getDescription().equals("Somma dei valori su tutti i dadi blu")) ||
                (privateObjectiveCard.getName().equals("Sfumature Viola") && privateObjectiveCard.getDescription().equals("Somma dei valori su tutti i dadi viola"));
    }

    @Before
    public void setUp(){

        privateObjectiveCardDeckBuilder = new PrivateObjectiveCardDeckBuilder(filePath);
    }

    /**
     * Tests that every PrivateObjectiveCard is correctly created
     */
    @Test
    public void testGetWindowPatternCardDeck(){

        Deck<PrivateObjectiveCard> privateObjectiveCardDeck = privateObjectiveCardDeckBuilder.getPrivateObjectiveCardDeck();
        List<PrivateObjectiveCard> privateObjectiveCards = privateObjectiveCardDeck.mixAndDistribute(5);

        for(PrivateObjectiveCard privateObjectiveCard : privateObjectiveCards){

            assertTrue(checkPrivateObjectiveCard(privateObjectiveCard));
        }

    }
}
