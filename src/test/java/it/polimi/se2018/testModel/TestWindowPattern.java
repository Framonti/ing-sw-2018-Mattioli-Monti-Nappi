package it.polimi.se2018.testModel;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Position;
import it.polimi.se2018.model.WindowPattern;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the WindowPattern class
 * @author fabio
 */
public class TestWindowPattern {

    private WindowPattern windowPattern;
    private Position position;
    private Dice dice;

    /**
     * Initializes a new window pattern, a position and a dice which is purple and its value is 4
     */
    @Before
    public void setUp() {
        windowPattern = new WindowPattern("wp", 4, new Dice[4][5]);
        position = new Position(2, 2);
        dice = new Dice(Colour.PURPLE);
        dice.setValue(4);
    }

    /**
     * Tests if the checkCell returns true when the cell is empty and when the cell has a restriction
     * with the same colour of the dice
     */
    @Test
    public void testCheckCellTrue() {
        assertTrue(windowPattern.checkCell(position, dice));

        Dice restriction = new Dice(Colour.PURPLE);
        windowPattern.getWindowPattern()[2][2] = restriction;

        assertTrue(windowPattern.checkCell(position, dice));
    }

    /**
     * Tests if the checkCell method returns false when the cell has a restriction with
     * a value different from the value of the dice
     */
    @Test
    public void testCheckCellFalse() {
        Dice restriction = new Dice(3);
        windowPattern.getWindowPattern()[2][2] = restriction;

        assertFalse(windowPattern.checkCell(position, dice));
    }

    /**
     * Tests if the String returned by toString is correct
     */
    @Test
    public void testToString() {
        windowPattern.getWindowPattern()[2][2] = dice;

        assertEquals("Nome: wp\nDifficoltà: 4\nO\tO\tO\tO\tO\t\nO\tO\tO\tO\tO\t\nO\tO\t" +
                dice.toString() + "\tO\tO\t\nO\tO\tO\tO\tO\t\n", windowPattern.toString());
    }
}
