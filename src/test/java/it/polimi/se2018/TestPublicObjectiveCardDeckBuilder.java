package it.polimi.se2018;

import it.polimi.se2018.model.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertTrue;

public class TestPublicObjectiveCardDeckBuilder {

    private final String filePath = "src/main/java/it/polimi/se2018/xml/PublicObjectiveCard.xml";
    private PublicObjectiveCardDeckBuilder publicObjectiveCardDeckBuilder;

    private boolean checkPublicObjectiveCardName(PublicObjectiveCard publicObjectiveCard){

        return  publicObjectiveCard.getName().equals("Colori diversi - Riga") ||
                publicObjectiveCard.getName().equals("Colori diversi - Colonna") ||
                publicObjectiveCard.getName().equals("Sfumature diverse - Riga") ||
                publicObjectiveCard.getName().equals("Sfumature diverse - Colonna") ||
                publicObjectiveCard.getName().equals("Sfumature Medie") ||
                publicObjectiveCard.getName().equals("Sfumature Scure") ||
                publicObjectiveCard.getName().equals("Sfumature Diverse") ||
                publicObjectiveCard.getName().equals("Varietà di colore") ||
                publicObjectiveCard.getName().equals("Diagonali Colorate");

    }

    @Before
    public void setUp(){

        publicObjectiveCardDeckBuilder = new PublicObjectiveCardDeckBuilder(filePath);
    }

    @Test
    public void testGetWindowPatternCardDeck(){

        Deck<PublicObjectiveCard> publicObjectiveCardDeck = publicObjectiveCardDeckBuilder.getPublicObjectiveCardDeck();
        List<PublicObjectiveCard> publicObjectiveCards = publicObjectiveCardDeck.mixAndDistribute(1);
        PublicObjectiveCard publicObjectiveCard = publicObjectiveCards.get(0);

        assertTrue(checkPublicObjectiveCardName(publicObjectiveCard));
    }
}
