package it.polimi.se2018.controller;

import it.polimi.se2018.events.mvevent.*;
import it.polimi.se2018.events.vcevent.*;
import it.polimi.se2018.model.*;
import it.polimi.se2018.view.VirtualView;
import java.util.*;

/**
 * This class represents the controller. It implements the Observer interface because the controller is an
 * observer of the view.
 * @author Daniele Mattioli
 */
public class Controller  implements Observer {
    private GameSingleton model;
    private VirtualView view;
    private List<ToolCard> toolCards;
    private Map<Integer, Runnable> eventsHandler = new HashMap<>();
    private VCEvent event;
    private long turnDuration;
    private boolean turnEnded = false;
    private TurnTimer turnTimer;
    private PlayerTurn playerTurn;
    private Game game;
    private Dice diceForFlux;
    private final Object lock = new Object();
    private final Object turnLock = new Object();
    private boolean singlePlayer;
    private int diceIndexSinglePlayer;

    private static final String EMPTY_POSITION = "Non c'è nessun dado nella posizione che hai inserito\n";
    private static final String RESTRICTION_VIOLATED = "Non stai rispettando le altre restrizioni di piazzamento\n";
    private static final String INSERTION_DENIED = "Non puoi inserire un dado in questa posizione\n";

    //spostato
    /**
     * Constructor of the class.
     * @param view          Virtual view
     * @param toolCards     List of tool cards chosen during the setup.
     * @param model         It's the only game running in the server
     * @param turnDuration  It's the time duration of a turn
     */
    public Controller(VirtualView view, List<ToolCard> toolCards, GameSingleton model, int turnDuration) {
        this.view = view;
        this.toolCards = toolCards;
        this.model = model;
        this.turnDuration = (long) turnDuration;
        singlePlayer = model.getPlayers().size() == 1;
        createMap();
    }

    //spostato
    /**
     * Associates a key to a method
     */
    private void createMap() {
        eventsHandler.put(1, this::grozingPliers);
        eventsHandler.put(2, this::eglomiseBrush);
        eventsHandler.put(3, this::copperFoilBurnisher);
        eventsHandler.put(4, this::lathekin);
        eventsHandler.put(5, this::lensCutter);
        eventsHandler.put(6, this::fluxBrushChooseDice);
        eventsHandler.put(7, this::glazingHammer);
        eventsHandler.put(8, this::runnerPliers);
        eventsHandler.put(9, this::corkBakedStraightedge);
        eventsHandler.put(10, this::grindingStone);
        eventsHandler.put(11, this::fluxRemoverChooseDice);
        eventsHandler.put(12, this::tapWheel);
        eventsHandler.put(13, this::fluxBrushPlaceDice);
        eventsHandler.put(14, this::fluxRemoverPlaceDice);
        eventsHandler.put(15, this::unsuspendPlayer);
        eventsHandler.put(16, this::saveDiceForSinglePlayer);
        eventsHandler.put(17, this::removePrivateObjectiveCard);
        eventsHandler.put(99, this::placeDiceFromDraftPoolToDicePattern);
        eventsHandler.put(100, this::skipTurn);
        eventsHandler.put(-1, this::setWindowPatternPlayer);
    }

    //spostato
    public Dice getDiceForFlux() {
        return diceForFlux;
    }

    //spostato
    public void setDiceForFlux(Dice diceForFlux) {
        this.diceForFlux = diceForFlux;
    }

    //spostato
    /**
     * Gets draft pool dice in diceIndex position
     *
     * @param diceIndex Dice index in the draft pool
     * @return Draft pool dice in diceIndex position
     * @throws IndexOutOfBoundsException if there is no dice in the selected position
     */
    private Dice getDiceFromDraftPool(int diceIndex) {
        if(diceIndex < 0 || diceIndex > model.getDraftPool().size()-1)
            throw new IndexOutOfBoundsException("There is no dice in the selected position");
        return model.getDraftPool().get(diceIndex);
    }


    //spostato
    /**
     * Gets a dice from round track
     * @param round Represents the number of the chosen round
     * @param index Represents the number of the dice in the chosen round
     * @return (index) dice in round (round) of the round track
     */
    private Dice getDiceFromRoundTrack(int round, int index) {
        return model.getRoundTrack().getDice(round, index);
    }

    //spostato
    /**
     * Gets a dice from the player's dice pattern
     * @param position Position of the dice
     * @return The dice chosen by the player in its dice pattern in position position
     */
    private Dice getDiceFromDicePattern(Position position) {
        return model.getCurrentPlayer().getDicePattern().getDice(position);
    }

    //spostato
    /**
     * Sets window pattern of the player
     */
    private void setWindowPatternPlayer() {
        WindowPatternChoiceEvent windowPatternChoiceEvent = (WindowPatternChoiceEvent) event;
        try{
            int choice = windowPatternChoiceEvent.getChoice();
            model.getCurrentPlayer().setWindowPattern(model.getCurrentPlayer().getWindowPatterns().get(choice), model.getPlayers().size() == 1);
            if (model.getPlayers().indexOf(model.getCurrentPlayer()) != model.getPlayers().size() - 1)
                model.setCurrentPlayer(model.getPlayers().get(model.getPlayers().indexOf(model.getCurrentPlayer()) + 1));
            else
                model.setCurrentPlayer(model.getPlayers().get(0));
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non hai inserito un numero corretto\n");
            view.showError(errorEvent);
        }
        canGameStart();
    }

