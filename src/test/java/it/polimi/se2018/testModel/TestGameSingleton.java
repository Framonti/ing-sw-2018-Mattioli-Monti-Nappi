package it.polimi.se2018.testModel;

import it.polimi.se2018.model.*;
import org.junit.Before;

import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestGameSingleton {
    private ArrayList <Player> players;
    private ArrayList <PublicObjectiveCard> publicObjectiveCards;
    private ArrayList <ToolCard> toolCards;
    private RoundTrack roundTrack;

    @Before
    public void setUp(){
        players = new ArrayList<>();
        publicObjectiveCards = new ArrayList<>();
        toolCards = new ArrayList<>();
        players.add(new Player("daniele"));
        players.add(new Player("fabio"));
        players.add(new Player("francesco"));

        for (Player player: players) {
            if (!player.getPrivateObjectiveCards().isEmpty())
                player.getPrivateObjectiveCards().clear();
        }
        players.get(0).addPrivateObjectiveCard(new PrivateObjectiveCard("carta1", "carta di daniele", Colour.BLUE));
        players.get(1).addPrivateObjectiveCard(new PrivateObjectiveCard("carta2", "carta di fabio", Colour.GREEN));
        players.get(2).addPrivateObjectiveCard(new PrivateObjectiveCard("carta3", "carta di francesco", Colour.PURPLE));

        publicObjectiveCards.add(new PublicObjectiveCard("", "", 3));
        publicObjectiveCards.add(new PublicObjectiveCard("", "", 2));
        publicObjectiveCards.add(new PublicObjectiveCard("", "", 1));
        toolCards.add(new ToolCard("","", Colour.BLUE, 1));
        toolCards.add(new ToolCard("","", Colour.PURPLE, 2));
        toolCards.add(new ToolCard("","", Colour.RED, 3));
        toolCards.add(new ToolCard("","", Colour.GREEN,4));
        roundTrack = new RoundTrack();



    }

    @Test
    public void testCreateDiceBag(){
        GameSingleton instance = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        assertEquals(90,instance.getDiceBag().size());

        //controllo che ci siano 18 dadi per ogni colore
        int y = 0;
        int p = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        for(Dice dice : instance.getDiceBag()){
            switch (dice.getColour()) {
                case PURPLE:
                    p++;
                    break;
                case BLUE:
                    b++;
                    break;
                case RED:
                    r++;
                    break;
                case YELLOW:
                    y++;
                    break;
                case GREEN:
                    g++;
                    break;
                default:
                    break;
            }
        }
        assertEquals(18, p);
        assertEquals(18, b);
        assertEquals(18, r);
        assertEquals(18, y);
        assertEquals(18, g);
                //instance.getDiceBag().removeAll(instance.getDiceBag());
    }

    @Test
    public void testIncreaseRoundTrue(){
        try{
            GameSingleton instance = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
            instance.setRound(0);
            instance.increaseRound();
            assertEquals(1,instance.getRound());
        } catch (IllegalArgumentException exception){
            fail();
        }

        try{
            GameSingleton instance = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
            instance.setRound(8);
            instance.increaseRound();
            assertEquals(9,instance.getRound());
        } catch (IllegalArgumentException exception){
            fail();
        }

        try{
            GameSingleton instance = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
            instance.setRound(3);
            instance.increaseRound();
            assertEquals(4,instance.getRound());
        } catch (IllegalArgumentException exception){
            fail();
        }
    }




    @Test
    public void testExtractAndRoll(){
        GameSingleton instance = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
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
        instance.getDraftPool().removeAll(instance.getDraftPool());
        assertEquals(90,instance.getDiceBag().size());
        assertTrue(b);
        //instance.getDiceBag().removeAll(instance.getDiceBag());

    }

    @Test
    public void testExtractAndRollOneDice(){
        GameSingleton instance = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        instance.extractAndRollOneDiceWithoutReturning();
        assertEquals(89,instance.getDiceBag().size());
        assertEquals(1,instance.getDraftPool().size());

        instance.extractAndRollOneDiceWithoutReturning();
        assertEquals(88,instance.getDiceBag().size());
        assertEquals(2,instance.getDraftPool().size());

        instance.extractAndRollOneDiceWithoutReturning();
        assertEquals(87,instance.getDiceBag().size());
        assertEquals(3,instance.getDraftPool().size());

        //rimetto i dadi nel diceBag
        boolean b = instance.getDiceBag().addAll(instance.getDraftPool());
        instance.getDraftPool().removeAll(instance.getDraftPool());
        assertEquals(90,instance.getDiceBag().size());
        assertTrue(b);
        //instance.getDiceBag().removeAll(instance.getDiceBag());



    }


    //TODO analizzare il caso di vittoria e i vari casi di pareggio
    @Test
    public void testSelectWinner(){
        GameSingleton instance = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        instance.getPlayers().get(0).setWindowPattern(new WindowPattern("", 4, new Dice[4][5]), false);
        instance.getPlayers().get(1).setWindowPattern(new WindowPattern("", 3, new Dice[4][5]), false);
        instance.getPlayers().get(2).setWindowPattern(new WindowPattern("", 5, new Dice[4][5]), false);

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

        Player winner = instance.getPlayers().get(0);
        for (Player player: instance.getPlayers()) {
            if (player.getPrivateObjectiveCards().get(0).getColour().equals(Colour.GREEN))
                winner = player;
        }
        for (Player player: instance.getPlayers()) {
            if (player.getPrivateObjectiveCards().get(0).getColour().equals(Colour.PURPLE))
                winner = player;
        }
        for (Player player: instance.getPlayers()) {
            if (player.getPrivateObjectiveCards().get(0).getColour().equals(Colour.BLUE))
                winner = player;
        }
        assertEquals(winner, instance.selectWinner());

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

        instance.getPlayers().get(0).setWindowPattern(new WindowPattern("", 4, new Dice[4][5]), false);
        instance.getPlayers().get(1).setWindowPattern(new WindowPattern("", 4, new Dice[4][5]), false);
        instance.getPlayers().get(2).setWindowPattern(new WindowPattern("", 3, new Dice[4][5]), false);
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

    @Test
    public void testFromDraftPoolToRoundTrack(){
        GameSingleton instance = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        List<Dice> pool = new ArrayList<>();
        Dice dice1 = new Dice (Colour.PURPLE);
        dice1.setValue(1);
        Dice dice2 = new Dice (Colour.GREEN);
        dice2.setValue(1);
        Dice dice3 = new Dice (Colour.YELLOW);
        dice3.setValue(1);

        pool.add(dice1);
        pool.add(dice2);
        pool.add(dice3);

        instance.getDraftPool().add(dice1);
        instance.getDraftPool().add(dice2);
        instance.getDraftPool().add(dice3);


        instance.fromDraftPoolToRoundTrack();

        assertEquals(pool,instance.getRoundTrack().getList(0));


    }

    @Test
    public void testDicePatternsToString() {
        GameSingleton instance = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        List<String> methodOutput1 = instance.dicePatternsToString();
        List<List<String>> methodOutput2 = instance.dicePatternsToStringPath();
        for (int i = 0; i < players.size(); i++) {
            assertEquals(instance.getPlayers().get(i).getDicePattern().toString(), methodOutput1.get(i));
            assertEquals(instance.getPlayers().get(i).getDicePattern().dicePatternToStringPath(), methodOutput2.get(i));
        }
    }

    @Test
    public void testPublicObjectiveCardsToString() {
        GameSingleton instance = GameSingleton.instance(players, publicObjectiveCards, toolCards, roundTrack);
        List<String> methodOutput1 = instance.publicObjectiveCardsToString();
        List<String> methodOutput2 = instance.publicObjectiveCardsToStringPath();
        for (int i = 0; i < instance.getPublicObjectiveCards().size(); i++) {
            assertEquals(instance.getPublicObjectiveCards().get(i).toString(), methodOutput1.get(i));
            assertEquals("src/main/Images/PublicObjectiveCard/" +
                    instance.getPublicObjectiveCards().get(i).getName().replaceAll("\\s", "") +
                    ".jpg", methodOutput2.get(i));
        }
    }

    @Test
    public void testToolCardsToStringAbbreviated() {
        GameSingleton instance = GameSingleton.instance(players, publicObjectiveCards, toolCards, roundTrack);
        List<String> methodOutput = instance.toolCardsToStringAbbreviated();
        for (int i = 0; i < instance.getToolCards().size(); i++) {
            assertEquals( instance.getToolCards().get(i).getId() + ")\t" +
                    instance.getToolCards().get(i).getName() + "\n\tPrezzo: " +
                    (instance.getToolCards().get(i).getFavorPoint() == 0 ? 1 : 2) + "\n", methodOutput.get(i));
        }
    }

    @Test
    public void testWindowPatternsToStringPath() {
        GameSingleton instance = GameSingleton.instance(players, publicObjectiveCards, toolCards, roundTrack);
        List<String> methodOutput = instance.windowPatternsToStringPath();
        for (int i = 0; i < players.size(); i++) {
            assertEquals(instance.getPlayers().get(i).getDicePattern().getWindowPattern().toStringPath(), methodOutput.get(i));
        }
    }

    @Test
    public void testGetFavorTokensNumberPlayers() {
        GameSingleton instance = GameSingleton.instance(players, publicObjectiveCards, toolCards, roundTrack);
        List<String> methodOutput = instance.getFavorTokensNumberPlayers();
        for (int i = 0; i < instance.getPlayersNumber(); i++) {
            assertEquals(Integer.toString(instance.getPlayers().get(i).getFavorTokensNumber()), methodOutput.get(i));
        }
    }

    @Test
    public void testCreateScoreTrackAndLastPlayer() {
        GameSingleton instance = GameSingleton.instance(players, publicObjectiveCards, toolCards, roundTrack);
        Dice[][] dices = new Dice[4][5];
        WindowPattern wp = new WindowPattern("wp", 4, dices);
        List<Integer> scores = new ArrayList<>();
        for (Player player: instance.getPlayers()) {
            player.setWindowPattern(wp, false);
            player.computeMyScore(publicObjectiveCards);
            scores.add(player.getScore());
        }
        instance.createScoreTrack(scores);
        instance.lastPlayer();
    }

}
