package it.polimi.se2018.testController;

import it.polimi.se2018.WindowPatternCardDeckBuilder;
import it.polimi.se2018.controller.ControllerCLI;
import it.polimi.se2018.events.mvevent.FavorTokensEvent;
import it.polimi.se2018.events.mvevent.MVEvent;
import it.polimi.se2018.events.vcevent.EglomiseBrushEvent;
import it.polimi.se2018.events.vcevent.GrozingPliersEvent;
import it.polimi.se2018.events.vcevent.VCEvent;
import it.polimi.se2018.events.vcevent.WindowPatternChoiceEvent;
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
        controllerCLI = new ControllerCLI(view,toolCards,model);
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
            model.getCurrentPlayer().addWindowPattern(card);


        WindowPatternChoiceEvent event = new WindowPatternChoiceEvent("1");
        controllerCLI.update(view, event);
        assertEquals(windowPatterns.get(0), model.getCurrentPlayer().getWindowPattern() );

        event = new WindowPatternChoiceEvent("2");
        controllerCLI.update(view, event);
        assertEquals(windowPatterns.get(1), model.getCurrentPlayer().getWindowPattern() );

        event = new WindowPatternChoiceEvent("3");
        controllerCLI.update(view, event);
        assertEquals(windowPatterns.get(2), model.getCurrentPlayer().getWindowPattern() );

        event = new WindowPatternChoiceEvent("4");
        controllerCLI.update(view, event);
        assertEquals(windowPatterns.get(3), model.getCurrentPlayer().getWindowPattern() );

    }

    //TODO
   /* @Test
    public void testHandleFavorTokensNumber(){
        model.setCurrentPlayer(model.getPlayers().get(0));

    }*/

   @Test(expected = NullPointerException.class)
   public void testGrozinPliersFalse(){
       model.getDraftPool().add(new Dice(1));
       model.getDraftPool().add(new Dice(6));
       model.getDraftPool().add(new Dice(3));


       GrozingPliersEvent event = new GrozingPliersEvent("1      1 ");
       controllerCLI.update(view,event);
       event = new GrozingPliersEvent("2      2 ");
       controllerCLI.update(view,event);

   }

    @Test
    public void testGrozingPliersTrue(){
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


    }


    @Test
    public void testEglomiseBrush(){
        model.setCurrentPlayer(model.getPlayers().get(0));
        //model.getCurrentPlayer().setWindowPattern(new WindowPattern("", 4, new Dice[4][5]));

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


        Dice diceGreen = new Dice(Colour.GREEN);
        diceGreen.setValue(4);
        model.getCurrentPlayer().getDicePattern().setDice(new Position (3,2),diceGreen);

        Dice diceToMove = new Dice(Colour.BLUE);
        diceToMove.setValue(3);
        model.getCurrentPlayer().getDicePattern().setDice(new Position (3,3), diceToMove );


        EglomiseBrushEvent event = new EglomiseBrushEvent("4 4 3 3 ");

        controllerCLI.update(view,event);


        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position (3,3)), null);
        assertEquals(model.getCurrentPlayer().getDicePattern().getDice(new Position(2,2)), diceToMove);





    }

    @After
    public void tearDown(){
        model.getDraftPool().removeAll(model.getDraftPool());
        model = null;
        view = null;
        controllerCLI = null;
        setup = null;

    }

}
