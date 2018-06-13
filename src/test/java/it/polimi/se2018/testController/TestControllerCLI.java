package it.polimi.se2018.testController;

import it.polimi.se2018.controller.ControllerCLI;


import it.polimi.se2018.events.vcevent.*;
import it.polimi.se2018.model.*;
import it.polimi.se2018.view.VirtualViewCLI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestControllerCLI {

    private GameSingleton model;
    private ArrayList<Player> players;
    private ArrayList <PublicObjectiveCard> publicObjectiveCards;
    private ArrayList <ToolCard> toolCards;
    private RoundTrack roundTrack;
    private VirtualViewCLI view;
    private ControllerCLI controllerCLI;
    private GameSetupSingleton setup;

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
        toolCards.add(new ToolCard("","", Colour.BLUE, 1));
        toolCards.add(new ToolCard("","", Colour.PURPLE, 2));
        toolCards.add(new ToolCard("","", Colour.RED, 3));
        toolCards.add(new ToolCard("","", Colour.GREEN,4));
        toolCards.add(new ToolCard("","", Colour.GREEN,5));
        toolCards.add(new ToolCard("","", Colour.GREEN,6));
        toolCards.add(new ToolCard("","", Colour.GREEN,7));
        toolCards.add(new ToolCard("","", Colour.GREEN,8));
        toolCards.add(new ToolCard("","", Colour.GREEN,9));
        toolCards.add(new ToolCard("","", Colour.GREEN,10));
        toolCards.add(new ToolCard("","", Colour.GREEN,11));
        toolCards.add(new ToolCard("","", Colour.GREEN,12));
    }




    @Test(expected = IllegalArgumentException.class)
    public void testSetWindowPatternPlayerTrue(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));
        ArrayList<WindowPattern> windowPatterns = new ArrayList<>();
        windowPatterns.add(new WindowPattern("wp1", 4, new Dice[4][5]));
        windowPatterns.add(new WindowPattern("wp2", 3, new Dice[4][5]));
        windowPatterns.add(new WindowPattern("wp3", 2, new Dice[4][5]));
        windowPatterns.add(new WindowPattern("wp4", 1, new Dice[4][5]));
        for( WindowPattern card : windowPatterns)
            model.getPlayers().get(0).addWindowPattern(card);


        WindowPatternChoiceEvent event = new WindowPatternChoiceEvent("1");
        controllerCLI.update(view, event);
        assertEquals(windowPatterns.get(0), model.getPlayers().get(0).getWindowPattern() );

        model.setCurrentPlayer(model.getPlayers().get(0));
        event = new WindowPatternChoiceEvent("2");
        controllerCLI.update(view, event);
        assertEquals(windowPatterns.get(1), model.getPlayers().get(0).getWindowPattern() );

        model.setCurrentPlayer(model.getPlayers().get(0));
        event = new WindowPatternChoiceEvent("3");
        controllerCLI.update(view, event);
        assertEquals(windowPatterns.get(2), model.getPlayers().get(0).getWindowPattern() );

        model.setCurrentPlayer(model.getPlayers().get(0));
        event = new WindowPatternChoiceEvent("4");
        controllerCLI.update(view, event);
        assertEquals(windowPatterns.get(3),model.getPlayers().get(0).getWindowPattern() );

        model.setCurrentPlayer(model.getPlayers().get(0));
        new WindowPatternChoiceEvent("10");

    }

    @Test(expected = NullPointerException.class)
    public void testGrozingPliersFalse1(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));
        model.getCurrentPlayer().setWindowPattern(new WindowPattern("wp1", 4, new Dice[4][5]));
        model.getDraftPool().add(new Dice(1));
        model.getDraftPool().add(new Dice(6));
        model.getDraftPool().add(new Dice(3));


        GrozingPliersEvent event = new GrozingPliersEvent("1      1 ");
        controllerCLI.update(view,event);

    }

    @Test(expected = NullPointerException.class)
    public void testGrozingPliersFalse2(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));
        model.getCurrentPlayer().setWindowPattern(new WindowPattern("wp1", 4, new Dice[4][5]));
        model.getDraftPool().add(new Dice(1));
        model.getDraftPool().add(new Dice(6));
        model.getDraftPool().add(new Dice(3));


        GrozingPliersEvent event = new GrozingPliersEvent("2    2 ");
        controllerCLI.update(view,event);

    }

    @Test(expected = NullPointerException.class)
    public void testGrozingPliersFalse3(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));
        model.getCurrentPlayer().setWindowPattern(new WindowPattern("wp1", 4, new Dice[4][5]));
        model.getDraftPool().add(new Dice(1));
        model.getDraftPool().add(new Dice(6));
        model.getDraftPool().add(new Dice(3));


        GrozingPliersEvent event = new GrozingPliersEvent("4    2 ");
        controllerCLI.update(view,event);

    }

    @Test(expected = NullPointerException.class)
    public void testGrozingPliersTrue(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));
        model.getCurrentPlayer().setWindowPattern(new WindowPattern("wp1", 8, new Dice[4][5]));
        model.getDraftPool().add(new Dice(1));
        model.getDraftPool().add(new Dice(6));
        model.getDraftPool().add(new Dice(3));



        GrozingPliersEvent event = new GrozingPliersEvent("2      1 ");
        try {
            controllerCLI.update(view, event);
        } catch (NullPointerException e) {
            model.getCurrentPlayer().reduceFavorTokens(1);
        }
        assertEquals(5, model.getDraftPool().get(1).getValue() );


        event = new GrozingPliersEvent("3      1 ");
        try {
            controllerCLI.update(view, event);
        } catch (NullPointerException e) {
            model.getCurrentPlayer().reduceFavorTokens(2);
        }
        assertEquals(2, model.getDraftPool().get(2).getValue() );

        model.getDraftPool().removeAll(model.getDraftPool());
        model.getDraftPool().add(new Dice(1));
        model.getDraftPool().add(new Dice(6));
        model.getDraftPool().add(new Dice(3));


        event = new GrozingPliersEvent("1      2 ");
        try {
            controllerCLI.update(view, event);
        } catch (NullPointerException e) {
            model.getCurrentPlayer().reduceFavorTokens(2);
        }
        assertEquals(2, model.getDraftPool().get(0).getValue() );


        event = new GrozingPliersEvent("3      2 ");
        try {
            controllerCLI.update(view, event);
        } catch (NullPointerException e) {
            model.getCurrentPlayer().reduceFavorTokens(2);
        }
        assertEquals(4, model.getDraftPool().get(2).getValue() );

        //an exception is thrown beacuse player has not enough favor tokens
        event = new GrozingPliersEvent("3      2 ");
        controllerCLI.update(view, event);


    }


    @Test(expected = NullPointerException.class)
    public void testEglomiseBrushFalse1(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();

        //Via Lux
        Dice[][] diceMatrix = new Dice[4][5];
        diceMatrix[0][0] = new Dice(Colour.YELLOW);
        diceMatrix[0][2] = new Dice(6);
        diceMatrix[1][1] = new Dice(1);
        diceMatrix[1][2] = new Dice(5);
        diceMatrix[1][4] = new Dice(2);
        diceMatrix[2][0] = new Dice(3);
        diceMatrix[2][1] = new Dice(Colour.YELLOW);
        diceMatrix[2][2] = new Dice(Colour.RED);
        diceMatrix[2][3] = new Dice(Colour.PURPLE);
        diceMatrix[3][2] = new Dice(4);
        diceMatrix[3][3] = new Dice(3);
        diceMatrix[3][4] = new Dice(Colour.RED);

        model.setCurrentPlayer(model.getPlayers().get(0));
        WindowPattern windowPatternTest = new WindowPattern("Name", 8, diceMatrix);
        model.getCurrentPlayer().setWindowPattern(windowPatternTest);

        Dice diceGreen = new Dice(Colour.YELLOW);
        diceGreen.setValue(6);
        model.getCurrentPlayer().getDicePattern().setDice(new Position (2,1),diceGreen);

        Dice diceBlue = new Dice(Colour.BLUE);
        diceBlue.setValue(5);
        model.getCurrentPlayer().getDicePattern().setDice(new Position (1,2),diceBlue);

        Dice diceToMove = new Dice(Colour.YELLOW);
        diceToMove.setValue(5);
        model.getCurrentPlayer().getDicePattern().setDice(new Position (3,3), diceToMove );


        EglomiseBrushEvent event = new EglomiseBrushEvent("4 4 3 3 ");

        controllerCLI.update(view,event);

    }


    @Test(expected = NullPointerException.class)
    public void testEglomiseBrushFalse2() {
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();

        //Via Lux
        Dice[][] diceMatrix = new Dice[4][5];
        diceMatrix[0][0] = new Dice(Colour.YELLOW);
        diceMatrix[0][2] = new Dice(6);
        diceMatrix[1][1] = new Dice(1);
        diceMatrix[1][2] = new Dice(5);
        diceMatrix[1][4] = new Dice(2);
        diceMatrix[2][0] = new Dice(3);
        diceMatrix[2][1] = new Dice(Colour.YELLOW);
        diceMatrix[2][2] = new Dice(Colour.RED);
        diceMatrix[2][3] = new Dice(Colour.PURPLE);
        diceMatrix[3][2] = new Dice(4);
        diceMatrix[3][3] = new Dice(3);
        diceMatrix[3][4] = new Dice(Colour.RED);

        model.setCurrentPlayer(model.getPlayers().get(0));
        WindowPattern windowPatternTest = new WindowPattern("Name", 8, diceMatrix);
        model.getCurrentPlayer().setWindowPattern(windowPatternTest);

        Dice diceGreen = new Dice(Colour.YELLOW);
        diceGreen.setValue(6);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2, 1), diceGreen);

        Dice diceBlue = new Dice(Colour.BLUE);
        diceBlue.setValue(5);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(1, 2), diceBlue);

        Dice diceToMove = new Dice(Colour.YELLOW);
        diceToMove.setValue(5);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(3, 3), diceToMove);

        EglomiseBrushEvent event = new EglomiseBrushEvent("4 4 2 3 ");

        controllerCLI.update(view, event);
    }



    @Test
    public void testEglomiseBrushTrue(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();

        //Via Lux
        Dice[][] diceMatrix = new Dice[4][5];
        diceMatrix[0][0] = new Dice(Colour.YELLOW);
        diceMatrix[0][2] = new Dice(6);
        diceMatrix[1][1] = new Dice(1);
        diceMatrix[1][2] = new Dice(5);
        diceMatrix[1][4] = new Dice(2);
        diceMatrix[2][0] = new Dice(3);
        diceMatrix[2][1] = new Dice(Colour.YELLOW);
        diceMatrix[2][2] = new Dice(Colour.RED);
        diceMatrix[2][3] = new Dice(Colour.PURPLE);
        diceMatrix[3][2] = new Dice(4);
        diceMatrix[3][3] = new Dice(3);
        diceMatrix[3][4] = new Dice(Colour.RED);

        model.setCurrentPlayer(model.getPlayers().get(0));
        WindowPattern windowPatternTest = new WindowPattern("Name", 1, diceMatrix);
        model.getCurrentPlayer().setWindowPattern(windowPatternTest);


        Dice diceGreen = new Dice(Colour.GREEN);
        diceGreen.setValue(4);
        model.getCurrentPlayer().getDicePattern().setDice(new Position (3,2),diceGreen);

        Dice diceToMove = new Dice(Colour.BLUE);
        diceToMove.setValue(3);
        model.getCurrentPlayer().getDicePattern().setDice(new Position (3,3), diceToMove );


        EglomiseBrushEvent event = new EglomiseBrushEvent("4 4 3 3 ");

        controllerCLI.update(view,event);


        assertNull(model.getCurrentPlayer().getDicePattern().getDice(new Position(3, 3)));
        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position(2,2)), diceToMove);

    }


    @Test(expected = NullPointerException.class)
    public void testCopperFoilBurnisherFalse() {
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players, publicObjectiveCards, toolCards, roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view, toolCards, model, 1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));


        //Via Lux
        Dice[][] diceMatrix = new Dice[4][5];
        diceMatrix[0][0] = new Dice(Colour.YELLOW);
        diceMatrix[0][2] = new Dice(6);
        diceMatrix[1][1] = new Dice(1);
        diceMatrix[1][2] = new Dice(5);
        diceMatrix[1][4] = new Dice(2);
        diceMatrix[2][0] = new Dice(3);
        diceMatrix[2][1] = new Dice(Colour.YELLOW);
        diceMatrix[2][2] = new Dice(Colour.RED);
        diceMatrix[2][3] = new Dice(Colour.PURPLE);
        diceMatrix[3][2] = new Dice(4);
        diceMatrix[3][3] = new Dice(3);
        diceMatrix[3][4] = new Dice(Colour.RED);

        WindowPattern windowPatternTest = new WindowPattern("Name", 3, diceMatrix);
        model.getCurrentPlayer().setWindowPattern(windowPatternTest);


        //test1
        Dice diceToMove = new Dice(Colour.YELLOW);
        diceToMove.setValue(4);
        Position positionDiceToMove = new Position(2, 1);
        model.getCurrentPlayer().getDicePattern().setDice(positionDiceToMove, diceToMove);

        Dice dice2 = new Dice(Colour.RED);
        dice2.setValue(3);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2, 2), dice2);

        CopperFoilBurnisherEvent event = new CopperFoilBurnisherEvent("3 2 1 2");
        controllerCLI.update(view, event);


    }


    @Test(expected = NullPointerException.class)
    public void testCopperFoilBurnisherTrue(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));


        //Via Lux
        Dice[][] diceMatrix = new Dice[4][5];
        diceMatrix[0][0] = new Dice(Colour.YELLOW);
        diceMatrix[0][2] = new Dice(6);
        diceMatrix[1][1] = new Dice(1);
        diceMatrix[1][2] = new Dice(5);
        diceMatrix[1][4] = new Dice(2);
        diceMatrix[2][0] = new Dice(3);
        diceMatrix[2][1] = new Dice(Colour.YELLOW);
        diceMatrix[2][2] = new Dice(Colour.RED);
        diceMatrix[2][3] = new Dice(Colour.PURPLE);
        diceMatrix[3][2] = new Dice(4);
        diceMatrix[3][3] = new Dice(3);
        diceMatrix[3][4] = new Dice(Colour.RED);

        WindowPattern windowPatternTest = new WindowPattern("Name", 3, diceMatrix);
        model.getCurrentPlayer().setWindowPattern(windowPatternTest);


        //test1
        Dice diceToMove = new Dice(Colour.YELLOW);
        diceToMove.setValue(4);
        Position positionDiceToMove = new Position (2,1);
        model.getCurrentPlayer().getDicePattern().setDice(positionDiceToMove, diceToMove );

        Dice dice2 = new Dice(Colour.RED);
        dice2.setValue(3);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,2), dice2 );

        CopperFoilBurnisherEvent event = new CopperFoilBurnisherEvent("3 2 2 3");
        controllerCLI.update(view, event);

        assertNull(model.getCurrentPlayer().getDicePattern().getDice(positionDiceToMove));
        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position(1,2)), diceToMove);

        //test 2
        Dice dice3 = new Dice(Colour.PURPLE);
        dice3.setValue(6);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,3), dice3);

        event = new CopperFoilBurnisherEvent("3 4 4 3");
        controllerCLI.update(view, event);

        assertNull(model.getCurrentPlayer().getDicePattern().getDice(new Position(2, 3)));
        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position(3,2)), dice3);


        event = new CopperFoilBurnisherEvent("4 3 3 3 ");
        controllerCLI.update(view, event);
    }

    @Test(expected = NullPointerException.class)
    public void testLathekinTrue(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));


        //Via Lux
        Dice[][] diceMatrix = new Dice[4][5];
        diceMatrix[0][0] = new Dice(Colour.YELLOW);
        diceMatrix[0][2] = new Dice(6);
        diceMatrix[1][1] = new Dice(1);
        diceMatrix[1][2] = new Dice(5);
        diceMatrix[1][4] = new Dice(2);
        diceMatrix[2][0] = new Dice(3);
        diceMatrix[2][1] = new Dice(Colour.YELLOW);
        diceMatrix[2][2] = new Dice(Colour.RED);
        diceMatrix[2][3] = new Dice(Colour.PURPLE);
        diceMatrix[3][2] = new Dice(4);
        diceMatrix[3][3] = new Dice(3);
        diceMatrix[3][4] = new Dice(Colour.RED);

        WindowPattern windowPatternTest = new WindowPattern("Name", 3, diceMatrix);
        model.getCurrentPlayer().setWindowPattern(windowPatternTest);



        Dice diceY = new Dice(Colour.YELLOW);
        diceY.setValue(3);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,1), diceY);

        Dice diceR = new Dice(Colour.RED);
        diceR.setValue(4);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,2), diceR);

        Dice diceP = new Dice(Colour.PURPLE);
        diceP.setValue(1);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,3),diceP);

        Dice diceP2 = new Dice(Colour.PURPLE);
        diceP2.setValue(1);
        model.getCurrentPlayer().getDicePattern().setDice(new Position (1,1),diceP2);


        LathekinEvent event = new LathekinEvent("3 2 3 1 3 3 4 3 ");
        controllerCLI.update(view, event);

        assertNull(model.getCurrentPlayer().getDicePattern().getDice(new Position(2, 1)));
        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position(2,0)),diceY );
        assertNull(model.getCurrentPlayer().getDicePattern().getDice(new Position(2, 2)));
        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position(3,2)),diceR );

        event = new LathekinEvent("3 1 2 1 4 3 2 4");
        controllerCLI.update(view, event);

        assertNull(model.getCurrentPlayer().getDicePattern().getDice(new Position(2, 0)));
        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position(1,0)),diceY );
        assertNull(model.getCurrentPlayer().getDicePattern().getDice(new Position(3, 2)));
        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position(1,3)),diceR );

        event = new LathekinEvent("3 1 2 1 4 3 2 4");
        controllerCLI.update(view, event);

    }


    @Test( expected = NullPointerException.class)
    public void testLathekinFalse(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));

        //Via Lux
        Dice[][] diceMatrix = new Dice[4][5];
        diceMatrix[0][0] = new Dice(Colour.YELLOW);
        diceMatrix[0][2] = new Dice(6);
        diceMatrix[1][1] = new Dice(1);
        diceMatrix[1][2] = new Dice(5);
        diceMatrix[1][4] = new Dice(2);
        diceMatrix[2][0] = new Dice(3);
        diceMatrix[2][1] = new Dice(Colour.YELLOW);
        diceMatrix[2][2] = new Dice(Colour.RED);
        diceMatrix[2][3] = new Dice(Colour.PURPLE);
        diceMatrix[3][2] = new Dice(4);
        diceMatrix[3][3] = new Dice(3);
        diceMatrix[3][4] = new Dice(Colour.RED);

        WindowPattern windowPatternTest = new WindowPattern("Name", 8, diceMatrix);
        model.getCurrentPlayer().setWindowPattern(windowPatternTest);



        Dice diceY = new Dice(Colour.YELLOW);
        diceY.setValue(3);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,1), diceY);

        Dice diceR = new Dice(Colour.RED);
        diceR.setValue(4);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,2), diceR);

        Dice diceP = new Dice(Colour.PURPLE);
        diceP.setValue(1);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,3),diceP);

        Dice diceP2 = new Dice(Colour.PURPLE);
        diceP2.setValue(1);
        model.getCurrentPlayer().getDicePattern().setDice(new Position (1,1),diceP2);


        LathekinEvent event = new LathekinEvent("3 3 2 5 3 2 3 1");

        controllerCLI.update(view, event);

    }

    @Test
    public void testLensCutterTrue(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));
        model.getCurrentPlayer().setWindowPattern(new WindowPattern("wp1", 8, new Dice[4][5]));

        Dice dice1 = new Dice(Colour.PURPLE);
        dice1.setValue(2);
        Dice dice2 = new Dice(Colour.RED);
        dice2.setValue(5);
        Dice dice3 = new Dice(Colour.GREEN);
        dice3.setValue(4);

        model.getDraftPool().add(dice1);
        model.getDraftPool().add(dice2);
        model.getDraftPool().add(dice3);

        model.fromDraftPoolToRoundTrack();

        Dice dice4 = new Dice (Colour.YELLOW);
        dice4.setValue(5);
        model.getDraftPool().add(dice4);

        LensCutterEvent event = new LensCutterEvent("1 1 2");
        controllerCLI.update(view,event);

        List<Dice> list = new ArrayList<>();
        list.add(dice1);
        list.add(dice3);
        list.add(dice4);

        assertEquals(model.getDraftPool().get(model.getDraftPool().size()-1),dice2);
        assertEquals(list, model.getRoundTrack().getList(0));

    }

    @Test(expected = NullPointerException.class)
    public void testLensCutterFalse1() {
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));
        model.getCurrentPlayer().setWindowPattern(new WindowPattern("wp1", 8, new Dice[4][5]));

        model.getDraftPool().add(new Dice(1));
        Dice dice1 = new Dice(1);
        model.getDraftPool().add(dice1);
        model.getDraftPool().add(new Dice(1));

        model.fromDraftPoolToRoundTrack();

        LensCutterEvent event = new LensCutterEvent("1 6 2");
        controllerCLI.update(view, event);


    }

    @Test(expected = NullPointerException.class)
    public void testLensCutterFalse2() {
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));
        model.getCurrentPlayer().setWindowPattern(new WindowPattern("wp1", 8, new Dice[4][5]));

        model.getDraftPool().add(new Dice(2));
        Dice dice1 = new Dice(2);
        model.getDraftPool().add(dice1);
        model.getDraftPool().add(new Dice(2));

        model.fromDraftPoolToRoundTrack();

        Dice dice2 = new Dice(Colour.YELLOW);
        dice2.setValue(5);
        model.getDraftPool().add(dice2);

        LensCutterEvent event = new LensCutterEvent("5 1 2");
        controllerCLI.update(view, event);

    }

    @Test(expected = NullPointerException.class)
    public void testLensCutterFalse3() {
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));
        model.getCurrentPlayer().setWindowPattern(new WindowPattern("wp1", 8, new Dice[4][5]));

        model.getDraftPool().add(new Dice(3));
        Dice dice1 = new Dice(3);
        model.getDraftPool().add(dice1);
        model.getDraftPool().add(new Dice(3));

        model.fromDraftPoolToRoundTrack();

        Dice dice2 = new Dice(Colour.YELLOW);
        dice2.setValue(5);
        model.getDraftPool().add(dice2);

        LensCutterEvent event = new LensCutterEvent("1 1 8");
        controllerCLI.update(view, event);

    }


    @Test
    public void testFluxBrushChooseDice(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));

        WindowPattern windowPatternTest = new WindowPattern("Name", 8, new Dice [4][5]);
        model.getCurrentPlayer().setWindowPattern(windowPatternTest);

        Dice dice1 = new Dice(Colour.PURPLE);
        Dice dice2 = new Dice(Colour.BLUE);
        Dice dice3 = new Dice(Colour.RED);

        dice1.setValue(1);
        dice2.setValue(2);
        dice3.setValue(3);

        model.getDraftPool().add(dice1);
        model.getDraftPool().add(dice2);
        model.getDraftPool().add(dice3);

        int size = model.getDraftPool().size();

        FluxBrushChooseDiceEvent event = new FluxBrushChooseDiceEvent("1");
        try{
            controllerCLI.update(view,event);
        } catch (NullPointerException e){
            assertEquals(size, model.getDraftPool().size());
            assertEquals(dice2, model.getDraftPool().get(1));
            assertEquals(dice3, model.getDraftPool().get(2));
            assertEquals(dice1.getColour(), model.getDraftPool().get(0).getColour());
           // model.getDiceBag().add(new Dice(Colour.BLUE));   //TODO: è la riga di codice che ho aggiunto sul treno per far fronte al problema del dice bag in GameSingleton
        }

    }


    @Test (expected = NullPointerException.class)
    public void testFluxBrushChooseDiceFalse(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));

        WindowPattern windowPatternTest = new WindowPattern("Name", 2, new Dice [4][5]);
        model.getCurrentPlayer().setWindowPattern(windowPatternTest);

        Dice dice1 = new Dice(Colour.PURPLE);
        Dice dice2 = new Dice(Colour.BLUE);
        Dice dice3 = new Dice(Colour.RED);

        dice1.setValue(1);
        dice2.setValue(2);
        dice3.setValue(3);

        model.getDraftPool().add(dice1);
        model.getDraftPool().add(dice2);
        model.getDraftPool().add(dice3);

        FluxBrushChooseDiceEvent event = new FluxBrushChooseDiceEvent("5");

        controllerCLI.update(view,event);

    }


    @Test
    public void testFluxBrushPlaceDice(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));

        //Via Lux
        Dice[][] diceMatrix = new Dice[4][5];
        diceMatrix[0][0] = new Dice(Colour.YELLOW);
        diceMatrix[0][2] = new Dice(6);
        diceMatrix[1][1] = new Dice(1);
        diceMatrix[1][2] = new Dice(5);
        diceMatrix[1][4] = new Dice(2);
        diceMatrix[2][0] = new Dice(3);
        diceMatrix[2][1] = new Dice(Colour.YELLOW);
        diceMatrix[2][2] = new Dice(Colour.RED);
        diceMatrix[2][3] = new Dice(Colour.PURPLE);
        diceMatrix[3][2] = new Dice(4);
        diceMatrix[3][3] = new Dice(3);
        diceMatrix[3][4] = new Dice(Colour.RED);

        WindowPattern windowPatternTest = new WindowPattern("Name", 8, diceMatrix);
        model.getCurrentPlayer().setWindowPattern(windowPatternTest);

        Dice diceY = new Dice(Colour.YELLOW);
        diceY.setValue(3);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,1), diceY);

        Dice diceR = new Dice(Colour.RED);
        diceR.setValue(4);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,2), diceR);

        Dice diceP = new Dice(Colour.PURPLE);
        diceP.setValue(1);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,3),diceP);

        Dice diceP2 = new Dice(Colour.PURPLE);
        diceP2.setValue(1);


        FluxBrushPlaceDiceEvent event = new FluxBrushPlaceDiceEvent("3 5");
        controllerCLI.update(view, event);

    }


    @Test(expected = NullPointerException.class)
    public void testGlazingHammerTrue(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));
        WindowPattern windowPatternTest = new WindowPattern("Name", 2, new Dice [4][5]);
        model.getCurrentPlayer().setWindowPattern(windowPatternTest);
        model.setLap(1);

        Dice dice1 = new Dice(Colour.PURPLE);
        Dice dice2 = new Dice(Colour.BLUE);
        Dice dice3 = new Dice(Colour.RED);

        dice1.setValue(1);
        dice2.setValue(2);
        dice3.setValue(3);

        model.getDraftPool().add(dice1);
        model.getDraftPool().add(dice2);
        model.getDraftPool().add(dice3);

        int size = model.getDraftPool().size();

        GlazingHammerEvent event = new GlazingHammerEvent();
        controllerCLI.update(view,event);

        assertEquals(size, model.getDraftPool().size());
        assertEquals(dice1.getColour(), model.getDraftPool().get(0).getColour());
        assertEquals(dice2.getColour(), model.getDraftPool().get(1).getColour());
        assertEquals(dice3.getColour(), model.getDraftPool().get(2).getColour());


        event = new GlazingHammerEvent();
        controllerCLI.update(view,event);

    }

    @Test(expected = NullPointerException.class)
    public void testGlazingHammerFalse() {
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));
        WindowPattern windowPatternTest = new WindowPattern("Name", 8, new Dice[4][5]);
        model.getCurrentPlayer().setWindowPattern(windowPatternTest);
        model.setLap(0);

        Dice dice1 = new Dice(Colour.PURPLE);
        Dice dice2 = new Dice(Colour.BLUE);
        Dice dice3 = new Dice(Colour.RED);

        dice1.setValue(1);
        dice2.setValue(2);
        dice3.setValue(3);

        model.getDraftPool().add(dice1);
        model.getDraftPool().add(dice2);
        model.getDraftPool().add(dice3);

        GlazingHammerEvent event = new GlazingHammerEvent();

        controllerCLI.update(view, event);
    }


    @Test(expected = NullPointerException.class)
    public void testRunnerPliers(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));
        model.getCurrentPlayer().setLap(0);


        //Via Lux
        Dice[][] diceMatrix = new Dice[4][5];
        diceMatrix[0][0] = new Dice(Colour.YELLOW);
        diceMatrix[0][2] = new Dice(6);
        diceMatrix[1][1] = new Dice(1);
        diceMatrix[1][2] = new Dice(5);
        diceMatrix[1][4] = new Dice(2);
        diceMatrix[2][0] = new Dice(3);
        diceMatrix[2][1] = new Dice(Colour.YELLOW);
        diceMatrix[2][2] = new Dice(Colour.RED);
        diceMatrix[2][3] = new Dice(Colour.PURPLE);
        diceMatrix[3][2] = new Dice(4);
        diceMatrix[3][3] = new Dice(3);
        diceMatrix[3][4] = new Dice(Colour.RED);

        WindowPattern windowPatternTest = new WindowPattern("Name", 3, diceMatrix);
        model.getCurrentPlayer().setWindowPattern(windowPatternTest);



        Dice diceY = new Dice(Colour.YELLOW);
        diceY.setValue(3);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,1), diceY);

        Dice diceR = new Dice(Colour.RED);
        diceR.setValue(4);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,2), diceR);

        Dice diceP = new Dice(Colour.PURPLE);
        diceP.setValue(1);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,3),diceP);

        Dice diceP2 = new Dice(Colour.PURPLE);
        diceP2.setValue(1);
        model.getDraftPool().add(diceP2);

        RunnerPliersEvent event = new RunnerPliersEvent("1 2 2 ");
        controllerCLI.update(view,event);

        assertEquals(diceP2,model.getCurrentPlayer().getDicePattern().getDice(new Position (1,1)));
        assertEquals(0, model.getDraftPool().size());
        assertEquals(1,model.getCurrentPlayer().getLap());

        event = new RunnerPliersEvent("1 1 4 ");
        controllerCLI.update(view,event);

    }

    @Test (expected = NullPointerException.class)
    public void testCorkBakedStrightedge(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));
        //Via Lux
        Dice[][] diceMatrix = new Dice[4][5];
        diceMatrix[0][0] = new Dice(Colour.YELLOW);
        diceMatrix[0][2] = new Dice(6);
        diceMatrix[1][1] = new Dice(1);
        diceMatrix[1][2] = new Dice(5);
        diceMatrix[1][4] = new Dice(2);
        diceMatrix[2][0] = new Dice(3);
        diceMatrix[2][1] = new Dice(Colour.YELLOW);
        diceMatrix[2][2] = new Dice(Colour.RED);
        diceMatrix[2][3] = new Dice(Colour.PURPLE);
        diceMatrix[3][2] = new Dice(4);
        diceMatrix[3][3] = new Dice(3);
        diceMatrix[3][4] = new Dice(Colour.RED);

        WindowPattern windowPatternTest = new WindowPattern("Name", 3, diceMatrix);
        model.getCurrentPlayer().setWindowPattern(windowPatternTest);



        Dice diceY = new Dice(Colour.YELLOW);
        diceY.setValue(3);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,1), diceY);

        Dice diceR = new Dice(Colour.RED);
        diceR.setValue(4);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,2), diceR);

        Dice diceP = new Dice(Colour.PURPLE);
        diceP.setValue(1);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,3),diceP);

        Dice diceP2 = new Dice(Colour.PURPLE);
        diceP2.setValue(1);
        model.getDraftPool().add(diceP2);

        CorkBakedStraightedgeEvent event = new CorkBakedStraightedgeEvent("1 1 4");
        controllerCLI.update(view, event);

        assertEquals(0, model.getDraftPool().size());
        assertEquals(diceP2, model.getCurrentPlayer().getDicePattern().getDice(new Position(0,3)));

        Dice diceG = new Dice(Colour.GREEN);
        diceG.setValue(6);
        model.getDraftPool().add(diceG);

        event = new CorkBakedStraightedgeEvent("1 1 1");
        controllerCLI.update(view, event);

    }


    @Test (expected = NullPointerException.class)
    public void testGrindingStone(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));
        WindowPattern windowPatternTest = new WindowPattern("Name", 18, new Dice[4][5]);
        model.getCurrentPlayer().setWindowPattern(windowPatternTest);

        Dice dice1 = new Dice(Colour.PURPLE);
        Dice dice2 = new Dice(Colour.BLUE);
        Dice dice3 = new Dice(Colour.RED);
        Dice dice4 = new Dice(Colour.YELLOW);
        Dice dice5 = new Dice(Colour.GREEN);
        Dice dice6 = new Dice(Colour.RED);

        dice1.setValue(1);
        dice2.setValue(2);
        dice3.setValue(3);
        dice4.setValue(4);
        dice5.setValue(5);
        dice6.setValue(6);

        model.getDraftPool().add(dice1);
        model.getDraftPool().add(dice2);
        model.getDraftPool().add(dice3);
        model.getDraftPool().add(dice4);
        model.getDraftPool().add(dice5);
        model.getDraftPool().add(dice6);

        GrindingStoneEvent event = new GrindingStoneEvent("1");
        controllerCLI.update(view,event);

        assertEquals(6,model.getDraftPool().size());
        assertEquals("6F",model.getDraftPool().get(0).toString());

        event = new GrindingStoneEvent("2");
        controllerCLI.update(view,event);
        assertEquals(6,model.getDraftPool().size());
        assertEquals("5B",model.getDraftPool().get(1).toString());

        event = new GrindingStoneEvent("3");
        controllerCLI.update(view,event);
        assertEquals(6,model.getDraftPool().size());
        assertEquals("4R",model.getDraftPool().get(2).toString());

        event = new GrindingStoneEvent("4");
        controllerCLI.update(view,event);
        assertEquals(6,model.getDraftPool().size());
        assertEquals("3G",model.getDraftPool().get(3).toString());

        event = new GrindingStoneEvent("5");
        controllerCLI.update(view,event);
        assertEquals(6,model.getDraftPool().size());
        assertEquals("2V",model.getDraftPool().get(4).toString());

        event = new GrindingStoneEvent("6");
        controllerCLI.update(view,event);
        assertEquals(6,model.getDraftPool().size());
        assertEquals("1R",model.getDraftPool().get(5).toString());

        event = new GrindingStoneEvent("7");
        controllerCLI.update(view,event);


    }


    @Test
    public void testFluxRemoverChooseDice(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));

        Dice[][] diceMatrix = new Dice[4][5];
        diceMatrix[0][0] = new Dice(Colour.YELLOW);
        diceMatrix[0][2] = new Dice(6);
        diceMatrix[1][1] = new Dice(1);
        diceMatrix[1][2] = new Dice(5);
        diceMatrix[1][4] = new Dice(2);
        diceMatrix[2][0] = new Dice(3);
        diceMatrix[2][1] = new Dice(Colour.YELLOW);
        diceMatrix[2][2] = new Dice(Colour.RED);
        diceMatrix[2][3] = new Dice(Colour.PURPLE);
        diceMatrix[3][2] = new Dice(4);
        diceMatrix[3][3] = new Dice(3);
        diceMatrix[3][4] = new Dice(Colour.RED);

        WindowPattern windowPatternTest = new WindowPattern("Name", 3, diceMatrix);
        model.getCurrentPlayer().setWindowPattern(windowPatternTest);

        Dice dice1 = new Dice(Colour.PURPLE);
        Dice dice2 = new Dice(Colour.BLUE);
        Dice dice3 = new Dice(Colour.RED);
        Dice dice4 = new Dice(Colour.YELLOW);
        Dice dice5 = new Dice(Colour.GREEN);

        dice1.setValue(1);
        dice2.setValue(2);
        dice3.setValue(3);
        dice4.setValue(4);
        dice5.setValue(5);

        model.getDraftPool().add(dice1);
        model.getDraftPool().add(dice2);
        model.getDraftPool().add(dice3);
        model.getDraftPool().add(dice4);
        model.getDraftPool().add(dice5);

        for (Dice dice: model.getDiceBag()) {
            if (dice.getColour().equals(Colour.PURPLE)) {
                model.getDiceBag().remove(dice);
                break;
            }
        }
        for (Dice dice: model.getDiceBag()) {
            if (dice.getColour().equals(Colour.BLUE)) {
                model.getDiceBag().remove(dice);
                break;
            }
        }
        for (Dice dice: model.getDiceBag()) {
            if (dice.getColour().equals(Colour.RED)) {
                model.getDiceBag().remove(dice);
                break;
            }
        }
        for (Dice dice: model.getDiceBag()) {
            if (dice.getColour().equals(Colour.YELLOW)) {
                model.getDiceBag().remove(dice);
                break;
            }
        }
        for (Dice dice: model.getDiceBag()) {
            if (dice.getColour().equals(Colour.GREEN)) {
                model.getDiceBag().remove(dice);
                break;
            }
        }


        FluxRemoverChooseDiceEvent event = new FluxRemoverChooseDiceEvent("3");
        try {
            controllerCLI.update(view, event);
        } catch (NullPointerException ignored) {}
        assertEquals(5, model.getDraftPool().size());
        assertFalse(model.getDraftPool().contains(dice3));
        assertEquals(dice3, model.getDiceBag().get(model.getDiceBag().size() - 1));

        for (Dice dice : model.getDraftPool())
            model.getDiceBag().add(dice);
    }

    @Test
    public void testFluxRemoverPlaceDiceTrue(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));

        //Via Lux
        Dice[][] diceMatrix = new Dice[4][5];
        diceMatrix[0][0] = new Dice(Colour.YELLOW);
        diceMatrix[0][2] = new Dice(6);
        diceMatrix[1][1] = new Dice(1);
        diceMatrix[1][2] = new Dice(5);
        diceMatrix[1][4] = new Dice(2);
        diceMatrix[2][0] = new Dice(3);
        diceMatrix[2][1] = new Dice(Colour.YELLOW);
        diceMatrix[2][2] = new Dice(Colour.RED);
        diceMatrix[2][3] = new Dice(Colour.PURPLE);
        diceMatrix[3][2] = new Dice(4);
        diceMatrix[3][3] = new Dice(3);
        diceMatrix[3][4] = new Dice(Colour.RED);

        WindowPattern windowPatternTest = new WindowPattern("Name", 1, diceMatrix);
        model.getCurrentPlayer().setWindowPattern(windowPatternTest);



        Dice diceY = new Dice(Colour.YELLOW);
        diceY.setValue(3);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,1), diceY);

        Dice diceR = new Dice(Colour.RED);
        diceR.setValue(4);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,2), diceR);

        Dice diceP = new Dice(Colour.PURPLE);
        diceP.setValue(1);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,3),diceP);

        Dice diceP2 = new Dice(Colour.PURPLE);
        diceP2.setValue(1);


        Dice tmp =new Dice(Colour.BLUE);
        controllerCLI.setDiceForFlux(tmp);

        FluxRemoverPlaceDiceEvent event = new FluxRemoverPlaceDiceEvent("4 3 5");
        controllerCLI.update(view,event);

        tmp.setValue(4);
        assertEquals(controllerCLI.getDiceForFlux(),model.getCurrentPlayer().getDicePattern().getDice(new Position(2,4)));
        assertEquals(controllerCLI.getDiceForFlux(),tmp);


    }

    @Test(expected = NullPointerException.class)
    public void testFluxRemoverPlaceDiceFalse(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));

        //Via Lux
        Dice[][] diceMatrix = new Dice[4][5];
        diceMatrix[0][0] = new Dice(Colour.YELLOW);
        diceMatrix[0][2] = new Dice(6);
        diceMatrix[1][1] = new Dice(1);
        diceMatrix[1][2] = new Dice(5);
        diceMatrix[1][4] = new Dice(2);
        diceMatrix[2][0] = new Dice(3);
        diceMatrix[2][1] = new Dice(Colour.YELLOW);
        diceMatrix[2][2] = new Dice(Colour.RED);
        diceMatrix[2][3] = new Dice(Colour.PURPLE);
        diceMatrix[3][2] = new Dice(4);
        diceMatrix[3][3] = new Dice(3);
        diceMatrix[3][4] = new Dice(Colour.RED);

        WindowPattern windowPatternTest = new WindowPattern("Name", 1, diceMatrix);
        model.getCurrentPlayer().setWindowPattern(windowPatternTest);



        Dice diceY = new Dice(Colour.YELLOW);
        diceY.setValue(3);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,1), diceY);

        Dice diceR = new Dice(Colour.RED);
        diceR.setValue(4);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,2), diceR);

        Dice diceP = new Dice(Colour.PURPLE);
        diceP.setValue(1);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,3),diceP);

        Dice diceP2 = new Dice(Colour.PURPLE);
        diceP2.setValue(1);


        Dice tmp =new Dice(Colour.BLUE);
        controllerCLI.setDiceForFlux(tmp);

        FluxRemoverPlaceDiceEvent event = new FluxRemoverPlaceDiceEvent("4 3 4");
        controllerCLI.update(view,event);
    }


    @Test(expected = NullPointerException.class)
    public void testTapWheel(){
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));
        //Via Lux
        Dice[][] diceMatrix = new Dice[4][5];
        diceMatrix[0][0] = new Dice(Colour.YELLOW);
        diceMatrix[0][2] = new Dice(6);
        diceMatrix[1][1] = new Dice(1);
        diceMatrix[1][2] = new Dice(5);
        diceMatrix[1][4] = new Dice(2);
        diceMatrix[2][0] = new Dice(3);
        diceMatrix[2][1] = new Dice(Colour.YELLOW);
        diceMatrix[2][2] = new Dice(Colour.RED);
        diceMatrix[2][3] = new Dice(Colour.PURPLE);
        diceMatrix[3][2] = new Dice(4);
        diceMatrix[3][3] = new Dice(3);
        diceMatrix[3][4] = new Dice(Colour.RED);

        WindowPattern windowPatternTest = new WindowPattern("Name", 7, diceMatrix);
        model.getCurrentPlayer().setWindowPattern(windowPatternTest);


        Dice diceP2 = new Dice(Colour.PURPLE);
        diceP2.setValue(3);
        model.getCurrentPlayer().getDicePattern().placeDice(new Position(3,0),diceP2);


        Dice diceY = new Dice(Colour.YELLOW);
        diceY.setValue(2);
        model.getCurrentPlayer().getDicePattern().placeDice(new Position(2,1), diceY);

        Dice diceR = new Dice(Colour.RED);
        diceR.setValue(4);
        model.getCurrentPlayer().getDicePattern().placeDice(new Position(2,2), diceR);

        Dice diceP = new Dice(Colour.PURPLE);
        diceP.setValue(1);
        model.getCurrentPlayer().getDicePattern().placeDice(new Position(2,3),diceP);


        Dice dice1 = new Dice(Colour.PURPLE);
        Dice dice2 = new Dice(Colour.BLUE);
        Dice dice3 = new Dice(Colour.RED);
        Dice dice4 = new Dice(Colour.YELLOW);

        dice1.setValue(1);
        dice2.setValue(2);
        dice3.setValue(3);
        dice4.setValue(4);

        model.getDraftPool().add(dice1);
        model.getDraftPool().add(dice2);
        model.getDraftPool().add(dice3);
        model.getDraftPool().add(dice4);

        model.fromDraftPoolToRoundTrack();

        TapWheelEvent event = new TapWheelEvent("1 1 4 1 4 2 ");
        controllerCLI.update(view,event);


        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position(3,1)), diceP2);


        event = new TapWheelEvent("1 1 3 4 2 4 4 2 4 1");
        controllerCLI.update(view,event);

        event = new TapWheelEvent("1 1 4 1 4 3");
        controllerCLI.update(view, event);

    }

    @Test(expected = NullPointerException.class)
    public void testPlaceDiceFromDraftPoolToDicePattern(){

        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();
        model.setCurrentPlayer(model.getPlayers().get(0));


        //Via Lux
        Dice[][] diceMatrix = new Dice[4][5];
        diceMatrix[0][0] = new Dice(Colour.YELLOW);
        diceMatrix[0][2] = new Dice(6);
        diceMatrix[1][1] = new Dice(1);
        diceMatrix[1][2] = new Dice(5);
        diceMatrix[1][4] = new Dice(2);
        diceMatrix[2][0] = new Dice(3);
        diceMatrix[2][1] = new Dice(Colour.YELLOW);
        diceMatrix[2][2] = new Dice(Colour.RED);
        diceMatrix[2][3] = new Dice(Colour.PURPLE);
        diceMatrix[3][2] = new Dice(4);
        diceMatrix[3][3] = new Dice(3);
        diceMatrix[3][4] = new Dice(Colour.RED);

        WindowPattern windowPatternTest = new WindowPattern("Name", 8, diceMatrix);
        model.getCurrentPlayer().setWindowPattern(windowPatternTest);



        Dice diceY = new Dice(Colour.YELLOW);
        diceY.setValue(3);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,1), diceY);

        Dice diceR = new Dice(Colour.RED);
        diceR.setValue(4);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,2), diceR);

        Dice diceP = new Dice(Colour.PURPLE);
        diceP.setValue(1);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,3),diceP);

        Dice diceP2 = new Dice(Colour.PURPLE);
        diceP2.setValue(1);
        model.getCurrentPlayer().getDicePattern().setDice(new Position (1,1),diceP2);


        Dice dice1 = new Dice(Colour.GREEN);
        dice1.setValue(5);

        Dice dice2 = new Dice(Colour.RED);
        dice2.setValue(2);

        Dice dice3 = new Dice(Colour.BLUE);
        dice3.setValue(2);

        model.getDraftPool().add(dice1);
        model.getDraftPool().add(dice2);
        model.getDraftPool().add(dice3);

        PlaceDiceEvent event = new PlaceDiceEvent("1 2 3 ");
        controllerCLI.update(view, event);
        assertEquals(2,model.getDraftPool().size());
        assertEquals(dice1, model.getCurrentPlayer().getDicePattern().getDice(new Position(1,2)));

        event = new PlaceDiceEvent("1 2 5 ");
        controllerCLI.update(view,event);
        assertEquals(1,model.getDraftPool().size());
        assertEquals(dice2, model.getCurrentPlayer().getDicePattern().getDice(new Position(1,4)));

        event = new PlaceDiceEvent("1  2 4 ");
        controllerCLI.update(view,event);
    }






    @After
    public void tearDown(){
        model.getDraftPool().removeAll(model.getDraftPool());
        model.getRoundTrack().removeAll();
        model = null;
        view = null;
        controllerCLI = null;
        setup = null;

    }

}
