package it.polimi.se2018.testModel;

import it.polimi.se2018.model.*;

import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class TestGameSetupSingleton {

    private GameSetupSingleton instance;

    @Before
    public void setUp(){
        instance = GameSetupSingleton.instance();
        List<Player> playerList = new ArrayList<>();
        playerList.add(new Player("daniele"));
        playerList.add(new Player("fabio"));
        playerList.add(new Player("francesco"));
        instance.addPlayers(playerList);
        instance.getPlayers().get(0).setPrivateObjectiveCard(new PrivateObjectiveCard("carta1", "carta di daniele",Colour.BLUE));
        instance.getPlayers().get(1).setPrivateObjectiveCard(new PrivateObjectiveCard("carta2", "carta di fabio",Colour.GREEN));
        instance.getPlayers().get(2).setPrivateObjectiveCard(new PrivateObjectiveCard("carta3", "carta di francesco",Colour.PURPLE));

    }


    @Test
    public void testCreateNewGame() {
        instance.createNewGame();
        for (Player player: instance.getPlayers()) {
            assertNotNull(player.getWindowPatterns());
            assertNotNull(player.getPrivateObjectiveCard());
        }
    }


}
