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
    @Before
    public void setUp(){
        players = new ArrayList<>();
        publicObjectiveCards = new ArrayList<>();
        toolCards = new ArrayList<>();
        players.add(new Player("daniele"));
        players.add(new Player("fabio"));
        players.add(new Player("francesco"));

        players.get(0).setPrivateObjectiveCard(new PrivateObjectiveCard("carta1", "carta di daniele", Colour.BLUE));
        players.get(1).setPrivateObjectiveCard(new PrivateObjectiveCard("carta2", "carta di fabio", Colour.GREEN));
        players.get(2).setPrivateObjectiveCard(new PrivateObjectiveCard("carta3", "carta di francesco", Colour.PURPLE));

        publicObjectiveCards.add(new PublicObjectiveCard("", "", 3));
        publicObjectiveCards.add(new PublicObjectiveCard("", "", 2));
        publicObjectiveCards.add(new PublicObjectiveCard("", "", 1));
        toolCards.add(new ToolCard("","", Colour.BLUE));
        toolCards.add(new ToolCard("","", Colour.PURPLE));
        toolCards.add(new ToolCard("","", Colour.RED));
        toolCards.add(new ToolCard("","", Colour.GREEN));
        scoreTrack = new ScoreTrack(players);
        roundTrack = new RoundTrack();


    }

    @Test
    public void testCreateDiceBag(){
        GameSingleton instance = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack,scoreTrack);
        assertEquals(90,instance.getDiceBag().size());

        //controllo che ci siano 18 dadi per ogni colore
        int n = 0;
        for(Dice dice : instance.getDiceBag()){
            if (dice.getColour().equals(Colour.GREEN))
                n++;
        }
        assertEquals(18, n);

        n=0;
        for(Dice dice : instance.getDiceBag()){
            if (dice.getColour().equals(Colour.RED))
                n++;
        }
        assertEquals(18, n);

        n=0;
        for(Dice dice : instance.getDiceBag()){
            if (dice.getColour().equals(Colour.PURPLE))
                n++;
        }
        assertEquals(18, n);

        n=0;
        for(Dice dice : instance.getDiceBag()){
            if (dice.getColour().equals(Colour.BLUE))
                n++;
        }
        assertEquals(18, n);

        n=0;
        for(Dice dice : instance.getDiceBag()){
            if (dice.getColour().equals(Colour.YELLOW))
                n++;
        }
        assertEquals(18, n);

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

        //rimetto i dadi nel diceBag
        boolean b = instance.getDiceBag().addAll(instance.getDraftPool());
        assertEquals(90,instance.getDiceBag().size());
        assertTrue(b);


    }


    //TODO analizzare il caso di vittoria e i vari casi di pareggio
    @Test
    public void testSelectWinner(){
        GameSingleton instance = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack,scoreTrack);
        instance.getPlayers().get(0).setWindowPattern(new WindowPattern("", 4, new Dice[4][5]));
        instance.getPlayers().get(1).setWindowPattern(new WindowPattern("", 3, new Dice[4][5]));
        instance.getPlayers().get(2).setWindowPattern(new WindowPattern("", 5, new Dice[4][5]));

        //winner is player 1
        instance.getPlayers().get(0).setScore(8);
        instance.getPlayers().get(1).setScore(10);
        instance.getPlayers().get(2).setScore(9);

        assertEquals(instance.getPlayers().get(1), instance.selectWinner());



        //winner is player 0
        instance.getPlayers().get(0).setScore(8);
        instance.getPlayers().get(1).setScore(8);
        instance.getPlayers().get(2).setScore(8);
        Dice d1 = new Dice(Colour.GREEN);
        Dice d2 = new Dice(Colour.BLUE);
        Dice d3 = new Dice(Colour.PURPLE);
        d1.setValue(1);
        d2.setValue(6);
        d3.setValue(3);
        instance.getPlayers().get(1).getDicePattern().setDice(new Position(0, 0), d1);
        instance.getPlayers().get(0).getDicePattern().setDice(new Position(1, 0), d2);
        instance.getPlayers().get(2).getDicePattern().setDice(new Position(3, 0), d3);
        //points obtained from private objective cards: player 1: 6    player 2: 1        player 3: 3

        assertEquals(instance.getPlayers().get(0), instance.selectWinner());

        //winner is player 2
        instance.getPlayers().get(0).setScore(8);
        instance.getPlayers().get(1).setScore(8);
        instance.getPlayers().get(2).setScore(8);
        d1.setValue(1);
        d2.setValue(1);
        d3.setValue(1);
        instance.getPlayers().get(1).getDicePattern().setDice(new Position(0, 0), d1);
        instance.getPlayers().get(0).getDicePattern().setDice(new Position(1, 0), d2);
        instance.getPlayers().get(2).getDicePattern().setDice(new Position(3, 0), d3);

        assertEquals(instance.getPlayers().get(2), instance.selectWinner());

        //winner is player 0

        instance.getPlayers().get(0).setWindowPattern(new WindowPattern("", 4, new Dice[4][5]));
        instance.getPlayers().get(1).setWindowPattern(new WindowPattern("", 4, new Dice[4][5]));
        instance.getPlayers().get(2).setWindowPattern(new WindowPattern("", 3, new Dice[4][5]));
        instance.getPlayers().get(0).setScore(8);
        instance.getPlayers().get(1).setScore(8);
        instance.getPlayers().get(2).setScore(8);
        d1.setValue(1);
        d2.setValue(1);
        d3.setValue(1);
        instance.getPlayers().get(1).getDicePattern().setDice(new Position(0, 0), d1);
        instance.getPlayers().get(0).getDicePattern().setDice(new Position(1, 0), d2);
        instance.getPlayers().get(2).getDicePattern().setDice(new Position(3, 0), d3);
        assertEquals(instance.getPlayers().get(0), instance.selectWinner());


    }
}
