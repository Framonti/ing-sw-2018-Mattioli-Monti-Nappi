package it.polimi.se2018;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Position;
import it.polimi.se2018.model.WindowPattern;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestWindowPattern {

    private WindowPattern windowPattern;
    private Position position;
    private Dice dice;

    @Before
    public void setUp() {
        Dice[][] dices = new Dice[4][5];
        windowPattern = new WindowPattern("", 4, dices);
        position = new Position(2, 2);
        dice = new Dice(Colour.PURPLE);
        dice.roll();
    }

    @Test
    public void testCheckCellTrue() {
        assertTrue(windowPattern.checkCell(position, dice));
    }

    @Test
    public void testCheckCellFalse() {
        Dice restriction = new Dice(Colour.RED);
        windowPattern.getWindowPattern()[2][2] = restriction;

        assertFalse(windowPattern.checkCell(position, dice));
    }
}
