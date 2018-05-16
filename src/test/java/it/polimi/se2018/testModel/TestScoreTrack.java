package it.polimi.se2018.testModel;

import it.polimi.se2018.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for the ScoreTrack class and the ScoreMarker class
 * @author fabio
 */
public class TestScoreTrack {

    private Player player;

    /**
     * Initializes a new Player
     */
    @Before
    public void setUp() {
        player = new Player("Fabio");
    }

    /**
     * Tests if the String returned by the toString method is correct
     */
    @Test
    public void testToString() {
        List<Player> players = new ArrayList<>();
        player.setScore(2);
        players.add(player);
        ScoreTrack scoreTrack = new ScoreTrack(players);

        assertEquals("Fabio: 2\n", scoreTrack.toString());
    }

    /**
     * Test if the player assigned to the score marker is correct
     */
    @Test
    public void testScoreMarker() {
        ScoreMarker scoreMarker = new ScoreMarker(player);

        assertEquals(player, scoreMarker.getCurrentPlayer());
    }
}
