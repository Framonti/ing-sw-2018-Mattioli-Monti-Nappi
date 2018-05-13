package it.polimi.se2018;

import it.polimi.se2018.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestPlayer {

    private Player player;

    @Before
    public void setUp() {
        Dice[][] pattern = new Dice[4][5];
        WindowPattern windowPattern = new WindowPattern("", 4, pattern);
        PrivateObjectiveCard  privateObjectiveCard= new PrivateObjectiveCard("", "", Colour.PURPLE);
        player = new Player("", privateObjectiveCard);
        player.setWindowPattern(windowPattern);
    }

    @Test
    public void testReduceFavorTokensTrue() {
        player.reduceFavorTokens(2);

        assertEquals(2, player.getFavorTokensNumber());
    }

    @Test
    public void testReduceFavorTokensFalse() {
        player.reduceFavorTokens(3);

        assertNotEquals(3, player.getFavorTokensNumber());
    }

    @Test
    public void testComputePrivateObjectiveCardScoreTrue() {

        assertEquals(0, player.computePrivateObjectiveCardScore());
    }

    @Test
    public void testComputePrivateObjectiveCardScoreFalse() {

        assertNotEquals(5 , player.computePrivateObjectiveCardScore());
    }

    @Test
    public void testComputeMyScoreTrue() {
        PublicObjectiveCard poc1 = new PublicObjectiveCard("Colori diversi - Riga", "", 6);
        PublicObjectiveCard poc2 = new PublicObjectiveCard("Colori diversi - Colonna", "", 5);
        PublicObjectiveCard poc3 = new PublicObjectiveCard("Sfumature diverse - Riga", "", 5);
        List<PublicObjectiveCard> publicObjectiveCards = new ArrayList<>();
        publicObjectiveCards.add(poc1);
        publicObjectiveCards.add(poc2);
        publicObjectiveCards.add(poc3);

        //4 points gained because of the favor tokens and 20 points lost because the window pattern is empty
        assertEquals(-16, player.computeMyScore(publicObjectiveCards));
    }

    @Test
    public void testComputeMyScoreFalse() {
        PublicObjectiveCard poc1 = new PublicObjectiveCard("Sfumature Chiare", "", 2);
        PublicObjectiveCard poc2 = new PublicObjectiveCard("Sfumature Medie", "", 2);
        PublicObjectiveCard poc3 = new PublicObjectiveCard("Sfumature diverse - Colonna", "", 4);
        List<PublicObjectiveCard> publicObjectiveCards = new ArrayList<>();
        publicObjectiveCards.add(poc1);
        publicObjectiveCards.add(poc2);
        publicObjectiveCards.add(poc3);

        //4 points gained because of the favor tokens and 20 points lost because the window pattern is empty
        assertNotEquals(0, player.computeMyScore(publicObjectiveCards));
    }
}
