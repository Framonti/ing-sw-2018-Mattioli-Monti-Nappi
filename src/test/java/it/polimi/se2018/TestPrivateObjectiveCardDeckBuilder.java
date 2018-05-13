package it.polimi.se2018;

import it.polimi.se2018.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class TestPrivateObjectiveCardDeckBuilder {
    private final String filePath = "src/main/java/it/polimi/se2018/xml/PrivateObjectiveCard.xml";
    private PrivateObjectiveCardDeckBuilder privateObjectiveCardDeckBuilder;

    private boolean checkPrivateObjectiveCardName(PrivateObjectiveCard privateObjectiveCard){

        return  privateObjectiveCard.getName().equals("Sfumature Rosse") ||
                privateObjectiveCard.getName().equals("Sfumature Verdi") ||
                privateObjectiveCard.getName().equals("Sfumature Gialle") ||
                privateObjectiveCard.getName().equals("Sfumature Blu") ||
                privateObjectiveCard.getName().equals("Sfumature Viola");
    }

    @Before
    public void setUp(){

        privateObjectiveCardDeckBuilder = new PrivateObjectiveCardDeckBuilder(filePath);
    }

    @Test
    public void testGetWindowPatternCardDeck(){

        Deck<PrivateObjectiveCard> privateObjectiveCardDeck = privateObjectiveCardDeckBuilder.getPrivateObjectiveCardDeck();
        List<PrivateObjectiveCard> privateObjectiveCards = privateObjectiveCardDeck.mixAndDistribute(1);
        PrivateObjectiveCard privateObjectiveCard = privateObjectiveCards.get(0);

        assertTrue(checkPrivateObjectiveCardName(privateObjectiveCard));
    }
}
