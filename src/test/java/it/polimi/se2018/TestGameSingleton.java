package it.polimi.se2018;

import it.polimi.se2018.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestGameSingleton {
    private ArrayList <Player> players;
    private ArrayList <PublicObjectiveCard> publicObjectiveCards;
    private ArrayList <ToolCard> toolCards;
    private ScoreTrack scoreTrack;
    private RoundTrack roundTrack;
    //private GameSingleton instance;
    @Before
    public void setUp(){
        players = new ArrayList<>();
        publicObjectiveCards = new ArrayList<>();
        toolCards = new ArrayList<>();
        players.add(new Player("daniele",new PrivateObjectiveCard("carta1", "carta di daniele", Colour.BLUE)));
        players.add(new Player("fabio",new PrivateObjectiveCard("carta2", "carta di fabio", Colour.GREEN)));
        players.add(new Player("francesco",new PrivateObjectiveCard("carta3", "carta di francesco", Colour.PURPLE)));
        publicObjectiveCards.add(new PublicObjectiveCard("", "", 3));
        publicObjectiveCards.add(new PublicObjectiveCard("", "", 2));
        publicObjectiveCards.add(new PublicObjectiveCard("", "", 1));
        toolCards.add(new ToolCard("","", Colour.BLUE));
        toolCards.add(new ToolCard("","", Colour.PURPLE));
        toolCards.add(new ToolCard("","", Colour.RED));
        toolCards.add(new ToolCard("","", Colour.GREEN));
        scoreTrack = new ScoreTrack(players);
        roundTrack = new RoundTrack();
        //instance = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack,scoreTrack);

    }

    @Test
    public void testCreateDiceBag(){
        GameSingleton instance = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack,scoreTrack);
        assertEquals(90-instance.getDraftPool().size(),instance.getDiceBag().size());
    }

    @Test
    public void testIncreaseRoundTrue(){
        try{
            GameSingleton instance = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack,scoreTrack);
            instance.setRound(0);
            instance.increaseRound();
            assertEquals(1,instance.getRound());
        } catch (IllegalArgumentException exception){
            fail();
        }

        try{
            GameSingleton instance = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack,scoreTrack);
            instance.setRound(8);
            instance.increaseRound();
            assertEquals(9,instance.getRound());
        } catch (IllegalArgumentException exception){
            fail();
        }

        try{
            GameSingleton instance = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack,scoreTrack);
            instance.setRound(3);
            instance.increaseRound();
            assertEquals(4,instance.getRound());
        } catch (IllegalArgumentException exception){
            fail();
        }



    }


    //DA CHIEDERE SE VA BENE COSì
    @Test(expected = IllegalArgumentException.class)
    public void testIncreaseRoundException(){
        GameSingleton instance = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack,scoreTrack);
        instance.setRound(-1);
        instance.increaseRound();

        instance.setRound(9);
        instance.increaseRound();

        instance.setRound(10);
        instance.increaseRound();
    }


    @Test
    public void testExtractAndRoll(){
        GameSingleton instance = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack,scoreTrack);
        int numberToExtract;
        numberToExtract = (2* instance.getPlayers().size() )+1;
        assertEquals(7,numberToExtract);
        instance.extractAndRoll();
        assertEquals(83,instance.getDiceBag().size());
        assertEquals(7,instance.getDraftPool().size());

        instance.extractAndRoll();
        assertEquals(83-7,instance.getDiceBag().size());
        assertEquals(7+7,instance.getDraftPool().size());

    }

    @Test
    public void testSelectWinner(){
        int i;
        i = 10;
        GameSingleton instance = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack,scoreTrack);
        for(Player player : instance.getPlayers()){
            player.setScore(i);
            i *= 2;
        }
        assertEquals(instance.getPlayers().get(2),instance.selectWinner());

        instance.getPlayers().get(0).setScore(1000);
        assertEquals(instance.getPlayers().get(0),instance.selectWinner());
        assertNotEquals(instance.getPlayers().get(2),instance.selectWinner());
    }
}