    /**
     * This method checks if every player has chosen a windowPattern.
     * If it is, it starts the game.
     */
    private void canGameStart() {
        for (Player player: model.getPlayers()) {
            if (player.getWindowPattern() == null)
                return;
        }
        model.myNotify(new AllWindowPatternChosen((int)turnDuration));
        game = new Game();
        game.start();
    }

    /**
     * Handles player's favorTokensNumber (when he decides to use a tool card) and favor tokens on the tool card
     * @param toolCard Tool card chosen by the player
     */
    private void handleFavorTokensNumber(ToolCard toolCard) {
        if (toolCard.getFavorPoint() == 0) {
            model.getCurrentPlayer().reduceFavorTokens(1);
            toolCard.increaseFavorPoint(1);
        } else {
            model.getCurrentPlayer().reduceFavorTokens(2);
            toolCard.increaseFavorPoint(2);
        }
    }

    /**
     * Gets a ToolCard, given its id
     * @param id The Id of the toolCard
     * @return The ToolCard associated with the Id
     */
    private ToolCard searchToolCard(int id) {
        for (ToolCard toolcard : toolCards) {
            if (toolcard.getId() == id)
                return toolcard;
        }
        return toolCards.get(0);
    }


    /**
     * Reduces a Dice value
     * @param diceChosen The Dice of which it is going to reduce the value
     * @throws IllegalArgumentException If the Dice has 1 as value
     */
    private void grozingPliersSubOne(Dice diceChosen){
        try {
            diceChosen.subOne();
            handleToolCardUsed(1);
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non puoi decrementare un dado con valore 1\n");
            view.showError(errorEvent);
        }
    }


    /**
     * Increases a Dice value
     * @param diceChosen The Dice of which it is going to increase the value
     * @throws IllegalArgumentException If the Dice has 6 as value
     */
    private void grozingPliersAddOne(Dice diceChosen){
        try {
            diceChosen.addOne();
            handleToolCardUsed(1);
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non puoi incrementare un dado con valore 6\n");
            view.showError(errorEvent);
        }
    }

    /**
     * Tool card 1 method
     */
    private void grozingPliers() {
        if((!singlePlayer && model.getCurrentPlayer().getFavorTokensNumber() >= (searchToolCard(1).getFavorPoint() < 1 ? 1 : 2))
                || (singlePlayer && checkDiceForSinglePlayer(1))) {
            GrozingPliersEvent grozingPliersEvent = (GrozingPliersEvent) event;
            try{
                Dice diceChosen = getDiceFromDraftPool(grozingPliersEvent.getDiceIndexDraftPool());
                int choice = grozingPliersEvent.getChoice();
                //if player wants to decrease the value of the dice by one
                if (choice == 0) {
                    grozingPliersSubOne(diceChosen);
                }
                if (choice == 1) {
                    grozingPliersAddOne(diceChosen);
                }
            }catch (IndexOutOfBoundsException exception) {
                ErrorEvent errorEvent = new ErrorEvent(EMPTY_POSITION);
                view.showError(errorEvent);
            }
        }
        else {
            handleErrorEvent();
        }
    }

    /**
     * Tool card 2 method, called when some checkConditions has already been met
     * @param initialPosition The initialPosition of the Dice to move
     * @param finalPosition The final position of the Dice to move
     * @throws IllegalArgumentException If other checks are not met
     */
    private void eglomiseBrushValidRestriction(Position initialPosition, Position finalPosition){
        try {
            model.getCurrentPlayer().getDicePattern().moveDice(initialPosition, finalPosition);
            handleToolCardUsed(2);
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent(RESTRICTION_VIOLATED);
            view.showError(errorEvent);
        }
    }

    /**
     * Tool card 2 method
     */
    private void eglomiseBrush() {
        if((!singlePlayer && model.getCurrentPlayer().getFavorTokensNumber() >= (searchToolCard(2).getFavorPoint() < 1 ? 1 : 2))
                || (singlePlayer && checkDiceForSinglePlayer(2))) {
            EglomiseBrushEvent eglomiseBrushEvent = (EglomiseBrushEvent) event;
            Dice diceChosen = getDiceFromDicePattern(eglomiseBrushEvent.getInitialPosition());
            Position finalPosition = eglomiseBrushEvent.getFinalPosition();
            Position initialPosition = eglomiseBrushEvent.getInitialPosition();
            if (model.getCurrentPlayer().getWindowPattern().checkCellValueRestriction(finalPosition, diceChosen) &&
                model.getCurrentPlayer().getDicePattern().checkAdjacencyWithoutInitialPosition(finalPosition,initialPosition) &&
                model.getCurrentPlayer().getDicePattern().checkAdjacentColourWithoutInitialPosition(finalPosition,initialPosition, diceChosen) &&
                model.getCurrentPlayer().getDicePattern().checkAdjacentValueWithoutInitialPosition(finalPosition,initialPosition, diceChosen)) {
                    eglomiseBrushValidRestriction(initialPosition, finalPosition);
            } else {
                ErrorEvent errorEvent = new ErrorEvent(RESTRICTION_VIOLATED);
                view.showError(errorEvent);
            }
        }
        else {
            handleErrorEvent();
        }
    }

    /**
     * Tool card 3 method, called when some checkConditions has already been met
     * @param initialPosition The initialPosition of the Dice to move
     * @param finalPosition The final position of the Dice to move
     * @throws IllegalArgumentException If other checks are not met
     */
    private void copperFoilBurnisherValidRestriction(Position initialPosition, Position finalPosition){
        try {
            model.getCurrentPlayer().getDicePattern().moveDice(initialPosition, finalPosition);
            handleToolCardUsed(3);

        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent(RESTRICTION_VIOLATED);
            view.showError(errorEvent);
        }
    }

