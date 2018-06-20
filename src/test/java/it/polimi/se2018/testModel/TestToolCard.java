package it.polimi.se2018.testModel;

import it.polimi.se2018.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestToolCard {

    private ToolCard toolCard1;

    @Before
    public void setUp(){
        toolCard1 = new ToolCard("carta1", "descrizione della carta 1", Colour.GREEN, 1);
    }

    @Test
    public void TestIncreaseFavorPoint(){

        toolCard1.increaseFavorPoint(0);
        assertEquals(0,toolCard1.getFavorPoint());

        toolCard1.increaseFavorPoint(1);
        assertEquals(1,toolCard1.getFavorPoint());

        toolCard1.increaseFavorPoint(5);
        assertEquals(6,toolCard1.getFavorPoint());
    }

    @Test
    public void TestToString(){
        assertEquals("1)\tcarta1\n\tdescrizione della carta 1\n\tColore: Verde\n\tPrezzo: 1\n", toolCard1.toString(false));
        toolCard1.increaseFavorPoint(3);
        assertEquals("1)\tcarta1\n\tdescrizione della carta 1\n\tColore: Verde\n\tPrezzo: 2\n", toolCard1.toString(false));
    }




}
