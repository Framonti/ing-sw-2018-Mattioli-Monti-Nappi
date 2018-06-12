package it.polimi.se2018.controller;

import it.polimi.se2018.events.mvevent.*;
import it.polimi.se2018.events.vcevent.*;
import it.polimi.se2018.model.*;
import it.polimi.se2018.view.VirtualViewCLI;


/**
 * This class represents the controller. It implements the Observer interface because the controller is an
 * observer of the view.
 * @author Daniele Mattioli
 */
import java.util.*;


public class ControllerCLI implements Observer {
    private GameSingleton model;
    private VirtualViewCLI view;
    private List<ToolCard> toolCards;
    private boolean isGameSetupEnded = false; //used for update method
    private Map<Integer, Runnable> eventsHandler = new HashMap<>();
    private VCEvent event;
    private String nonValidInput = "Non valid input";
    private long turnDuration;
    private boolean turnEnded = false;
    private TurnTimer turnTimer;
    private PlayerTurn playerTurn;
    private Game game;
    private Dice diceForFlux;
    private final Object lock = new Object();
    private final Object turnLock = new Object();

    /**
     * Constructor of the class.
     *
     * @param view      Virtual view
     * @param toolCards List of tool cards chosen during the setup.
     */

    public ControllerCLI(VirtualViewCLI view, List<ToolCard> toolCards, GameSingleton model, int turnDuration) {
        this.view = view;
        this.toolCards = toolCards;
        this.model = model;
        this.turnDuration = (long) turnDuration;
        createMap();
    }



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
        eventsHandler.put(99, this::placeDiceFromDraftPoolToDicePattern);
        eventsHandler.put(100, this::skipTurn);
        eventsHandler.put(-1, this::setWindowPatternPlayer);
    }


    public Dice getDiceForFlux() {
        return diceForFlux;
    }

    public void setDiceForFlux(Dice diceForFlux) {
        this.diceForFlux = diceForFlux;
    }

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


    /**
     * Gets a dice from round track
     *
     * @param round Represents the number of the chosen round
     * @param index Represents the number of the dice in the chosen round
     * @return (index) dice in round (round) of the round track
     */
    private Dice getDiceFromRoundTrack(int round, int index) {
        return model.getRoundTrack().getDice(round, index);
    }

    /**
     * Gets a dice from the player's dice pattern
     *
     * @param position Position of the dice
     * @return The dice chosen by the player in its dice pattern in position position
     */
    private Dice getDiceFromDicePattern(Position position) {
        return model.getCurrentPlayer().getDicePattern().getDice(position);
    }


    /**
     * Sets window pattern of the player
     */
    private void setWindowPatternPlayer() {
        WindowPatternChoiceEvent windowPatternChoiceEvent = (WindowPatternChoiceEvent) event;
        try{
            int choice = windowPatternChoiceEvent.getChoice();
            model.getCurrentPlayer().setWindowPattern(model.getCurrentPlayer().getWindowPatterns().get(choice));
            if (model.getPlayers().indexOf(model.getCurrentPlayer()) != model.getPlayers().size() - 1)
                model.setCurrentPlayer(model.getPlayers().get(model.getPlayers().indexOf(model.getCurrentPlayer()) + 1));
            else
                model.setCurrentPlayer(model.getPlayers().get(0));
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non hai inserito un numero corretto\n");
            view.showError(errorEvent);
        }
    }


    /**
     * Handles player's favorTokensNumber (when he decides to use a tool card) and favor tokens on the tool card
     *
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

    private ToolCard searchToolCard(int id) {
        for (ToolCard toolcard : toolCards) {
            if (toolcard.getId() == id)
                return toolcard;
        }
        return toolCards.get(0);
    }


    private void grozingPliersSubOne(Dice diceChosen){
        try {
            diceChosen.subOne();
            model.getCurrentPlayer().setToolCardUsed(true);
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non puoi decrementare un dado con valore 1\n");
            view.showError(errorEvent);
        }
    }


    private void grozingPliersAddOne(Dice diceChosen){
        try {
            diceChosen.addOne();
            model.getCurrentPlayer().setToolCardUsed(true);
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non puoi incrementare un dado con valore 6\n");
            view.showError(errorEvent);
        }
    }

    /**
     * Tool card 1 method
     */
    private void grozingPliers() {
        if(model.getCurrentPlayer().getFavorTokensNumber() > searchToolCard(1).getFavorPoint()) {
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
                handleFavorTokensNumber(searchToolCard(1));
            }catch (IndexOutOfBoundsException exception) {
                ErrorEvent errorEvent = new ErrorEvent("Non c'è nessun dado nella posizione che hai inserito\n");
                view.showError(errorEvent);
            }
        }
        else {
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
        }
    }



    private void eglomiseBrushValidRestriction( Position initialPosition, Position finalPosition ){
        try {
            model.getCurrentPlayer().getDicePattern().moveDice(initialPosition, finalPosition);
            model.getCurrentPlayer().setToolCardUsed(true);
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non stai rispettando le altre restrizioni di piazzamento\n");
            view.showError(errorEvent);
        }
    }

    /**
     * Tool card 2 method
     */
    private void eglomiseBrush() {
        if(model.getCurrentPlayer().getFavorTokensNumber() > searchToolCard(2).getFavorPoint()) {
            EglomiseBrushEvent eglomiseBrushEvent = (EglomiseBrushEvent) event;
            Dice diceChosen = getDiceFromDicePattern(eglomiseBrushEvent.getInitialPosition());
            Position finalPosition = eglomiseBrushEvent.getFinalPosition();
            Position initialPosition = eglomiseBrushEvent.getInitialPosition();
            if (model.getCurrentPlayer().getWindowPattern().checkCellValueRestriction(finalPosition, diceChosen) &&
                model.getCurrentPlayer().getDicePattern().checkAdjacency(finalPosition) &&
                model.getCurrentPlayer().getDicePattern().checkAdjacentColour(finalPosition, diceChosen) &&
                model.getCurrentPlayer().getDicePattern().checkAdjacentValue(finalPosition, diceChosen)) {
                    eglomiseBrushValidRestriction(initialPosition, finalPosition);
                    handleFavorTokensNumber(searchToolCard(2));
            } else {
                ErrorEvent errorEvent = new ErrorEvent("Non stai rispettando le altre restrizioni di piazzamento\n");
                view.showError(errorEvent);
            }
        }
        else {
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
        }
    }


    private void copperFoilBurnisherValidRestriction(Position initialPosition, Position finalPosition){
        try {
            model.getCurrentPlayer().getDicePattern().moveDice(initialPosition, finalPosition);
            model.getCurrentPlayer().setToolCardUsed(true);

        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non stai rispettando le altre restrizioni di piazzamento\n");
            view.showError(errorEvent);
        }
    }

    /**
     * Tool card 3 method
     */
    private void copperFoilBurnisher() {
        if(model.getCurrentPlayer().getFavorTokensNumber() > searchToolCard(3).getFavorPoint()) {
            CopperFoilBurnisherEvent copperFoilBurnisherEvent = (CopperFoilBurnisherEvent) event;
            Dice diceChosen = getDiceFromDicePattern(copperFoilBurnisherEvent.getInitialPosition());
            Position finalPosition = copperFoilBurnisherEvent.getFinalPosition();
            Position initialPosition = copperFoilBurnisherEvent.getInitialPosition();
            if (model.getCurrentPlayer().getWindowPattern().checkCellColourRestriction(finalPosition, diceChosen) &&
                model.getCurrentPlayer().getDicePattern().checkAdjacency(finalPosition) &&
                model.getCurrentPlayer().getDicePattern().checkAdjacentColour(finalPosition, diceChosen) &&
                model.getCurrentPlayer().getDicePattern().checkAdjacentValue(finalPosition, diceChosen)) {
                    copperFoilBurnisherValidRestriction(initialPosition, finalPosition);
                    handleFavorTokensNumber(searchToolCard(3));
            } else {
                ErrorEvent errorEvent = new ErrorEvent("Non stai rispettando le altre restrizioni di piazzamento\n");
                view.showError(errorEvent);
            }
        }
        else {
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
        }
    }