    /**
     * Tool card 3 method
     */
    private void copperFoilBurnisher() {
        if((!singlePlayer && model.getCurrentPlayer().getFavorTokensNumber() >= (searchToolCard(3).getFavorPoint() < 1 ? 1 : 2))
                || (singlePlayer && checkDiceForSinglePlayer(3))) {
            CopperFoilBurnisherEvent copperFoilBurnisherEvent = (CopperFoilBurnisherEvent) event;
            Dice diceChosen = getDiceFromDicePattern(copperFoilBurnisherEvent.getInitialPosition());
            Position finalPosition = copperFoilBurnisherEvent.getFinalPosition();
            Position initialPosition = copperFoilBurnisherEvent.getInitialPosition();
            if (model.getCurrentPlayer().getWindowPattern().checkCellColourRestriction(finalPosition, diceChosen) &&
                model.getCurrentPlayer().getDicePattern().checkAdjacencyWithoutInitialPosition(finalPosition,initialPosition) &&
                model.getCurrentPlayer().getDicePattern().checkAdjacentColourWithoutInitialPosition(finalPosition,initialPosition, diceChosen) &&
                model.getCurrentPlayer().getDicePattern().checkAdjacentValueWithoutInitialPosition(finalPosition,initialPosition, diceChosen)) {
                    copperFoilBurnisherValidRestriction(initialPosition, finalPosition);
            } else {
                ErrorEvent errorEvent = new ErrorEvent(RESTRICTION_VIOLATED);
                view.showError(errorEvent);
            }
        }
        else {
            handleErrorEvent();
        }
    }

    /**
     * Tool card 4 method, called when some checkConditions have already been met
     * @param initialPosition1 The initialPosition of the first Dice to move
     * @param initialPosition2 The initialPosition of the second Dice to move
     * @param finalPosition1 The final position of the first Dice to move
     * @param finalPosition2 The final position of the second Dice to move
     * @throws IllegalArgumentException If other checks are not met
     */
    private void lathekinValidRestriction(Position initialPosition1, Position finalPosition1, Position initialPosition2, Position finalPosition2){
    try {
        model.getCurrentPlayer().getDicePattern().moveDice(initialPosition1, finalPosition1);
        model.getCurrentPlayer().getDicePattern().moveDice(initialPosition2, finalPosition2);
        handleToolCardUsed(4);
    } catch (IllegalArgumentException exception) {
        ErrorEvent errorEvent = new ErrorEvent("Non stai rispettando le restrizioni di piazzamento\n");
        view.showError(errorEvent);
    }
}


    /**
     * Tool card 4 method
     */
    private void lathekin() {
        if((!singlePlayer && model.getCurrentPlayer().getFavorTokensNumber() >= (searchToolCard(4).getFavorPoint() < 1 ? 1 : 2))
                || (singlePlayer && checkDiceForSinglePlayer(4))) {
            LathekinEvent lathekinEvent = (LathekinEvent) event;
            Dice diceChosen1 = getDiceFromDicePattern(lathekinEvent.getInitialPosition1());
            Position initialPosition1 = lathekinEvent.getInitialPosition1();
            Position finalPosition1 = lathekinEvent.getFinalPosition1();
            Dice diceChosen2 = getDiceFromDicePattern(lathekinEvent.getInitialPosition2());
            Position initialPosition2 = lathekinEvent.getInitialPosition2();
            Position finalPosition2 = lathekinEvent.getFinalPosition2();

            if (model.getCurrentPlayer().getDicePattern().isDicePlaceable(finalPosition1, diceChosen1) &&
                model.getCurrentPlayer().getDicePattern().isDicePlaceable(finalPosition2, diceChosen2)) {
                    lathekinValidRestriction(initialPosition1, finalPosition1, initialPosition2, finalPosition2);
            } else {
                ErrorEvent errorEvent = new ErrorEvent(RESTRICTION_VIOLATED);
                view.showError(errorEvent);
            }
        }
        else{
            handleErrorEvent();

        }
    }



