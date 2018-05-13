package it.polimi.se2018;

import it.polimi.se2018.model.*;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;


public class TestGameSetupSingleton {

    private GameSetupSingleton instance;

    @Before
    public void setUp(){
        instance = GameSetupSingleton.instance();
        instance.getPlayers().add(new Player("daniele",new PrivateObjectiveCard("carta1", "carta di daniele",Colour.BLUE)));
        instance.getPlayers().add(new Player("fabio",new PrivateObjectiveCard("carta2", "carta di fabio",Colour.GREEN)));
        instance.getPlayers().add(new Player("francesco",new PrivateObjectiveCard("carta3", "carta di francesco",Colour.PURPLE)));
    }

    @Test
    public void testAssignWindowPattern(){
        instance.assignWindowPattern();
        int i;
        for(i = 0 ; i < GameSetupSingleton.instance().getPlayers().size(); i++)
            assertEquals(4,GameSetupSingleton.instance().getPlayers().get(i).getWindowPatterns().size());
    }


}
