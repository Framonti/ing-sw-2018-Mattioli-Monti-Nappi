package it.polimi.se2018.controller;

import it.polimi.se2018.events.mvevent.*;
import it.polimi.se2018.events.vcevent.*;
import it.polimi.se2018.model.*;
import it.polimi.se2018.view.VirtualViewCLI;

//TODO: GESTIRE LA PARTITA IN GRANDE (IL VECCHIO METODO GAME)

/**
 * This class represents the controller. It implements the Observer interface because the controller is an
 * observer of the view.
 * @author Daniele Mattioli
 */
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//da controllare: eccezioni, update()

public class ControllerCLI implements Observer  {
    private GameSingleton model;
    private VirtualViewCLI view;
    private List<ToolCard> toolCards;
    private boolean isGameSetupEnded = false; //used for update method
    private Map<Integer, Runnable> eventsHandler = new HashMap<>();
    private VCEvent event;
    private String nonValidInput = "Non valid input";
    private int turnDuration = 10000;
    private boolean turnEnded = false;
    private TurnTimer turnTimer = new TurnTimer();

    /**
     * Constructor of the class.
     * @param view Virtual view
     * @param toolCards List of tool cards chosen during the setup.
     */
    //TODO DA METTERE METODO choosePlayerOrder di GameSetup
    //TODO AGGIUNGERE WINNER EVENT
    //TODO: rivedere inizializzazione di model nel costruttore
    public ControllerCLI(VirtualViewCLI view, List<ToolCard> toolCards, GameSingleton model, int turnDuration) {
        this.view = view;
        this.toolCards = toolCards;
        this.model = model;
        this.turnDuration = turnDuration;
        createMap();
    }


    //TODO: rivedere commento javadoc
    /**
     * Associates a key to a method
     */
    private void createMap() {
        eventsHandler.put(1, this::grozingPliers);
        eventsHandler.put(2, this::eglomiseBrush);
        eventsHandler.put(3, this::copperFoilBurnisher);
        eventsHandler.put(4, this::lathekin);
        eventsHandler.put(5, this::lensCutter);
        eventsHandler.put(6, this::fluxBrush);
        eventsHandler.put(7, this::glazingHammer);
        eventsHandler.put(8, this::runnerPliers);
        eventsHandler.put(9, this::corkBakedStraightedge);
        eventsHandler.put(10, this::grindingStone);
        eventsHandler.put(11, this::fluxRemover);
        eventsHandler.put(12, this::tapWheel);
        eventsHandler.put(99, this::placeDiceFromDraftPoolToDicePattern);
        eventsHandler.put(100, this::skipTurn);
        eventsHandler.put(-1, this::setWindowPatternPlayer);
    }

    //TODO rivedere TUTTO!!!
    public void game() {

        model.extractAndRoll();

        while (model.getRound() < 10){
            view.setCurrentPlayer(model.getCurrentPlayer());
            view.showAll(new ShowAllEvent(model.dicePatternsToString(), model.playersToString(),model.publicObjectiveCardsToString(),
                        model.toolCardsToString(),model.draftPoolToString(),model.getRoundTrack().toString(),
                        model.getCurrentPlayer().getPrivateObjectiveCard().toString()));
            Thread turnT = new Thread(turnTimer);
            turnT.start();
            while(!turnEnded) {
                view.showActionMenu(new ActionMenuEvent(model.getCurrentPlayer().isDiceMoved(), model.getCurrentPlayer().isToolCardUsed(),
                                                        model.toolCardsToString()));
                view.getInput();
            }
            turnEnded = false;
            turnT.interrupt();
            nextPlayer();
       }
        computeAllScores();
        ScoreTrackEvent showScoreTrackEvent = new ScoreTrackEvent(model.getScoreTrack().toString());
        model.mySetChanged();
        model.notifyObservers(showScoreTrackEvent);
        WinnerEvent winnerEvent = new WinnerEvent(model.selectWinner().getName());
        model.mySetChanged();
        model.notifyObservers(winnerEvent);
    }

    /**
     * Gets the private objective card the player has asked for
     * @return The private objective card the player has asked for
     */
    private PrivateObjectiveCard getPrivateObjectiveCard () {
            return model.getCurrentPlayer().getPrivateObjectiveCard();
    }