    /**
     * Swaps a dice in the draft pool with a dice in the round track.
     * @param round             Number of the chosen round
     * @param indexOfRoundTrack Index of the dice in the chosen round
     * @param indexOfDraftPool  Index of the dice in the draft pool
     * @throws IndexOutOfBoundsException If there are no dices in index chosen by the player
     */
    private void swapDice(int round, int indexOfRoundTrack, int indexOfDraftPool) {
        try {
            model.getRoundTrack().getList(round).add(model.getDraftPool().get(indexOfDraftPool));
            model.getDraftPool().remove(indexOfDraftPool);
            model.getDraftPool().add(model.getRoundTrack().getDice(round, indexOfRoundTrack));
            model.getRoundTrack().getList(round).remove(indexOfRoundTrack);
            DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath());
            model.myNotify(draftPoolEvent);
            RoundTrackEvent roundTrackEvent = new RoundTrackEvent(model.getRoundTrack().toString(), model.getRoundTrack().toStringPath());
            model.myNotify(roundTrackEvent);
            handleToolCardUsed(5);
        } catch (IndexOutOfBoundsException exception) {
            String nonValidInput = "Non valid input";
            throw new IndexOutOfBoundsException(nonValidInput);
        }
    }



    /**
     * Helper method for the tool card 5
     * @param roundIndex The round index
     * @param diceIndexInRoundTrack The index of the dice chosen
     * @param diceIndexInDraftPool The Index of the dice on the draftPool
     */
    private void lensCutterHelper(int roundIndex, int diceIndexInRoundTrack, int diceIndexInDraftPool){
        try {
            swapDice(roundIndex, diceIndexInRoundTrack,diceIndexInDraftPool);
        } catch (IndexOutOfBoundsException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non ci sono dadi nelle posizioni che hai indicato\n");
            view.showError(errorEvent);
        }
    }

    /**
     * Tool card 5 method
     */
    private void lensCutter() {
        if((!singlePlayer && model.getCurrentPlayer().getFavorTokensNumber() >= (searchToolCard(5).getFavorPoint() < 1 ? 1 : 2))
                || (singlePlayer && checkDiceForSinglePlayer(5))) {
            LensCutterEvent lensCutterEvent = (LensCutterEvent) event;
            int roundIndex = lensCutterEvent.getRoundIndex();
            int diceIndexInRoundTrack = lensCutterEvent.getDiceIndexInRoundTrack();
            int diceIndexInDraftPool = lensCutterEvent.getDiceIndexInDraftPool();
            lensCutterHelper(roundIndex, diceIndexInRoundTrack, diceIndexInDraftPool);
        }
        else{
            handleErrorEvent();
        }
    }

    //il metodo inizialmente era void e c'era l'attributo successfulMove che però era del metodo chiamante

    /**
     * Helper method for the fluxBrushPlaceDice method
     * @param finalPosition The finalPosition on which the dice will be placed
     * @param diceChosen The Dice chosen
     * @return True if is possible to use the ToolCard, false otherwise
     */
    private boolean fluxBrushPlaceDiceHelper(Position finalPosition, Dice diceChosen){
        try {
            model.getCurrentPlayer().getDicePattern().placeDice(finalPosition, diceChosen);
            model.getDraftPool().remove(diceChosen);
            DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath());
            model.myNotify(draftPoolEvent);
            handleToolCardUsed(6);
            return true;
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non puoi inserire il dado in questa posizione\n");
            view.showError(errorEvent);
            return false;
        }
    }

    /**
     * Helper method for the ToolCard 6
     */
    private void fluxBrushPlaceDice(){
        FluxBrushPlaceDiceEvent fluxBrushEvent = (FluxBrushPlaceDiceEvent) event;
        Position finalPosition = fluxBrushEvent.getFinalPosition();
        boolean successfulMove;
        while(true) {
            //inizialmente non c'era l'assegnamento a successfulMove, visto che il metodo helper restituiva void
            successfulMove = fluxBrushPlaceDiceHelper(finalPosition, diceForFlux);
            if(successfulMove)
                break;
            view.showError(new ErrorEvent("Non puoi inserire il dado in questa posizione\n"));
            view.fluxBrushChoice(new FluxBrushChoiceEvent(diceForFlux.toString()));

        }

    }

    /**
     * Tool card 6 method
     */
    //mi tengo io la posizione
    private void fluxBrushChooseDice() {
        if((!singlePlayer && model.getCurrentPlayer().getFavorTokensNumber() >= (searchToolCard(6).getFavorPoint() < 1 ? 1 : 2))
                || (singlePlayer && checkDiceForSinglePlayer(6))) {
            FluxBrushChooseDiceEvent fluxBrushEvent = (FluxBrushChooseDiceEvent) event;
            try{
                Dice diceChosen = getDiceFromDraftPool(fluxBrushEvent.getDiceIndexInDraftPool());
                diceChosen.roll();
                DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath());
                model.myNotify(draftPoolEvent);
                FluxBrushChoiceEvent fluxBrushChoiceEvent = new FluxBrushChoiceEvent(diceChosen.toString());
                boolean successfulMove = false;
                for(Position position : model.getCurrentPlayer().getDicePattern().getEmptyPositions()) {
                    if(model.getCurrentPlayer().getDicePattern().isDicePlaceable(position, diceChosen))
                        successfulMove = true;
                }
                if(!successfulMove){
                    ErrorEvent errorEvent = new ErrorEvent("Non potevi inserire il dado in nessuna posizione\n");
                    view.showError(errorEvent);
                }
                else {
                    this.diceForFlux = diceChosen;
                    view.fluxBrushChoice(fluxBrushChoiceEvent);
                }
            }catch (IndexOutOfBoundsException exception){
                ErrorEvent errorEvent = new ErrorEvent(EMPTY_POSITION);
                view.showError(errorEvent);
            }

        } else {
            handleErrorEvent();
        }
    }

    /**
     * Tool card 7 method
     */
    private void glazingHammer() {
        if((!singlePlayer && model.getCurrentPlayer().getFavorTokensNumber() >= (searchToolCard(7).getFavorPoint() < 1 ? 1 : 2))
                || (singlePlayer && checkDiceForSinglePlayer(7))) {
            if (model.getLap() == 1 && !model.getCurrentPlayer().isDiceMoved()) {
                model.rollEveryDice();
                handleToolCardUsed(7);
            } else {
                ErrorEvent errorEvent = new ErrorEvent("Non puoi usare questa carta durante il primo turno o se hai già usato la riserva\n");
                view.showError(errorEvent);
            }
        }
        else{
            handleErrorEvent();
        }
    }

    /**
     * Helper method for the ToolCard 8
     * @param finalPosition The position in which place the dice chosen
     * @param diceChosen The Dice chosen
     */
    private void runnerPliersHelper(Position finalPosition, Dice diceChosen){
        try {
            model.getCurrentPlayer().getDicePattern().placeDice(finalPosition, diceChosen);
            model.getDraftPool().remove(diceChosen);
            model.getCurrentPlayer().setLap(1);
            DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath());
            model.myNotify(draftPoolEvent);
            handleToolCardUsed(8);
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent(INSERTION_DENIED);
            view.showError(errorEvent);
        }
    }

    /**
     * Tool card 8 method
     */
    private void runnerPliers() {
        if((!singlePlayer && model.getCurrentPlayer().getFavorTokensNumber() >= (searchToolCard(8).getFavorPoint() < 1 ? 1 : 2))
                || (singlePlayer && checkDiceForSinglePlayer(8))) {
            RunnerPliersEvent runnerPliersEvent = (RunnerPliersEvent) event;
            try{
                Dice diceChosen= getDiceFromDraftPool(runnerPliersEvent.getDiceIndex());
                Position finalPosition = runnerPliersEvent.getPosition();
                runnerPliersHelper(finalPosition, diceChosen);
            }catch (IndexOutOfBoundsException exception) {
                ErrorEvent errorEvent = new ErrorEvent(EMPTY_POSITION);
                view.showError(errorEvent);
            }
        }
        else{
            handleErrorEvent();
        }
    }


    //DUBBIO

    /**
     * Tool card 9 method
     */
    private void corkBakedStraightedge() {
        if((!singlePlayer && model.getCurrentPlayer().getFavorTokensNumber() >= (searchToolCard(9).getFavorPoint() < 1 ? 1 : 2))
                || (singlePlayer && checkDiceForSinglePlayer(9))) {
            CorkBakedStraightedgeEvent corkBakedStraightedgeEvent = (CorkBakedStraightedgeEvent) event;
            try {
                Dice diceChosen = getDiceFromDraftPool(corkBakedStraightedgeEvent.getIndexInDraftPool());
                Position finalPosition = corkBakedStraightedgeEvent.getFinalPosition();
                if (model.getCurrentPlayer().getDicePattern().checkLimitationForCorkBackedStraightedge(finalPosition, diceChosen)) {
                    model.getCurrentPlayer().getDicePattern().setDice(finalPosition, diceChosen);
                    model.getDraftPool().remove(diceChosen);
                    DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath());
                    model.myNotify(draftPoolEvent);
                    handleToolCardUsed(9);
                    model.getCurrentPlayer().setLap(1);
                } else {
                    ErrorEvent errorEvent = new ErrorEvent(INSERTION_DENIED);
                    view.showError(errorEvent);
                }
            }catch (IndexOutOfBoundsException exception) {
                ErrorEvent errorEvent = new ErrorEvent(EMPTY_POSITION);
                view.showError(errorEvent);
            }

        } else {
            handleErrorEvent();
        }
    }


    /**
     * Tool card 10 method
     */
    private void grindingStone() {
        if((!singlePlayer && model.getCurrentPlayer().getFavorTokensNumber() >= (searchToolCard(10).getFavorPoint() < 1 ? 1 : 2))
                || (singlePlayer && checkDiceForSinglePlayer(10))) {
            GrindingStoneEvent grindingStoneEvent = (GrindingStoneEvent) event;
            if (grindingStoneEvent.getDicePosition() <= model.getDraftPool().size()) {
                try {
                    getDiceFromDraftPool(grindingStoneEvent.getDicePosition()).turn();
                    DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath());
                    model.myNotify(draftPoolEvent);
                    handleToolCardUsed(10);
                }catch (IndexOutOfBoundsException exception) {
                    ErrorEvent errorEvent = new ErrorEvent(EMPTY_POSITION);
                    view.showError(errorEvent);
                }

            } else {
                ErrorEvent errorEvent = new ErrorEvent("Non c'è nessun dado in questa posizione\n");
                view.showError(errorEvent);
            }
        }
        else{
            handleErrorEvent();
        }
    }

    /**
     * Helper method of the toolCard 11
     */
    private void fluxRemoverPlaceDice(){
        handleToolCardUsed(11);
        try{
            FluxRemoverPlaceDiceEvent fluxRemoverPlaceDiceEvent = (FluxRemoverPlaceDiceEvent) event;
            diceForFlux.setValue(fluxRemoverPlaceDiceEvent.getDiceValue());
            DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath());
            model.myNotify(draftPoolEvent);
            Position finalPosition = fluxRemoverPlaceDiceEvent.getDicePosition();
            model.getCurrentPlayer().getDicePattern().placeDice(finalPosition, diceForFlux);
            model.getDraftPool().remove(diceForFlux);
            draftPoolEvent = new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath());
            model.myNotify(draftPoolEvent);
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent(INSERTION_DENIED);
            view.showError(errorEvent);
            view.fluxRemoverChoice(new FluxRemoverChoiceEvent(diceForFlux.toString()));
        }
    }
    /**
     * Tool card 11 method
     */
    private void fluxRemoverChooseDice() {
        if((!singlePlayer && model.getCurrentPlayer().getFavorTokensNumber() >= (searchToolCard(11).getFavorPoint() < 1 ? 1 : 2))
                || (singlePlayer && checkDiceForSinglePlayer(11))) {
            model.getCurrentPlayer().setToolCardUsed(true);
            FluxRemoverChooseDiceEvent fluxRemoverEvent = (FluxRemoverChooseDiceEvent) event;
            try {
                Dice diceChosenFromDraftPool = getDiceFromDraftPool(fluxRemoverEvent.getDiceIndex());
                model.getDiceBag().add(diceChosenFromDraftPool);
                model.getDraftPool().remove(diceChosenFromDraftPool);
                model.getDraftPool().add(fluxRemoverEvent.getDiceIndex(),model.extractAndRollOneDice());
                Dice diceChosen = model.getDraftPool().get(fluxRemoverEvent.getDiceIndex());
                DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath());
                model.myNotify(draftPoolEvent);
                boolean successfulMove = false;
                for(Position position : model.getCurrentPlayer().getDicePattern().getEmptyPositions()) {
                    if (model.getCurrentPlayer().getWindowPattern().checkCellColourRestriction(position, diceChosen)&&
                            model.getCurrentPlayer().getDicePattern().checkDicePatternLimitations(position,diceChosen) &&
                            model.getCurrentPlayer().getDicePattern().checkAdjacentColour(position, diceChosen)) {
                        successfulMove = true;
                    }
                }
                if(!successfulMove){
                    ErrorEvent errorEvent = new ErrorEvent("Non potevi inserire il dado in nessuna posizione\n");
                    view.showError(errorEvent);
                }
                else {
                    this.diceForFlux = diceChosen;
                    FluxRemoverChoiceEvent fluxRemoverChoiceEvent = new FluxRemoverChoiceEvent(diceChosen.toString());
                    view.fluxRemoverChoice(fluxRemoverChoiceEvent);
                    ErrorEvent errorEvent = new ErrorEvent("OK toolCard 11");
                    view.showError(errorEvent);
                }

            } catch (IndexOutOfBoundsException exception) {
                ErrorEvent errorEvent = new ErrorEvent(EMPTY_POSITION);
                view.showError(errorEvent);
            }
        } else {
            handleErrorEvent();
        }
    }

    /**
     * Method used when the user wants to move only one dice using the ToolCard12
     * @param initialPosition1 The initial position of the dice
     * @param finalPosition1 The final position of the dice
     */
    private void tapWheelOneDiceMoved(Position initialPosition1, Position finalPosition1){
        try {
            model.getCurrentPlayer().getDicePattern().moveDice(initialPosition1, finalPosition1);
            handleToolCardUsed(12);
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent(INSERTION_DENIED);
            view.showError(errorEvent);
        }
    }

    /**
     * Method used when the user wants to move two dices using the ToolCard12
     * @param initialPosition1 The initial position of the first dice
     * @param finalPosition1 The final position of the first dice
     * @param initialPosition2 The initial position of the second dice
     * @param finalPosition2 The final position of the second dice
     */
    private void tapWheelTwoDiceMoved(Position initialPosition1, Position finalPosition1, Position initialPosition2, Position finalPosition2){
        try {
            model.getCurrentPlayer().getDicePattern().moveDice(initialPosition1, finalPosition1);
            model.getCurrentPlayer().getDicePattern().moveDice(initialPosition2, finalPosition2);
            handleToolCardUsed(12);
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent(INSERTION_DENIED);
            view.showError(errorEvent);
        }
    }

    /**
     * Tool card 12 method
     */
    private void tapWheel() {
        if((!singlePlayer && model.getCurrentPlayer().getFavorTokensNumber() >= (searchToolCard(12).getFavorPoint() < 1 ? 1 : 2))
                || (singlePlayer && checkDiceForSinglePlayer(12))) {
            TapWheelEvent tapWheelEvent = (TapWheelEvent) event;
            int round = tapWheelEvent.getRoundIndex();
            int indexOfRoundTrack = tapWheelEvent.getDiceIndex();
            Dice diceChosenFromRoundTrack = getDiceFromRoundTrack(round, indexOfRoundTrack);
            Position initialPosition1 = tapWheelEvent.getFirstDicePosition();
            Position finalPosition1 = tapWheelEvent.getNewFirstDicePosition();
            Position initialPosition2 = tapWheelEvent.getSecondDicePosition();
            Position finalPosition2 = tapWheelEvent.getNewSecondDicePosition();
            Dice tmp1 = model.getCurrentPlayer().getDicePattern().removeDice(initialPosition1);
            //if the player wants to move only one dice
            if (tmp1.getColour().equals(diceChosenFromRoundTrack.getColour()) &&
                    model.getCurrentPlayer().getDicePattern().isDicePlaceable(finalPosition1, tmp1)){
                if (initialPosition2 == null && finalPosition2 == null) {
                            model.getCurrentPlayer().getDicePattern().setDice(initialPosition1,tmp1);
                            tapWheelOneDiceMoved(initialPosition1,finalPosition1);
                }
                //if the player wants to move two dices
                else if (initialPosition2 != null && finalPosition2 != null){
                    Dice tmp2 = model.getCurrentPlayer().getDicePattern().removeDice(initialPosition2);
                    if(tmp2.getColour().equals(diceChosenFromRoundTrack.getColour()) &&
                        model.getCurrentPlayer().getDicePattern().isDicePlaceable(finalPosition2, tmp2)) {
                        model.getCurrentPlayer().getDicePattern().setDice(initialPosition1,tmp1);
                        model.getCurrentPlayer().getDicePattern().setDice(initialPosition2,tmp2);
                        tapWheelTwoDiceMoved(initialPosition1, finalPosition1, initialPosition2, finalPosition2);
                    }

                }
                else {
                    ErrorEvent errorEvent = new ErrorEvent("Non stai rispettando le restrizioni di piazzamento\n");
                    view.showError(errorEvent);
                }

            }else{
                ErrorEvent errorEvent = new ErrorEvent("Non stai rispettando le restrizioni di piazzamento\n");
                view.showError(errorEvent);
            }

        }
        else{
            handleErrorEvent();
        }
    }

    /**
     * Makes a suspended player to rejoin the game
     */
    private void unsuspendPlayer() {
        UnsuspendEvent unsuspendEvent = (UnsuspendEvent) event;
        for (Player player: model.getPlayers()) {
            if (unsuspendEvent.getName().equals(player.getName())) {
                player.setSuspended(false);
                break;
            }
        }
    }

    /**
     * Calls the method associated to a specific event
     * @param event Event
     */
    private void performAction(VCEvent event) {
        eventsHandler.get(event.getId()).run();
    }


    /**
     * Places a dice from the draft pool to the round track
     */
    private void placeDiceFromDraftPoolToDicePattern() {
        PlaceDiceEvent placeDiceEvent = (PlaceDiceEvent) event;
        try{
            Dice diceChosen = getDiceFromDraftPool(placeDiceEvent.getDiceIndexDraftPool());
            Position finalPosition = placeDiceEvent.getPositionToPlace();
            model.getCurrentPlayer().getDicePattern().placeDice(finalPosition, diceChosen);
            model.getDraftPool().remove(diceChosen);
            model.getCurrentPlayer().setDiceMoved(true);
            DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath());
            model.myNotify(draftPoolEvent);
        }catch (IndexOutOfBoundsException e) {
            view.showError(new ErrorEvent(EMPTY_POSITION));
        }catch (IllegalArgumentException e) {
            view.showError(new ErrorEvent(INSERTION_DENIED));
        }
    }


    /**
     * Skips current player's turn
     */
    private void skipTurn() {
        turnEnded = true;
        turnTimer.interrupt(); //è finito il turno del giocatore
        playerTurn.interrupt(); //non voglio più input
        view.showEndTurn(new EndTurnEvent());
        synchronized (turnLock) {
            turnLock.notifyAll(); //risveglia il thread principale
        }
    }

    /**
     * Compute all scores of the players
     */
    private void computeAllScores() {
        for (Player player : model.getPlayers()) {
            player.computeMyScore(model.getPublicObjectiveCards());
        }

    }

    private void removePrivateObjectiveCard() {
        PrivateObjectiveCardChosen privateObjectiveCardChosen = (PrivateObjectiveCardChosen) event;
        model.getCurrentPlayer().getPrivateObjectiveCards().remove(
                (privateObjectiveCardChosen.getIndexOfChosenCard() == 0 ? 1 : 0));
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg == null)
            endGame();
        else {
            event = (VCEvent) arg;
            performAction(event);
            if (event.getId() != 6 && event.getId() != 11 && event.getId() != 16) {
                synchronized (lock) {
                    lock.notifyAll();
                }
            }
        }
    }

    private boolean checkDiceForSinglePlayer(int id){
        return (model.getDraftPool().get(diceIndexSinglePlayer).getColour().equals(searchToolCard(id).getColour()));
    }


    private void saveDiceForSinglePlayer(){
        DiceChosenSinglePlayer diceChosenSinglePlayer = (DiceChosenSinglePlayer) event;
        diceIndexSinglePlayer = diceChosenSinglePlayer.getDicePosition();
    }

    private void handleDiceForSinglePlayer(int id){
        model.getDraftPool().remove(diceIndexSinglePlayer);
        DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath());
        model.myNotify(draftPoolEvent);
        toolCards.remove(searchToolCard(id));
    }

    private void handleToolCardUsed(int id){
        model.getCurrentPlayer().setToolCardUsed(true);
        if(!singlePlayer)
            handleFavorTokensNumber(searchToolCard(id));
        else
            handleDiceForSinglePlayer(id);
    }

    private void handleErrorEvent(){
        ErrorEvent errorEvent;
        if(singlePlayer)
            errorEvent = new ErrorEvent("Il dado che hai selezionato per pagare la carta utensile non era corretto\n");
        else
            errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
        view.showError(errorEvent);

    }

    /**
     * This class represents the timer of the turn.
     * If the timer ends, the player is suspended.
     */
    class TurnTimer extends Thread {

        @Override
        public void run() {
            try {
                sleep(turnDuration);
                model.getCurrentPlayer().setSuspended(true);
                view.playerSuspended();
                turnEnded = true;
                playerTurn.interrupt();
                synchronized (turnLock) {
                    turnLock.notifyAll();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * This class represents the turn of the player.
     * It shows the action menu and asks for an input.
     */
    class PlayerTurn extends Thread {

        @Override
        public void run() {
            while (!turnEnded) {
                view.showActionMenu(new ActionMenuEvent(model.getCurrentPlayer().isDiceMoved(), model.getCurrentPlayer().isToolCardUsed(),
                        model.toolCardsToStringAbbreviated()));
                view.getInput();
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }
    }



    /**
     * This class represents the game.
     * It handles the whole game during the 10 rounds and then shows the scores obtained by each player.
     */
    class Game extends Thread {

        /**
         * Creates an new ShowAllEvent
         * @return A new ShowAllEvent
         */
        private ShowAllEvent createShowAllEvent(){
            return new ShowAllEvent(
                    new DicePatternEvent(model.dicePatternsToString(), model.playersToString(), model.dicePatternsToStringPath(), model.getCurrentPlayer().getName()),
                    model.publicObjectiveCardsToString(), model.publicObjectiveCardsToStringPath(),
                    new ToolCardEvent(model.toolCardsToString(), model.toolCardsToStringPath(), model.getFavorTokensOnToolCards() ),
                    new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath()),
                    new RoundTrackEvent(model.getRoundTrack().toString(), model.getRoundTrack().toStringPath()),
                    model.getCurrentPlayer().getPrivateObjectiveCardsToString(), model.getCurrentPlayer().getPrivateObjectiveCardsToStringPath(),
                    new SetWindowPatternsGUIEvent(model.windowPatternsToStringPath(),model.getFavorTokensNumberPlayers()));
        }

        @Override
        public void run() {
            model.extractAndRoll();
            for(Player player : model.getPlayers()){
                view.setCurrentPlayer(player);
                model.setCurrentPlayer(player);
                view.showAll(createShowAllEvent());
            }
            model.setCurrentPlayer(model.getPlayers().get(0));
            view.setCurrentPlayer(model.getPlayers().get(0));
            while (model.getRound() < 11) {
                turnTimer = new TurnTimer();
                playerTurn = new PlayerTurn();
                view.setCurrentPlayer(model.getCurrentPlayer());
                view.showAll(createShowAllEvent());
                turnEnded = false;
                turnTimer.start(); //thread for time
                playerTurn.start(); //thread to ask input

                synchronized (turnLock) {
                    try {
                        turnLock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                nextPlayer();
            }
            model.myNotify(new GameEnded());

            while (singlePlayer && model.getCurrentPlayer().getPrivateObjectiveCards().size() > 1) {
                model.myNotify(new AskPrivateObjectiveCard(model.getCurrentPlayer().getPrivateObjectiveCardsToString(),
                        model.getCurrentPlayer().getPrivateObjectiveCardsToStringPath()));
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            showScores();
        }

        /**
         * Creates an event containing info about the final scores of the players
         */
        private void showScores() {
            computeAllScores();

            if (model.getPlayers().size() == 1) {
                model.getCurrentPlayer().setScore(model.getCurrentPlayer().getScore() -     //the score is decreased again
                        (2 * model.getCurrentPlayer().getDicePattern().emptySpaces()));     //because we're in singlePlayer
                Player sagrada = new Player("sagrada");
                sagrada.setScore(model.getRoundTrack().sumDiceValue());
                if (sagrada.getScore() < model.getCurrentPlayer().getScore())
                    model.getPlayers().add(sagrada);
                else
                    model.getPlayers().add(0, sagrada);
            } else {

                model.getPlayers().sort(Comparator.comparingInt(Player::getScore));
                Collections.reverse(model.getPlayers());

                Player winner = model.selectWinner();
                model.getPlayers().remove(winner);
                model.getPlayers().add(0, winner);
            }

            List<Integer> scores = new ArrayList<>();
            for(Player player: model.getPlayers())
                scores.add(player.getScore());

            model.createScoreTrack(scores);
        }

        /**
         * Changes the current player and, if lap is the second, calls nextRound()
         */
        private void nextPlayer() {
            if (model.getRound() == 11)
                return;
            model.getCurrentPlayer().setDiceMoved(false);
            model.getCurrentPlayer().setToolCardUsed(false);
            if (model.getLap() == 0) {
                if (!model.getCurrentPlayer().equals(model.getPlayers().get(model.getPlayersNumber() - 1))) {  //if player isn't the last element of the array list
                    model.setCurrentPlayer(model.getPlayers().get(model.getPlayers().indexOf(model.getCurrentPlayer()) + 1)); //currentPlayer is the one following player
                } else {
                    model.setLap(1); //beginning of the second turn of the round
                }

            } else if (model.getLap() == 1) {
                if (!model.getCurrentPlayer().equals(model.getPlayers().get(0))) {  //if player isn't the first element of the array list
                    model.setCurrentPlayer(model.getPlayers().get(model.getPlayers().indexOf(model.getCurrentPlayer()) - 1)); //currentPlayer is the previous of player
                } else {
                    model.setLap(0); //ends of second turn
                    nextRound();
                }
            }
            if (model.getCurrentPlayer().isSuspended() || model.getCurrentPlayer().isConnectionLost())
                nextPlayer();
        }

        /**
         * Increase round, changes players position, puts remaining dices from the draft pool to the round track,
         * extracts new dices from diceBag and show
         */
        private void nextRound() {
            model.increaseRound();
            if (model.getRound() < 11) {
                Player toRemove = model.getPlayers().remove(0);
                model.getPlayers().add(toRemove); //remove the first player, shift by one the other elements of players and then add the first player at the end of the array list
                model.setCurrentPlayer(model.getPlayers().get(0));
                model.fromDraftPoolToRoundTrack();
                model.extractAndRoll();
                model.myNotify(new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath()));
            }
        }
    }

    /**
     * This method is called only if there is only one player left in the game.
     * It interrupts every other Thread in the Controller and tells the last player it has won
     */
    private void endGame() {
        turnEnded = true;
        if (game.isAlive())
            game.interrupt();
        if (playerTurn.isAlive())
            playerTurn.interrupt();
        if (turnTimer.isAlive())
            turnTimer.interrupt();

        if (model.getPlayers().size() == 1)     //avviene solo se in singleplayer il giocatore abbandona la partita
            model.myNotify(new ScoreTrackEvent(null, null));
        else {
            model.myNotify(new GameEnded());

            computeAllScores();
            model.getPlayers().sort(Comparator.comparingInt(Player::getScore));
            Collections.reverse(model.getPlayers());

            model.lastPlayer();
        }
    }

}


