package it.polimi.se2018.testModel;

import it.polimi.se2018.model.*;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;


public class TestGameSetupSingleton {

    private GameSetupSingleton instance;

    @Before
    public void setUp(){
        instance = GameSetupSingleton.instance();
        instance.getPlayers().add(new Player("daniele"));
        instance.getPlayers().add(new Player("fabio"));
        instance.getPlayers().add(new Player("francesco"));
        instance.getPlayers().get(0).setPrivateObjectiveCard(new PrivateObjectiveCard("carta1", "carta di daniele",Colour.BLUE));
        instance.getPlayers().get(1).setPrivateObjectiveCard(new PrivateObjectiveCard("carta2", "carta di fabio",Colour.GREEN));
        instance.getPlayers().get(2).setPrivateObjectiveCard(new PrivateObjectiveCard("carta3", "carta di francesco",Colour.PURPLE));

    }

    @Test
    public void testAssignWindowPattern(){
        //instance.assignWindowPattern();
        int i;
        for(i = 0 ; i < GameSetupSingleton.instance().getPlayers().size(); i++)
            assertEquals(4,GameSetupSingleton.instance().getPlayers().get(i).getWindowPatterns().size());
    }


}