    /**
     * Gets draft pool dice in diceIndex position
     * @param diceIndex Dice index in the draft pool
     * @return Draft pool dice in diceIndex position
     */
    private Dice getDiceFromDraftPool (int diceIndex){
        return model.getDraftPool().get(diceIndex);
    }


    /**
     * Gets a dice from round track
     * @param round Represents the number of the chosen round
     * @param index Represents the number of the dice in the chosen round
     * @return (index) dice in round (round) of the round track
     */
    private Dice getDiceFromRoundTrack ( int round, int index){
        return model.getRoundTrack().getDice(round, index);
    }

    /**
     * Gets a dice from the player's dice pattern
     * @param position Position of the dice
     * @return The dice chosen by the player in its dice pattern in position position
     */
    private Dice getDiceFromDicePattern (Position position){
        return model.getCurrentPlayer().getDicePattern().getDice(position);
    }

    /**
     * Sets window pattern of the player
     */
    private void setWindowPatternPlayer () {
        WindowPatternChoiceEvent windowPatternChoiceEvent = (WindowPatternChoiceEvent) event;
        int choice = windowPatternChoiceEvent.getChoice();
        if (choice >=0 && choice < 4)
            model.getCurrentPlayer().setWindowPattern(model.getCurrentPlayer().getWindowPatterns().get(choice));
        else {
            ErrorEvent errorEvent = new ErrorEvent("Non hai inserito un numero corretto\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }




    /**
     * Handles player's favorTokensNumber (when he decides to use a tool card) and favor tokens on the cool card
     * @param toolCard Tool card chosen by the player
     */
    private void handleFavorTokensNumber (ToolCard toolCard){
        if (toolCard.getFavorPoint() == 0) {
            try {
                toolCard.increaseFavorPoint(1);
                model.getCurrentPlayer().reduceFavorTokens(1);
                FavorTokensEvent favorTokensEvent = new FavorTokensEvent(String.valueOf(model.getCurrentPlayer().getFavorTokensNumber()), String.valueOf(model.getCurrentPlayer()));
                model.mySetChanged();
                model.notifyObservers(favorTokensEvent);
                ToolCardEvent toolCardEvent = new ToolCardEvent(model.toolCardsToString());
                model.mySetChanged();
                model.notifyObservers(toolCardEvent);
            } catch (UnsupportedOperationException exception) {
                ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
                view.showError(errorEvent);
                view.getInput();
            }
        } else {
            try {
                toolCard.increaseFavorPoint(2);
                model.getCurrentPlayer().reduceFavorTokens(2);
                FavorTokensEvent favorTokensEvent = new FavorTokensEvent(String.valueOf(model.getCurrentPlayer().getFavorTokensNumber()), String.valueOf(model.getCurrentPlayer()));
                model.mySetChanged();
                model.notifyObservers(favorTokensEvent);
                ToolCardEvent toolCardEvent = new ToolCardEvent(model.toolCardsToString());
                model.mySetChanged();
                model.notifyObservers(toolCardEvent);
            } catch (UnsupportedOperationException exception) {
                ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
                view.showError(errorEvent);
                view.getInput();
            }
        }
    }

    /**
     * Tool card 1 method
     */
    private void grozingPliers () {
        GrozingPliersEvent grozingPliersEvent = (GrozingPliersEvent) event;
        Dice diceChosen = getDiceFromDraftPool(grozingPliersEvent.getDiceIndexDraftPool());
        int choice = grozingPliersEvent.getChoice();
        //if player wants to decrease the value of the dice by one
        if (choice == 0) {
            try {
                diceChosen.subOne();
                DraftPoolEvent draftPoolEvent = new DraftPoolEvent( model.draftPoolToString());
                model.mySetChanged();
                model.notifyObservers(draftPoolEvent);
            } catch (IllegalArgumentException exception) {
                ErrorEvent errorEvent = new ErrorEvent("Non puoi decrementare un dado con valore 1\n");
                view.showError(errorEvent);
                view.getInput();
            }

        }
        if (choice == 1) {
            try {
                diceChosen.addOne();
                DraftPoolEvent draftPoolEvent = new DraftPoolEvent( model.draftPoolToString());
                model.mySetChanged();
                model.notifyObservers(draftPoolEvent);
            } catch (IllegalArgumentException exception) {
                ErrorEvent errorEvent = new ErrorEvent("Non puoi incrementare un dado con valore 6\n");
                view.showError(errorEvent);
                view.getInput();
            }
        }
    }

    /**
     * Tool card 2 method
     */
    private void eglomiseBrush () {
        EglomiseBrushEvent eglomiseBrushEvent = (EglomiseBrushEvent) event;
        Dice diceChosen = getDiceFromDicePattern(eglomiseBrushEvent.getInitialPosition());
        Position finalPosition = eglomiseBrushEvent.getFinalPosition();
        Position initialPosition = eglomiseBrushEvent.getInitialPosition();
        if (model.getCurrentPlayer().getWindowPattern().checkCellValueRestriction(finalPosition, diceChosen) &&
                model.getCurrentPlayer().getDicePattern().checkAdjacency(finalPosition) &&
                model.getCurrentPlayer().getDicePattern().checkAdjacentColour(finalPosition, diceChosen) &&
                model.getCurrentPlayer().getDicePattern().checkAdjacentValue(finalPosition, diceChosen)) {
            try {
                model.getCurrentPlayer().getDicePattern().moveDice(initialPosition, finalPosition);
                DicePatternEvent dicePatternEvent = new DicePatternEvent(model.getCurrentPlayer().getDicePattern().dicePatternToString(), model.playersToString());
                model.mySetChanged();
                model.notifyObservers(dicePatternEvent);
            } catch (IllegalArgumentException exception) {
                ErrorEvent errorEvent = new ErrorEvent("Non stai rispettando le altre restrizioni di piazzamento\n");
                view.showError(errorEvent);
                view.getInput();
            }
        }
        else{
            ErrorEvent errorEvent = new ErrorEvent("Non stai rispettando le altre restrizioni di piazzamento\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }

    /**
     * Tool card 3 method
     */
    private void copperFoilBurnisher () {
        CopperFoilBurnisherEvent copperFoilBurnisherEvent = (CopperFoilBurnisherEvent) event;
        Dice diceChosen = getDiceFromDicePattern(copperFoilBurnisherEvent.getInitialPosition());
        Position finalPosition = copperFoilBurnisherEvent.getFinalPosition();
        if (model.getCurrentPlayer().getWindowPattern().checkCellColourRestriction(finalPosition, diceChosen) &&
                model.getCurrentPlayer().getDicePattern().checkAdjacency(finalPosition) &&
                model.getCurrentPlayer().getDicePattern().checkAdjacentColour(finalPosition, diceChosen) &&
                model.getCurrentPlayer().getDicePattern().checkAdjacentValue(finalPosition, diceChosen)) {
            try {
                model.getCurrentPlayer().getDicePattern().moveDice(copperFoilBurnisherEvent.getInitialPosition(), finalPosition);
                DicePatternEvent dicePatternEvent = new DicePatternEvent(model.getCurrentPlayer().getDicePattern().dicePatternToString(), model.playersToString());
                model.mySetChanged();
                model.notifyObservers(dicePatternEvent);
            } catch (IllegalArgumentException exception) {
                ErrorEvent errorEvent = new ErrorEvent("Non stai rispettando le altre restrizioni di piazzamento\n");
                view.showError(errorEvent);
                view.getInput();
            }
        }
        else{
            ErrorEvent errorEvent = new ErrorEvent("Non stai rispettando le altre restrizioni di piazzamento\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }


    /**
     * Tool card 4 method
     */
    private void lathekin () {
        LathekinEvent lathekinEvent = (LathekinEvent) event;
        Dice diceChosen1 = getDiceFromDicePattern(lathekinEvent.getInitialPosition1());
        Position initialPosition1 = lathekinEvent.getInitialPosition1();
        Position finalPosition1 = lathekinEvent.getFinalPosition1();
        Dice diceChosen2 = getDiceFromDicePattern(lathekinEvent.getInitialPosition2());
        Position initialPosition2 = lathekinEvent.getInitialPosition2();
        Position finalPosition2 = lathekinEvent.getFinalPosition2();

        if (model.getCurrentPlayer().getDicePattern().checkDicePatternLimitations(finalPosition1, diceChosen1) &&
                model.getCurrentPlayer().getWindowPattern().checkCell(finalPosition1, diceChosen1) &&
                model.getCurrentPlayer().getDicePattern().checkDicePatternLimitations(finalPosition2, diceChosen2) &&
                model.getCurrentPlayer().getWindowPattern().checkCell(finalPosition2, diceChosen2)) {
            try {
                model.getCurrentPlayer().getDicePattern().moveDice(initialPosition1, finalPosition1);
                model.getCurrentPlayer().getDicePattern().moveDice(initialPosition2, finalPosition2);
                DicePatternEvent dicePatternEvent = new DicePatternEvent(model.getCurrentPlayer().getDicePattern().dicePatternToString(), model.playersToString());
                model.mySetChanged();
                model.notifyObservers(dicePatternEvent);
            } catch (IllegalArgumentException exception) {
                ErrorEvent errorEvent = new ErrorEvent("Non stai rispettando le restrizioni di piazzamento\n");
                view.showError(errorEvent);
                view.getInput();
            }
        }
        else{
            ErrorEvent errorEvent = new ErrorEvent("Non stai rispettando le altre restrizioni di piazzamento\n");
            view.showError(errorEvent);
            view.getInput();
        }

    }




    //TODO: CONTROLLARE SE VA BENE CHE QUESTA RILANCI L'ECCEZIONE E SIA POI LENS CUTTER A RICHIEDERE L'INPUT
    /**
     * Swaps a dice in the draft pool with a dice in the round track
     * @param round Number of the chosen round
     * @param indexOfRoundTrack Index of the dice in the chosen round
     * @param indexOfDraftPool Index of the dice in the draft pool
     * @throws IndexOutOfBoundsException If there are no dices in index chosen by the player
     */
    private void swapDice ( int round, int indexOfRoundTrack, int indexOfDraftPool){
        try {
            model.getRoundTrack().getList(round).add(model.getDraftPool().get(indexOfDraftPool));
            model.getDraftPool().remove(indexOfDraftPool);
            model.getDraftPool().add(model.getRoundTrack().getDice(round, indexOfRoundTrack));
            model.getRoundTrack().getList(round).remove(indexOfRoundTrack);
            DraftPoolEvent draftPoolEvent = new DraftPoolEvent( model.draftPoolToString());
            model.mySetChanged();
            model.notifyObservers(draftPoolEvent);
            RoundTrackEvent roundTrackEvent = new RoundTrackEvent(model.getRoundTrack().toString());
            model.mySetChanged();
            model.notifyObservers(roundTrackEvent);
        } catch (IndexOutOfBoundsException exception){
            throw new IndexOutOfBoundsException(nonValidInput);
        }
    }



    /**
     * Tool card 5 method
     */
    private void lensCutter () {
        LensCutterEvent lensCutterEvent = (LensCutterEvent) event;
        try {
            swapDice(lensCutterEvent.getRoundIndex(), lensCutterEvent.getDiceIndexInRoundTrack(), lensCutterEvent.getDiceIndexInDraftPool());
        } catch (IndexOutOfBoundsException exception){
            ErrorEvent errorEvent = new ErrorEvent("Non ci sono dadi nelle posizioni che hai indicato\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }






    /**
     * Tool card 6 method
     */
    private void fluxBrush () {
        FluxBrushEvent fluxBrushEvent = (FluxBrushEvent) event;
        Dice diceChosen = getDiceFromDraftPool(fluxBrushEvent.getDiceIndexInDraftPool());
        diceChosen.roll();
        int i;
        boolean successfulMove = false;
        for (i = 0; i < model.getCurrentPlayer().getDicePattern().emptySpaces() && !successfulMove; i++) {
            try {
                model.getCurrentPlayer().getDicePattern().placeDice(fluxBrushEvent.getFinalPosition(), diceChosen);
                successfulMove = true;
                model.getDraftPool().remove(diceChosen);
                DraftPoolEvent draftPoolEvent = new DraftPoolEvent( model.draftPoolToString());
                model.mySetChanged();
                model.notifyObservers(draftPoolEvent);
                DicePatternEvent dicePatternEvent = new DicePatternEvent(model.getCurrentPlayer().getDicePattern().dicePatternToString(), model.playersToString());
                model.mySetChanged();
                model.notifyObservers(dicePatternEvent);
            } catch (IllegalArgumentException exception) {
                ErrorEvent errorEvent = new ErrorEvent("Non puoi inserire il dado in questa posizione\n");
                view.showError(errorEvent);
                view.getInput();
                successfulMove = false;
            }
        }
        if (!successfulMove) {
            ErrorEvent errorEvent = new ErrorEvent("Non potevi inserire il dado in nessuna posizione\n");
            view.showError(errorEvent);
        }
    }


    /**
     * Tool card 7 method
     */
    private void glazingHammer () {
        int i;
        if (model.getCurrentPlayer().getLap() == 1) {
            for (i = 0; i < model.getDraftPool().size(); i++)
                model.getDraftPool().get(i).roll();
            DraftPoolEvent draftPoolEvent = new DraftPoolEvent( model.draftPoolToString());
            model.mySetChanged();
            model.notifyObservers(draftPoolEvent);
        } else {
            ErrorEvent errorEvent = new ErrorEvent("Non puoi usare questa carta durante il primo turno\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }


    /**
     * Tool card 8 method
     */
    private void runnerPliers () {
        RunnerPliersEvent runnerPliersEvent = (RunnerPliersEvent) event;
        Dice diceChosen = getDiceFromDraftPool(runnerPliersEvent.getDiceIndex());
        Position finalPosition = runnerPliersEvent.getPosition();
        try {
            model.getCurrentPlayer().getDicePattern().placeDice(finalPosition, diceChosen);
            model.getDraftPool().remove(diceChosen);
            model.getCurrentPlayer().setLap(1);
            DraftPoolEvent draftPoolEvent = new DraftPoolEvent( model.draftPoolToString());
            model.mySetChanged();
            model.notifyObservers(draftPoolEvent);
            DicePatternEvent dicePatternEvent = new DicePatternEvent(model.getCurrentPlayer().getDicePattern().dicePatternToString(), model.playersToString());
            model.mySetChanged();
            model.notifyObservers(dicePatternEvent);
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non puoi inserire un dado in questa posizione\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }


    //DUBBIO
    /**
     * Tool card 9 methot
     */
    private void corkBakedStraightedge () {
        CorkBakedStraightedgeEvent corkBakedStraightedgeEvent = (CorkBakedStraightedgeEvent) event;
        Dice diceChosen = getDiceFromDraftPool(corkBakedStraightedgeEvent.getIndexInDraftPool());
        Position finalPosition = corkBakedStraightedgeEvent.getFinalPosition();
        if (model.getCurrentPlayer().getWindowPattern().checkCell(finalPosition, diceChosen)) {
            model.getCurrentPlayer().getDicePattern().setDice(finalPosition, diceChosen);
            model.getDraftPool().remove(diceChosen);
            DraftPoolEvent draftPoolEvent = new DraftPoolEvent( model.draftPoolToString());
            model.mySetChanged();
            model.notifyObservers(draftPoolEvent);
            DicePatternEvent dicePatternEvent = new DicePatternEvent(model.getCurrentPlayer().getDicePattern().dicePatternToString(), model.playersToString());
            model.mySetChanged();
            model.notifyObservers(dicePatternEvent);
        } else {
            ErrorEvent errorEvent = new ErrorEvent("Non puoi inserire un dado in questa posizione\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }



    /**
     * Tool card 10 method
     */
    private void grindingStone () {
        GrindingStoneEvent grindingStoneEvent = (GrindingStoneEvent) event;
        if(grindingStoneEvent.getDicePosition() <= model.getDraftPool().size()) {
            getDiceFromDraftPool(grindingStoneEvent.getDicePosition()).turn();
            DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString());
            model.mySetChanged();
            model.notifyObservers(draftPoolEvent);
        }
        else{
            ErrorEvent errorEvent = new ErrorEvent("Non c'è nessun dado in questa posizione\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }

    //DUBBIO
    /**
     * Tool card 11 method
     */
    private void fluxRemover () {
        FluxRemoverEvent fluxRemoverEvent = (FluxRemoverEvent) event;
        Dice diceChosenFromDraftPool = getDiceFromDraftPool(fluxRemoverEvent.getDiceIndex());
        model.getDiceBag().add(diceChosenFromDraftPool);
        model.getDraftPool().remove(diceChosenFromDraftPool);
        model.extractAndRoll();
        Dice diceChosenFromDiceBag = model.getDraftPool().get(model.getDraftPool().size() - 1);
        switch (diceChosenFromDiceBag.getColour()) {
            case YELLOW:
                diceChosenFromDiceBag.setValue(fluxRemoverEvent.getYellowDiceValue());
                break;

            case GREEN:
                diceChosenFromDiceBag.setValue(fluxRemoverEvent.getGreenDiceValue());
                break;

            case RED:
                diceChosenFromDiceBag.setValue(fluxRemoverEvent.getRedDiceValue());
                break;

            case BLUE:
                diceChosenFromDiceBag.setValue(fluxRemoverEvent.getBlueDiceValue());
                break;

            case PURPLE:
                diceChosenFromDiceBag.setValue(fluxRemoverEvent.getPurpleDiceValue());
                break;

            default:
                break;

        }

        Position finalPosition = fluxRemoverEvent.getDicePosition();
        try {
            model.getCurrentPlayer().getDicePattern().placeDice(finalPosition, diceChosenFromDiceBag);
            model.getDraftPool().remove(diceChosenFromDraftPool);
            DraftPoolEvent draftPoolEvent = new DraftPoolEvent( model.draftPoolToString());
            model.mySetChanged();
            model.notifyObservers(draftPoolEvent);
            DicePatternEvent dicePatternEvent = new DicePatternEvent(model.getCurrentPlayer().getDicePattern().dicePatternToString(), model.playersToString());
            model.mySetChanged();
            model.notifyObservers(dicePatternEvent);
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non puoi inserire un dado in questa posizione\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }

    //TODO CONTROLLARE SE BISOGNA LANCIARE ECCEZIONI AL LIVELLO PIù ALTO

    /**
     * Tool card 12 method
     */
    private void tapWheel () {
        TapWheelEvent tapWheelEvent = (TapWheelEvent) event;
        int round = tapWheelEvent.getRoundIndex();
        int indexOfRoundTrack = tapWheelEvent.getDiceIndex();
        Dice diceChosenFromRoundTrack = getDiceFromRoundTrack(round, indexOfRoundTrack);
        Position initialPosition1 = tapWheelEvent.getFirstDicePosition();
        Position finalPosition1 = tapWheelEvent.getNewFirstDicePosition();
        Position initialPosition2 = tapWheelEvent.getSecondDicePosition();
        Position finalPosition2 = tapWheelEvent.getNewSecondDicePosition();

        //if the player wants to move only one dice
        if (initialPosition2 == null && finalPosition2 == null &&
                model.getCurrentPlayer().getDicePattern().getDice(initialPosition1).getColour().equals(diceChosenFromRoundTrack.getColour()) &&
                model.getCurrentPlayer().getDicePattern().checkDicePatternLimitations(finalPosition1, diceChosenFromRoundTrack) &&
                model.getCurrentPlayer().getWindowPattern().checkCell(finalPosition1, diceChosenFromRoundTrack)) {
            try {
                model.getCurrentPlayer().getDicePattern().moveDice(initialPosition1, finalPosition1);
                DicePatternEvent dicePatternEvent = new DicePatternEvent(model.getCurrentPlayer().getDicePattern().dicePatternToString(), model.playersToString());
                model.mySetChanged();
                model.notifyObservers(dicePatternEvent);
            } catch (IllegalArgumentException exception) {
                ErrorEvent errorEvent = new ErrorEvent("Non puoi inserire un dado in questa posizione\n");
                view.showError(errorEvent);
                view.getInput();
            }
        }

        //if the player wants to move two dices
        else if (initialPosition2 != null && finalPosition2 != null &&
                model.getCurrentPlayer().getDicePattern().getDice(initialPosition1).getColour().equals(diceChosenFromRoundTrack.getColour()) &&
                model.getCurrentPlayer().getDicePattern().checkDicePatternLimitations(finalPosition1, diceChosenFromRoundTrack) &&
                model.getCurrentPlayer().getWindowPattern().checkCell(finalPosition1, diceChosenFromRoundTrack)) {
            try {
                model.getCurrentPlayer().getDicePattern().moveDice(initialPosition1, finalPosition1);
                model.getCurrentPlayer().getDicePattern().moveDice(initialPosition2, finalPosition2);
                DicePatternEvent dicePatternEvent = new DicePatternEvent(model.getCurrentPlayer().getDicePattern().dicePatternToString(), model.playersToString());
                model.mySetChanged();
                model.notifyObservers(dicePatternEvent);
            } catch (IllegalArgumentException exception) {
                ErrorEvent errorEvent = new ErrorEvent("Non puoi inserire un dado in questa posizione\n");
                view.showError(errorEvent);
                view.getInput();
            }
        }
    }

    /**
     * Calls the method associated to a specific event
     * @param event Event
     */
    private void performAction (VCEvent event){
        eventsHandler.get(event.getId()).run();
    }


    /**
     * Places a dice from the draft pool to the round track
     */
    private void placeDiceFromDraftPoolToDicePattern (){
        PlaceDiceEvent placeDiceEvent = (PlaceDiceEvent) event;
        Dice diceChosen = getDiceFromDraftPool(placeDiceEvent.getDiceIndexDraftPool());
        Position finalPosition = placeDiceEvent.getPositionToPlace();
        model.getCurrentPlayer().getDicePattern().placeDice(finalPosition, diceChosen);
        model.getDraftPool().remove(diceChosen);
        model.getCurrentPlayer().reverseDiceMoved();
        DraftPoolEvent draftPoolEvent = new DraftPoolEvent( model.draftPoolToString());
        model.mySetChanged();
        model.notifyObservers(draftPoolEvent);
        DicePatternEvent dicePatternEvent = new DicePatternEvent(model.getCurrentPlayer().getDicePattern().dicePatternToString(), model.playersToString());
        model.mySetChanged();
        model.notifyObservers(dicePatternEvent);
    }

    /**
     * Changes the current player and if lap is the second, calls nextRound()
     */
    private void nextPlayer () {
        if (model.getLap() == 0) {
            for (Player player : model.getPlayers()) {
                if (!player.equals(model.getPlayers().get(model.getPlayersNumber() - 1))) {  //if player isn't the last element of the array list
                    model.setCurrentPlayer(model.getPlayers().get(model.getPlayers().indexOf(player) + 1)); //currentPlayer is the one following player
                } else {
                    model.setCurrentPlayer(player); //currentPlayer is still player
                    model.setLap(1); //begins second turn of the round
                }

            }

        } else if (model.getLap() == 1) {
            for (Player player : model.getPlayers()) {
                if (!player.equals(model.getPlayers().get(0))) {  //if player isn't the first element of the array list
                    model.setCurrentPlayer(model.getPlayers().get(model.getPlayers().indexOf(player) - 1)); //currentPlayer is the previous of player
                } else {
                    model.setLap(0); //end of second turn
                    nextRound();
                }

            }
        }
    }



    /**
     * Increase round, changes players position, puts remaining dices from the draft pool to the round track,
     * extracts new dices from diceBag and show
     */
    private void nextRound () {
        model.increaseRound();
        model.getPlayers().add(model.getPlayersNumber() - 1, model.getPlayers().remove(0)); //remove the first player, shift by one the other elements of players and then add the first player at the end of the array list
        model.setCurrentPlayer(model.getPlayers().get(0));
        model.fromDraftPoolToRoundTrack();
        model.extractAndRoll();
        ShowAllEvent showAllEvent = new ShowAllEvent(model.dicePatternsToString(), model.playersToString(), model.publicObjectiveCardsToString(), model.toolCardsToString(), model.draftPoolToString(), model.getRoundTrack().toString(), model.getCurrentPlayer().getPrivateObjectiveCard().toString());
        model.mySetChanged();
        model.notifyObservers(showAllEvent);
    }



    /**
     * Compute all scores of the players
     */
    private void computeAllScores () {
        for (Player player : model.getPlayers()) {
            player.computeMyScore(model.getPublicObjectiveCards());
        }

    }

    /**
     * Skips current player's turn
     */
    private void skipTurn () {
        nextPlayer();
    }


    @Override
    public void update (Observable o, Object arg){
        event = (VCEvent)arg;
        performAction(event);
    }

    class TurnTimer implements Runnable {

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            long end = start + turnDuration;
            //Just waits
            while (System.currentTimeMillis() < end) { }
            turnEnded = true;
        }
    }

}
