package it.polimi.se2018.testController;

import it.polimi.se2018.WindowPatternCardDeckBuilder;
import it.polimi.se2018.controller.ControllerCLI;
import it.polimi.se2018.events.mvevent.FavorTokensEvent;
import it.polimi.se2018.events.mvevent.MVEvent;
import it.polimi.se2018.events.vcevent.*;
import it.polimi.se2018.model.*;
import it.polimi.se2018.view.VirtualViewCLI;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestControllerCLI {

    private GameSingleton model;
    private ArrayList<Player> players;
    private ArrayList <PublicObjectiveCard> publicObjectiveCards;
    private ArrayList <ToolCard> toolCards;
    private ScoreTrack scoreTrack;
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
        scoreTrack = new ScoreTrack(players);
        roundTrack = new RoundTrack();
        model = GameSingleton.instance(players,publicObjectiveCards,toolCards,roundTrack,scoreTrack);
        view = new VirtualViewCLI();
        controllerCLI = new ControllerCLI(view,toolCards,model,1000);
        setup = GameSetupSingleton.instance();


    }

    @Test
    public void testSetWindowPatternPlayerTrue(){
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

    }



    @Test(expected = NullPointerException.class)
    public void testGrozinPliersFalse1(){
        model.setCurrentPlayer(model.getPlayers().get(0));
        model.getCurrentPlayer().setWindowPattern(new WindowPattern("wp1", 4, new Dice[4][5]));
        model.getDraftPool().add(new Dice(1));
        model.getDraftPool().add(new Dice(6));
        model.getDraftPool().add(new Dice(3));


        GrozingPliersEvent event = new GrozingPliersEvent("1      1 ");
        controllerCLI.update(view,event);

    }

    @Test(expected = NullPointerException.class)
    public void testGrozinPliersFalse2(){
        model.setCurrentPlayer(model.getPlayers().get(0));
        model.getCurrentPlayer().setWindowPattern(new WindowPattern("wp1", 4, new Dice[4][5]));
        model.getDraftPool().add(new Dice(1));
        model.getDraftPool().add(new Dice(6));
        model.getDraftPool().add(new Dice(3));


        GrozingPliersEvent event = new GrozingPliersEvent("2    2 ");
        controllerCLI.update(view,event);

    }

    @Test(expected = NullPointerException.class)
    public void testGrozingPliersTrue(){
        model.setCurrentPlayer(model.getPlayers().get(0));
        model.getCurrentPlayer().setWindowPattern(new WindowPattern("wp1", 8, new Dice[4][5]));
        model.getDraftPool().add(new Dice(1));
        model.getDraftPool().add(new Dice(6));
        model.getDraftPool().add(new Dice(3));



        GrozingPliersEvent event = new GrozingPliersEvent("2      1 ");
        controllerCLI.update(view, event);
        assertEquals(5, model.getDraftPool().get(1).getValue() );


        event = new GrozingPliersEvent("3      1 ");
        controllerCLI.update(view, event);
        assertEquals(2, model.getDraftPool().get(2).getValue() );

        model.getDraftPool().removeAll(model.getDraftPool());
        model.getDraftPool().add(new Dice(1));
        model.getDraftPool().add(new Dice(6));
        model.getDraftPool().add(new Dice(3));


        event = new GrozingPliersEvent("1      2 ");
        controllerCLI.update(view, event);
        assertEquals(2, model.getDraftPool().get(0).getValue() );





        event = new GrozingPliersEvent("3      2 ");
        controllerCLI.update(view, event);
        assertEquals(4, model.getDraftPool().get(2).getValue() );

        //an exception is thrown beacuse player has not enough favor tokens
        event = new GrozingPliersEvent("3      2 ");
        controllerCLI.update(view, event);


    }


    @Test(expected = NullPointerException.class)
    public void testEglomiseBrushFalse1(){

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

        System.out.println(model.getCurrentPlayer().getDicePattern());

        Dice diceGreen = new Dice(Colour.GREEN);
        diceGreen.setValue(4);
        model.getCurrentPlayer().getDicePattern().setDice(new Position (3,2),diceGreen);

        Dice diceToMove = new Dice(Colour.BLUE);
        diceToMove.setValue(3);
        model.getCurrentPlayer().getDicePattern().setDice(new Position (3,3), diceToMove );


        System.out.println(model.getCurrentPlayer().getDicePattern());
        EglomiseBrushEvent event = new EglomiseBrushEvent("4 4 3 3 ");

        controllerCLI.update(view,event);


        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position (3,3)), null);
        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position(2,2)), diceToMove);
    }


    @Test(expected = NullPointerException.class)
    public void testCopperFoilBurnisher(){
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

        System.out.println(model.getCurrentPlayer().getDicePattern());

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
        System.out.println(model.getCurrentPlayer().getDicePattern());
        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(positionDiceToMove), null);
        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position(1,2)), diceToMove);

        //test 2
        Dice dice3 = new Dice(Colour.PURPLE);
        dice3.setValue(6);
        model.getCurrentPlayer().getDicePattern().setDice(new Position(2,3), dice3);

        event = new CopperFoilBurnisherEvent("3 4 4 3");
        controllerCLI.update(view, event);
        System.out.println(model.getCurrentPlayer().getDicePattern());

        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position(2,3)), null);
        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position(3,2)), dice3);



        event = new CopperFoilBurnisherEvent("4 3 3 3 ");
        controllerCLI.update(view, event);
    }

    @Test
    public void testLathekinTrue(){
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

        System.out.println(model.getCurrentPlayer().getDicePattern());


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

        System.out.println(model.getCurrentPlayer().getDicePattern());

        LathekinEvent event = new LathekinEvent("3 2 3 1 3 3 4 3 ");

        controllerCLI.update(view, event);

        System.out.println(model.getCurrentPlayer().getDicePattern());
        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position(2,1)), null);
        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position(2,0)),diceY );
        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position(2,2)), null);
        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position(3,2)),diceR );

        event = new LathekinEvent("3 1 2 1 4 3 2 4");

        controllerCLI.update(view, event);

        System.out.println(model.getCurrentPlayer().getDicePattern());
        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position(2,0)), null);
        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position(1,0)),diceY );
        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position(3,2)), null);
        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position(1,3)),diceR );

    }


    @Test( expected = NullPointerException.class)
    public void testLathekinFalse(){
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

        System.out.println(model.getCurrentPlayer().getDicePattern());


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

        System.out.println(model.getCurrentPlayer().getDicePattern());

        LathekinEvent event = new LathekinEvent("3 3 2 5 3 2 3 1");

        controllerCLI.update(view, event);

        System.out.println(model.getCurrentPlayer().getDicePattern());


    }

    @Test
    public void testLensCutterTrue(){
        model.setCurrentPlayer(model.getPlayers().get(0));
        model.getCurrentPlayer().setWindowPattern(new WindowPattern("wp1", 8, new Dice[4][5]));

        model.getDraftPool().add(new Dice(1));
        Dice dice1 = new Dice(6);
        model.getDraftPool().add(dice1);
        model.getDraftPool().add(new Dice(3));
        System.out.println("draft pool iniziale\n"+ model.getDraftPool());

        model.fromDraftPoolToRoundTrack();
        System.out.println("roundtrack dopo che ho spostato la prima draftpool\n" + model.getRoundTrack().toString());
        System.out.println("draft pool dopo che l'ho svuotata\n"+ model.getDraftPool());

        Dice dice2 = new Dice (Colour.YELLOW);
        dice2.setValue(5);
        model.getDraftPool().add(dice2);
        System.out.println("draft pool con nuovo dado\n"+ model.getDraftPool());

        LensCutterEvent event = new LensCutterEvent("1 1 2");
        controllerCLI.update(view,event);

        System.out.println("round track dopo evento\n"+model.getRoundTrack());
        System.out.println(" draft pool dopo evento\n"+model.getDraftPool());


        List<Dice> currentRoundTrack = new ArrayList<>();
        currentRoundTrack.add(new Dice(1));
        currentRoundTrack.add(new Dice(3));
        currentRoundTrack.add(dice2);
        //System.out.println(currentRoundTrack);
        //System.out.println(model.getRoundTrack().getList(0));
        assertEquals(model.getDraftPool().get(model.getDraftPool().size()-1),dice1);
        //TODO: controllare l'errore qui
        //assertEquals(currentRoundTrack, model.getRoundTrack().getList(0));

    }

    @Test(expected = NullPointerException.class)
    public void testLensCutterFalse1() {
        model.setCurrentPlayer(model.getPlayers().get(0));
        model.getCurrentPlayer().setWindowPattern(new WindowPattern("wp1", 8, new Dice[4][5]));

        model.getDraftPool().add(new Dice(1));
        Dice dice1 = new Dice(6);
        model.getDraftPool().add(dice1);
        model.getDraftPool().add(new Dice(3));
        System.out.println("draft pool iniziale\n" + model.getDraftPool());

        model.fromDraftPoolToRoundTrack();
        System.out.println("roundtrack dopo che ho spostato la prima draftpool\n" + model.getRoundTrack().toString());
        System.out.println("draft pool dopo che l'ho svuotata\n" + model.getDraftPool());

        Dice dice2 = new Dice(Colour.YELLOW);
        dice2.setValue(5);
        model.getDraftPool().add(dice2);
        System.out.println("draft pool con nuovo dado\n" + model.getDraftPool());

        LensCutterEvent event = new LensCutterEvent("1 6 2");
        controllerCLI.update(view, event);

    }

    @Test(expected = NullPointerException.class)
    public void testLensCutterFalse2() {
        model.setCurrentPlayer(model.getPlayers().get(0));
        model.getCurrentPlayer().setWindowPattern(new WindowPattern("wp1", 8, new Dice[4][5]));

        model.getDraftPool().add(new Dice(1));
        Dice dice1 = new Dice(6);
        model.getDraftPool().add(dice1);
        model.getDraftPool().add(new Dice(3));
        System.out.println("draft pool iniziale\n" + model.getDraftPool());

        model.fromDraftPoolToRoundTrack();
        System.out.println("roundtrack dopo che ho spostato la prima draftpool\n" + model.getRoundTrack().toString());
        System.out.println("draft pool dopo che l'ho svuotata\n" + model.getDraftPool());

        Dice dice2 = new Dice(Colour.YELLOW);
        dice2.setValue(5);
        model.getDraftPool().add(dice2);
        System.out.println("draft pool con nuovo dado\n" + model.getDraftPool());

        LensCutterEvent event = new LensCutterEvent("5 1 2");
        controllerCLI.update(view, event);

    }

    @Test(expected = NullPointerException.class)
    public void testLensCutterFalse3() {
        model.setCurrentPlayer(model.getPlayers().get(0));
        model.getCurrentPlayer().setWindowPattern(new WindowPattern("wp1", 8, new Dice[4][5]));

        model.getDraftPool().add(new Dice(1));
        Dice dice1 = new Dice(6);
        model.getDraftPool().add(dice1);
        model.getDraftPool().add(new Dice(3));
        System.out.println("draft pool iniziale\n" + model.getDraftPool());

        model.fromDraftPoolToRoundTrack();
        System.out.println("roundtrack dopo che ho spostato la prima draftpool\n" + model.getRoundTrack().toString());
        System.out.println("draft pool dopo che l'ho svuotata\n" + model.getDraftPool());

        Dice dice2 = new Dice(Colour.YELLOW);
        dice2.setValue(5);
        model.getDraftPool().add(dice2);
        System.out.println("draft pool con nuovo dado\n" + model.getDraftPool());

        LensCutterEvent event = new LensCutterEvent("1 1 8");
        controllerCLI.update(view, event);

    }


    /*@Test
    public void testFluxBrushEvent(){

    }*/



        @After
    public void tearDown(){
        model.getDraftPool().removeAll(model.getDraftPool());
        model = null;
        view = null;
        controllerCLI = null;
        setup = null;

    }

}