private void lathekinValidRestriction(Position initialPosition1, Position finalPosition1, Position initialPosition2, Position finalPosition2){
    try {
        model.getCurrentPlayer().getDicePattern().moveDice(initialPosition1, finalPosition1);
        model.getCurrentPlayer().getDicePattern().moveDice(initialPosition2, finalPosition2);
        model.getCurrentPlayer().setToolCardUsed(true);
    } catch (IllegalArgumentException exception) {
        ErrorEvent errorEvent = new ErrorEvent("Non stai rispettando le restrizioni di piazzamento\n");
        view.showError(errorEvent);
    }
}


    /**
     * Tool card 4 method
     */
    private void lathekin() {
        if(model.getCurrentPlayer().getFavorTokensNumber() > searchToolCard(4).getFavorPoint()) {
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
                    handleFavorTokensNumber(searchToolCard(4));
            } else {
                ErrorEvent errorEvent = new ErrorEvent("Non stai rispettando le altre restrizioni di piazzamento\n");
                view.showError(errorEvent);
            }
        }
        else{
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);

        }
    }



    /**
     * Swaps a dice in the draft pool with a dice in the round track.
     *
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
            model.getCurrentPlayer().setToolCardUsed(true);
            model.myNotify(draftPoolEvent);
            RoundTrackEvent roundTrackEvent = new RoundTrackEvent(model.getRoundTrack().toString(), model.getRoundTrack().toStringPath());
            model.myNotify(roundTrackEvent);
        } catch (IndexOutOfBoundsException exception) {
            throw new IndexOutOfBoundsException(nonValidInput);
        }
    }


    //TODO: rivedere il nome di questo metodo
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
        if(model.getCurrentPlayer().getFavorTokensNumber() > searchToolCard(5).getFavorPoint()) {
            LensCutterEvent lensCutterEvent = (LensCutterEvent) event;
            int roundIndex = lensCutterEvent.getRoundIndex();
            int diceIndexInRoundTrack = lensCutterEvent.getDiceIndexInRoundTrack();
            int diceIndexInDraftPool = lensCutterEvent.getDiceIndexInDraftPool();
            lensCutterHelper(roundIndex, diceIndexInRoundTrack, diceIndexInDraftPool);
            handleFavorTokensNumber(searchToolCard(5));
        }
        else{
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
        }
    }

    //TODO: rivedere il nome del metodo
    //il metodo inizialmente era void e c'era l'attributo successfulMove che però era del metodo chiamante
    private boolean fluxBrushPlaceDiceHelper(Position finalPosition, Dice diceChosen){
        try {
            model.getCurrentPlayer().getDicePattern().placeDice(finalPosition, diceChosen);
            model.getDraftPool().remove(diceChosen);
            DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath());
            model.myNotify(draftPoolEvent);
            return true;
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non puoi inserire il dado in questa posizione\n");
            view.showError(errorEvent);
            return false;
        }
    }

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
        if (model.getCurrentPlayer().getFavorTokensNumber() > searchToolCard(6).getFavorPoint()) {
            model.getCurrentPlayer().setToolCardUsed(true);
            handleFavorTokensNumber(searchToolCard(6));
            FluxBrushChooseDiceEvent fluxBrushEvent = (FluxBrushChooseDiceEvent) event;
            try{
                Dice diceChosen = getDiceFromDraftPool(fluxBrushEvent.getDiceIndexInDraftPool());
                diceChosen.roll();
                FluxBrushChoiceEvent fluxBrushChoiceEvent = new FluxBrushChoiceEvent(diceChosen.toString());
                boolean succesfulMove = false;
                for(Position position : model.getCurrentPlayer().getDicePattern().getEmptyPositions()) {
                    if(model.getCurrentPlayer().getDicePattern().isDicePlaceable(position, diceChosen))
                        succesfulMove = true;
                }
                if(!succesfulMove){
                    ErrorEvent errorEvent = new ErrorEvent("Non potevi inserire il dado in nessuna posizione\n");
                    view.showError(errorEvent);
                }
                else {
                    this.diceForFlux = diceChosen;
                    view.fluxBrushChoice(fluxBrushChoiceEvent);
                }
            }catch (IndexOutOfBoundsException exception){
                ErrorEvent errorEvent = new ErrorEvent("Non c'è nessun dado nella posizione che hai inserito\n");
                view.showError(errorEvent);
            }

        } else {
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
        }
    }

    /**
     * Tool card 7 method
     */
    private void glazingHammer() {
        if(model.getCurrentPlayer().getFavorTokensNumber() > searchToolCard(7).getFavorPoint()) {
            if (model.getLap() == 1) {
                model.rollEveryDice();
                model.getCurrentPlayer().setToolCardUsed(true);
                handleFavorTokensNumber(searchToolCard(7));
            } else {
                ErrorEvent errorEvent = new ErrorEvent("Non puoi usare questa carta durante il primo turno\n");
                view.showError(errorEvent);
            }
        }
        else{
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
        }
    }


    private void runnerPliersHelper(Position finalPosition, Dice diceChosen){
        try {
            model.getCurrentPlayer().getDicePattern().placeDice(finalPosition, diceChosen);
            model.getDraftPool().remove(diceChosen);
            model.getCurrentPlayer().setLap(1);
            DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath());
            model.getCurrentPlayer().setToolCardUsed(true);
            model.myNotify(draftPoolEvent);
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non puoi inserire un dado in questa posizione\n");
            view.showError(errorEvent);
        }
    }

    /**
     * Tool card 8 method
     */
    private void runnerPliers() {
        if(model.getCurrentPlayer().getFavorTokensNumber() > searchToolCard(8).getFavorPoint()) {
            RunnerPliersEvent runnerPliersEvent = (RunnerPliersEvent) event;
            try{
                Dice diceChosen= getDiceFromDraftPool(runnerPliersEvent.getDiceIndex());
                Position finalPosition = runnerPliersEvent.getPosition();
                runnerPliersHelper(finalPosition, diceChosen);
                handleFavorTokensNumber(searchToolCard(8));
            }catch (IndexOutOfBoundsException exception) {
                ErrorEvent errorEvent = new ErrorEvent("Non c'è nessun dado nella posizione che hai inserito\n");
                view.showError(errorEvent);
            }
        }
        else{
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
        }
    }


    //DUBBIO

    /**
     * Tool card 9 method
     */
    private void corkBakedStraightedge() {
        if(model.getCurrentPlayer().getFavorTokensNumber() > searchToolCard(9).getFavorPoint()) {
            CorkBakedStraightedgeEvent corkBakedStraightedgeEvent = (CorkBakedStraightedgeEvent) event;
            try {
                Dice diceChosen = getDiceFromDraftPool(corkBakedStraightedgeEvent.getIndexInDraftPool());
                Position finalPosition = corkBakedStraightedgeEvent.getFinalPosition();
                if (model.getCurrentPlayer().getWindowPattern().checkCell(finalPosition, diceChosen)) {
                    model.getCurrentPlayer().getDicePattern().setDice(finalPosition, diceChosen);
                    model.getDraftPool().remove(diceChosen);
                    DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath());
                    model.getCurrentPlayer().setToolCardUsed(true);
                    model.myNotify(draftPoolEvent);
                    handleFavorTokensNumber(searchToolCard(9));
                } else {
                    ErrorEvent errorEvent = new ErrorEvent("Non puoi inserire un dado in questa posizione\n");
                    view.showError(errorEvent);
                }
            }catch (IndexOutOfBoundsException exception) {
                ErrorEvent errorEvent = new ErrorEvent("Non c'è nessun dado nella posizione che hai inserito\n");
                view.showError(errorEvent);
            }

        } else {
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
        }
    }


    /**
     * Tool card 10 method
     */
    private void grindingStone() {
        if(model.getCurrentPlayer().getFavorTokensNumber() > searchToolCard(10).getFavorPoint()) {
            GrindingStoneEvent grindingStoneEvent = (GrindingStoneEvent) event;
            if (grindingStoneEvent.getDicePosition() <= model.getDraftPool().size()) {
                try {
                    getDiceFromDraftPool(grindingStoneEvent.getDicePosition()).turn();
                    DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath());
                    model.getCurrentPlayer().setToolCardUsed(true);
                    model.myNotify(draftPoolEvent);
                    handleFavorTokensNumber(searchToolCard(10));
                }catch (IndexOutOfBoundsException exception) {
                    ErrorEvent errorEvent = new ErrorEvent("Non c'è nessun dado nella posizione che hai inserito\n");
                    view.showError(errorEvent);
                }

            } else {
                ErrorEvent errorEvent = new ErrorEvent("Non c'è nessun dado in questa posizione\n");
                view.showError(errorEvent);
            }
        }
        else{
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
        }
    }



    private void fluxRemoverPlaceDice(){
        try{
            FluxRemoverPlaceDiceEvent fluxRemoverPlaceDiceEvent = (FluxRemoverPlaceDiceEvent) event;
            diceForFlux.setValue(fluxRemoverPlaceDiceEvent.getDiceValue());
            Position finalPosition = fluxRemoverPlaceDiceEvent.getDicePosition();
            model.getCurrentPlayer().getDicePattern().placeDice(finalPosition, diceForFlux);
            model.getDraftPool().remove(diceForFlux);
            DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath());
            model.getCurrentPlayer().setToolCardUsed(true);
            model.myNotify(draftPoolEvent);
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non puoi inserire un dado in questa posizione\n");
            view.showError(errorEvent);
            view.fluxRemoverChoice(new FluxRemoverChoiceEvent(diceForFlux.toString()));
        }
    }
    /**
     * Tool card 11 method
     */
    private void fluxRemoverChooseDice() {
        if (model.getCurrentPlayer().getFavorTokensNumber() > searchToolCard(11).getFavorPoint()) {
            handleFavorTokensNumber(searchToolCard(11));
            model.getCurrentPlayer().setToolCardUsed(true);
            FluxRemoverChooseDiceEvent fluxRemoverEvent = (FluxRemoverChooseDiceEvent) event;
            try {
                Dice diceChosenFromDraftPool = getDiceFromDraftPool(fluxRemoverEvent.getDiceIndex());
                model.getDiceBag().add(diceChosenFromDraftPool);
                model.getDraftPool().remove(diceChosenFromDraftPool);
                model.extractAndRollOneDice();
                Dice diceChosen = model.getDraftPool().get(model.getDraftPool().size() - 1);
                DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath());
                model.myNotify(draftPoolEvent);
                boolean succesfulMove = false;
                for(Position position : model.getCurrentPlayer().getDicePattern().getEmptyPositions()) {
                    if (model.getCurrentPlayer().getWindowPattern().checkCellColourRestriction(position, diceChosen)&&
                            model.getCurrentPlayer().getDicePattern().checkDicePatternLimitations(position,diceChosen) &&
                            model.getCurrentPlayer().getDicePattern().checkAdjacentColour(position, diceChosen)) {
                        succesfulMove = true;
                    }
                }
                if(!succesfulMove){
                    ErrorEvent errorEvent = new ErrorEvent("Non potevi inserire il dado in nessuna posizione\n");
                    view.showError(errorEvent);
                }
                else {
                    this.diceForFlux = diceChosen;
                    FluxRemoverChoiceEvent fluxRemoverChoiceEvent = new FluxRemoverChoiceEvent(diceChosen.toString());
                    view.fluxRemoverChoice(fluxRemoverChoiceEvent);
                }

            } catch (IndexOutOfBoundsException exception) {
                ErrorEvent errorEvent = new ErrorEvent("Non c'è nessun dado nella posizione che hai inserito\n");
                view.showError(errorEvent);
            }
        } else {
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
        }
    }



    private void tapWheelOneDiceMoved(Position initialPosition1, Position finalPosition1){
        try {
            model.getCurrentPlayer().getDicePattern().moveDice(initialPosition1, finalPosition1);
            model.getCurrentPlayer().setToolCardUsed(true);
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non puoi inserire un dado in questa posizione\n");
            view.showError(errorEvent);
        }
    }

    private void tapWheelTwoDiceMoved(Position initialPosition1, Position finalPosition1, Position initialPosition2, Position finalPosition2){
        try {
            model.getCurrentPlayer().getDicePattern().moveDice(initialPosition1, finalPosition1);
            model.getCurrentPlayer().getDicePattern().moveDice(initialPosition2, finalPosition2);
            model.getCurrentPlayer().setToolCardUsed(true);
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non puoi inserire un dado in questa posizione\n");
            view.showError(errorEvent);
        }
    }

    /**
     * Tool card 12 method
     */
    private void tapWheel() {
        if(model.getCurrentPlayer().getFavorTokensNumber() > searchToolCard(12).getFavorPoint()) {
            TapWheelEvent tapWheelEvent = (TapWheelEvent) event;
            int round = tapWheelEvent.getRoundIndex();
            int indexOfRoundTrack = tapWheelEvent.getDiceIndex();
            Dice diceChosenFromRoundTrack = getDiceFromRoundTrack(round, indexOfRoundTrack);
            Position initialPosition1 = tapWheelEvent.getFirstDicePosition();
            Position finalPosition1 = tapWheelEvent.getNewFirstDicePosition();
            Position initialPosition2 = tapWheelEvent.getSecondDicePosition();
            Position finalPosition2 = tapWheelEvent.getNewSecondDicePosition();
            handleFavorTokensNumber(searchToolCard(12));
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
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
        }//TODO: manca una gestione nel caso in cui non passi i controlli
    }

    /**
     * Calls the method associated to a specific event
     *
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
        }catch (IndexOutOfBoundsException | IllegalArgumentException exception) {
            ErrorEvent errorEvent;
            if(exception instanceof IndexOutOfBoundsException)
                errorEvent = new ErrorEvent("Non c'è nessun dado nella posizione che hai inserito\n");
            else
                errorEvent = new ErrorEvent("Non puoi inserire un dado in questa posizione\n");
            view.showError(errorEvent);
        }
    }

    /**
     * Changes the current player and if lap is the second, calls nextRound()
     */
    private void nextPlayer() {
        model.getCurrentPlayer().setDiceMoved(false);
        model.getCurrentPlayer().setToolCardUsed(false);
        if (model.getLap() == 0) {
            if (!model.getCurrentPlayer().equals(model.getPlayers().get(model.getPlayersNumber() - 1))) {  //if player isn't the last element of the array list
                model.setCurrentPlayer(model.getPlayers().get(model.getPlayers().indexOf(model.getCurrentPlayer()) + 1)); //currentPlayer is the one following player
            } else {
                //  model.setCurrentPlayer(player); //currentPlayer is still player *andrebbe cambiato in set..(currentPlayer) quindi è inutile
                model.setLap(1); //begins second turn of the round
            }


        } else if (model.getLap() == 1) {
            if (!model.getCurrentPlayer().equals(model.getPlayers().get(0))) {  //if player isn't the first element of the array list
                model.setCurrentPlayer(model.getPlayers().get(model.getPlayers().indexOf(model.getCurrentPlayer()) - 1)); //currentPlayer is the previous of player
            } else {
                model.setLap(0); //end of second turn
                nextRound();
            }
        }
    }


    /**
     * Increase round, changes players position, puts remaining dices from the draft pool to the round track,
     * extracts new dices from diceBag and show
     */
    private void nextRound() {
        model.increaseRound();
        Player toRemove =  model.getPlayers().remove(0);
        model.getPlayers().add(toRemove); //remove the first player, shift by one the other elements of players and then add the first player at the end of the array list
        model.setCurrentPlayer(model.getPlayers().get(0));
        model.fromDraftPoolToRoundTrack();
        model.extractAndRoll();
        // ShowAllEvent showAllEvent = new ShowAllEvent(model.dicePatternsToString(), model.playersToString(), model.publicObjectiveCardsToString(), model.toolCardsToString(), model.draftPoolToString(), model.getRoundTrack().toString(), model.getCurrentPlayer().getPrivateObjectiveCard().toString());
        // model.mySetChanged();
        // model.notifyObservers(showAllEvent);
    }


    /**
     * Compute all scores of the players
     */
    private void computeAllScores() {
        for (Player player : model.getPlayers()) {
            player.computeMyScore(model.getPublicObjectiveCards());
        }

    }

    /**
     * Skips current player's turn
     */
    private void skipTurn() {
        turnEnded = true;
        turnTimer.interrupt(); //è finito il turno del giocatore
        playerTurn.interrupt(); //non voglio più input
        synchronized (turnLock) {
            turnLock.notifyAll(); //risveglia il thread principale
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        event = (VCEvent) arg;
        performAction(event);
        synchronized (lock){
            lock.notifyAll();
        }
    }

    class TurnTimer extends Thread {

        @Override
        public void run() {
            try {
                sleep(turnDuration);
                skipTurn();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    class PlayerTurn extends Thread {

        @Override
        public void run() {
            while (!turnEnded) {
                view.showActionMenu(new ActionMenuEvent(model.getCurrentPlayer().isDiceMoved(), model.getCurrentPlayer().isToolCardUsed(),
                        model.toolCardsToStringAbbreviated()));
                view.getInput();
                if(model.getCurrentPlayer().getClientInterfaceRMI() == null) {
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
    }

    class Game extends Thread {

        @Override
        public void run() {
            model.extractAndRoll();
            for(Player player : model.getPlayers()){
                view.setCurrentPlayer(player);
                model.setCurrentPlayer(player);
                view.showAll(new ShowAllEvent(
                        new DicePatternEvent(model.dicePatternsToString(), model.playersToString(), model.dicePatternsToStringPath(), model.getCurrentPlayer().getName()),
                        model.publicObjectiveCardsToString(), model.publicObjectiveCardsToStringPath(),
                        new ToolCardEvent(model.toolCardsToString(), model.toolCardsToStringPath()),
                        new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath()),
                        new RoundTrackEvent(model.getRoundTrack().toString(), model.getRoundTrack().toStringPath()),
                        model.getCurrentPlayer().getPrivateObjectiveCard().toString(),model.getCurrentPlayer().getPrivateObjectiveCardToString(),
                        new SetWindowPatternsGUIEvent(model.windowPatternsToStringPath()))
                );
            }
            model.setCurrentPlayer(model.getPlayers().get(0));
            view.setCurrentPlayer(model.getPlayers().get(0));
            while (model.getRound() < 11) {
                turnTimer = new TurnTimer();
                playerTurn = new PlayerTurn();
                view.setCurrentPlayer(model.getCurrentPlayer());
                view.showAll(new ShowAllEvent(
                        new DicePatternEvent(model.dicePatternsToString(), model.playersToString(), model.dicePatternsToStringPath(), model.getCurrentPlayer().getName()),
                        model.publicObjectiveCardsToString(), model.publicObjectiveCardsToStringPath(),
                        new ToolCardEvent(model.toolCardsToString(), model.toolCardsToStringPath()),
                        new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath()),
                        new RoundTrackEvent(model.getRoundTrack().toString(), model.getRoundTrack().toStringPath()),
                        model.getCurrentPlayer().getPrivateObjectiveCard().toString(),model.getCurrentPlayer().getPrivateObjectiveCardToString(),
                        new SetWindowPatternsGUIEvent(model.windowPatternsToStringPath()))
                );
                turnEnded = false;
                turnTimer.start(); //thread for time
                playerTurn.start(); //thread to ask input

                synchronized (turnLock) {
                    try {
                        turnLock.wait();
                    } catch (InterruptedException e) {
                        System.out.println("Just one client left!");
                        Thread.currentThread().interrupt();
                        return;      //Ho dovuto perché interrupt() per qualche motivo non vuole
                    }
                }

                view.showEndTurn(new EndTurnEvent());
                nextPlayer();
            }
            model.myNotify(new GameEnded());
            showScores();
        }

        private void showScores() {

            computeAllScores();
            model.getPlayers().sort(Comparator.comparingInt(Player::getScore));
            Collections.reverse(model.getPlayers());
            List<Integer> scores = new ArrayList<>();
            for(Player player: model.getPlayers())
                scores.add(player.getScore());

            model.createScoreTrack(scores);
        }
    }

    public void endGame() {
        turnEnded = true;
        if (game.isAlive())
            game.interrupt();
        if (playerTurn.isAlive())
            playerTurn.interrupt();
        if (turnTimer.isAlive())
            turnTimer.interrupt();
        model.lastPlayer();
    }

    public void game() {
        game = new Game();
        game.start();
    }

}


