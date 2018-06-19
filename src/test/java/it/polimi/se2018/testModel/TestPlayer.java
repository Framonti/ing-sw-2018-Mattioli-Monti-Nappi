package it.polimi.se2018.testModel;

import it.polimi.se2018.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for the Player class
 * @author fabio
 */
public class TestPlayer {

    private Player player;

    /**
     * Initializes a new Player and assign to him a new window pattern
     */
    @Before
    public void setUp() {
        player = new Player("");
        player.setPrivateObjectiveCard(new PrivateObjectiveCard("", "", Colour.PURPLE));
        player.setWindowPattern(new WindowPattern("", 4, new Dice[4][5]), false);
    }

    /**
     * Decreases the favor tokens by 2
     */
    @Test
    public void testReduceFavorTokensTrue() {
        try {
            player.reduceFavorTokens(2);
        } catch (NullPointerException ignored) {}

        assertEquals(2, player.getFavorTokensNumber());
    }

    /**
     * Tests if the Exception is thrown
     */
    @Test
    public void testUnsupportedOperationException() {
        boolean exc = false;
        try {
            player.reduceFavorTokens(5);
        }
        catch (UnsupportedOperationException e) {
            exc = true;
            assertEquals("The number of favor tokens is not sufficient", e.getMessage());
        }

        assertTrue(exc);
    }

    /**
     * Tests if the score obtained with the privateObjectiveCard is correct
     */
    @Test
    public void testComputePrivateObjectiveCardScore() {
        Dice dice = new Dice(Colour.PURPLE);
        int value = dice.rollAndGet();
        try {
            player.getDicePattern().setDice(new Position(1, 1), dice);
        } catch (NullPointerException ignored) {}

        assertEquals(value, player.computePrivateObjectiveCardScore());
    }

    /**
     * Tests if the total score obtained with the first 5 publicObjectiveCards is correct
     * 6 dices are added on the pattern for the simulation
     */
    @Test
    public void testComputeMyScoreTrue() {
        List<PublicObjectiveCard> publicObjectiveCards = new ArrayList<>();
        publicObjectiveCards.add(new PublicObjectiveCard("Colori diversi - Riga", "", 6));
        publicObjectiveCards.add(new PublicObjectiveCard("Colori diversi - Colonna", "", 5));
        publicObjectiveCards.add(new PublicObjectiveCard("Sfumature diverse - Riga", "", 5));
        publicObjectiveCards.add(new PublicObjectiveCard("Sfumature Chiare", "", 2));
        publicObjectiveCards.add(new PublicObjectiveCard("Sfumature diverse - Colonna", "", 4));

        Dice d1 = new Dice(Colour.GREEN);
        Dice d2 = new Dice(Colour.RED);
        Dice d3 = new Dice(Colour.YELLOW);
        Dice d4 = new Dice(Colour.PURPLE);
        Dice d5 = new Dice(Colour.BLUE);
        Dice d6 = new Dice(Colour.BLUE);
        d1.setValue(1);
        d2.setValue(2);
        d3.setValue(3);
        d4.setValue(4);
        d5.setValue(5);
        d6.setValue(6);

        try {
            player.getDicePattern().setDice(new Position(0, 0), d1);
        } catch (NullPointerException ignored) {}
        try {
            player.getDicePattern().setDice(new Position(1, 0), d2);
        } catch (NullPointerException ignored) {}
        try {
            player.getDicePattern().setDice(new Position(1, 1), d4);
        } catch (NullPointerException ignored) {}
        try {
            player.getDicePattern().setDice(new Position(1, 2), d1);
        } catch (NullPointerException ignored) {}
        try {
            player.getDicePattern().setDice(new Position(1, 3), d3);
        } catch (NullPointerException ignored) {}
        try {
            player.getDicePattern().setDice(new Position(1, 4), d5);
        } catch (NullPointerException ignored) {}
        try {
            player.getDicePattern().setDice(new Position(3, 0), d3);
        } catch (NullPointerException ignored) {}
        try {
            player.getDicePattern().setDice(new Position(2, 0), d5);
        } catch (NullPointerException ignored) {}
        try {
            player.getDicePattern().setDice(new Position(3, 1), d6);
        } catch (NullPointerException ignored) {}

        player.computeMyScore(publicObjectiveCards);

        assertEquals(19, player.getScore());
    }

    /**
     * Tests that the total score is not 0 with the last 5 publicObjectiveCards
     * 6 dices are added on the pattern for the simulation
     */
    @Test
    public void testComputeMyScoreFalse() {
        List<PublicObjectiveCard> publicObjectiveCards = new ArrayList<>();
        publicObjectiveCards.add(new PublicObjectiveCard("Sfumature Medie", "", 2));
        publicObjectiveCards.add(new PublicObjectiveCard("Sfumature Scure", "", 2));
        publicObjectiveCards.add(new PublicObjectiveCard("Sfumature Diverse", "", 5));
        publicObjectiveCards.add(new PublicObjectiveCard("Diagonali Colorate", "", 0));
        publicObjectiveCards.add(new PublicObjectiveCard("Varietà di Colore", "", 4));

        Dice d1 = new Dice(Colour.GREEN);
        Dice d2 = new Dice(Colour.RED);
        Dice d3 = new Dice(Colour.YELLOW);
        Dice d4 = new Dice(Colour.PURPLE);
        Dice d5 = new Dice(Colour.BLUE);
        Dice d6 = new Dice(Colour.BLUE);
        d1.setValue(1);
        d2.setValue(2);
        d3.setValue(3);
        d4.setValue(4);
        d5.setValue(5);
        d6.setValue(6);

        try {
            player.getDicePattern().setDice(new Position(0, 0), d1);
        } catch (NullPointerException ignored) {}
        try {
            player.getDicePattern().setDice(new Position(1, 0), d2);
        } catch (NullPointerException ignored) {}
        try {
            player.getDicePattern().setDice(new Position(3, 0), d3);
        } catch (NullPointerException ignored) {}
        try {
            player.getDicePattern().setDice(new Position(1, 1), d4);
        } catch (NullPointerException ignored) {}
        try {
            player.getDicePattern().setDice(new Position(2, 0), d5);
        } catch (NullPointerException ignored) {}
        try {
            player.getDicePattern().setDice(new Position(3, 1), d6);
        } catch (NullPointerException ignored) {}

        player.computeMyScore(publicObjectiveCards);
        //4 points gained because of the favor tokens, 2+2 gained for the sets of 3-4 and 5-6, 5 gained for all the sets
        //2 gained because of diagonals, 4 gained because of colour set and 14 points lost because of empty spaces
        assertNotEquals(0, player.getScore());
    }
}
