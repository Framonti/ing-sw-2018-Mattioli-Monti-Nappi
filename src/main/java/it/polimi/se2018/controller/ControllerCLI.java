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

//da controllare: eccezioni, update()

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
    private Dice diceForFluxBrush;

    /**
     * Constructor of the class.
     *
     * @param view      Virtual view
     * @param toolCards List of tool cards chosen during the setup.
     */
    //TODO DA METTERE METODO choosePlayerOrder di GameSetup
    //TODO AGGIUNGERE WINNER EVENT
    //TODO: rivedere inizializzazione di model nel costruttore
    public ControllerCLI(VirtualViewCLI view, List<ToolCard> toolCards, GameSingleton model, int turnDuration) {
        this.view = view;
        this.toolCards = toolCards;
        this.model = model;
        this.turnDuration = (long) turnDuration;
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
        eventsHandler.put(6, this::fluxBrushChooseDice);
        eventsHandler.put(7, this::glazingHammer);
        eventsHandler.put(8, this::runnerPliers);
        eventsHandler.put(9, this::corkBakedStraightedge);
        eventsHandler.put(10, this::grindingStone);
        eventsHandler.put(11, this::fluxRemover);
        eventsHandler.put(12, this::tapWheel);
        eventsHandler.put(13, this::fluxBrushPlaceDice);
        eventsHandler.put(99, this::placeDiceFromDraftPoolToDicePattern);
        eventsHandler.put(100, this::skipTurn);
        eventsHandler.put(-1, this::setWindowPatternPlayer);
    }

    //TODO rivedere TUTTO!!!
    public synchronized void game() {

        model.extractAndRoll();

        while (model.getRound() < 11) {
            turnTimer = new TurnTimer();
            playerTurn = new PlayerTurn();
            view.setCurrentPlayer(model.getCurrentPlayer());
            view.showAll(new ShowAllEvent(
                    new DicePatternEvent(model.dicePatternsToString(), model.playersToString()),
                    model.publicObjectiveCardsToString(),
                    new ToolCardEvent(model.toolCardsToString()),
                    new DraftPoolEvent(model.draftPoolToString()),
                    new RoundTrackEvent(model.getRoundTrack().toString()),
                    model.getCurrentPlayer().getPrivateObjectiveCard().toString())
            );
            try {
                turnEnded = false;
                turnTimer.start(); //thread for time
                playerTurn.start(); //thread to ask input
                wait();

                view.showEndTurn(new EndTurnEvent());
            } catch (InterruptedException e) {
                System.out.println("interrupted exception è grave");
            }
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
     *
     * @return The private objective card the player has asked for
     */
    private PrivateObjectiveCard getPrivateObjectiveCard() {
        return model.getCurrentPlayer().getPrivateObjectiveCard();
    }


    /**
     * Gets draft pool dice in diceIndex position
     *
     * @param diceIndex Dice index in the draft pool
     * @return Draft pool dice in diceIndex position
     */
    private Dice getDiceFromDraftPool(int diceIndex) {
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
        int choice = windowPatternChoiceEvent.getChoice();
        if (choice >= 0 && choice < 4) {
            model.getCurrentPlayer().setWindowPattern(model.getCurrentPlayer().getWindowPatterns().get(choice));
            if (model.getPlayers().indexOf(model.getCurrentPlayer()) != model.getPlayers().size() - 1)
                model.setCurrentPlayer(model.getPlayers().get(model.getPlayers().indexOf(model.getCurrentPlayer()) + 1));
            else
                model.setCurrentPlayer(model.getPlayers().get(0));
        } else {
            ErrorEvent errorEvent = new ErrorEvent("Non hai inserito un numero corretto\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }


    /**
     * Handles player's favorTokensNumber (when he decides to use a tool card) and favor tokens on the tool card
     *
     * @param toolCard Tool card chosen by the player
     */
    private void handleFavorTokensNumber(ToolCard toolCard) {
        if (toolCard.getFavorPoint() == 0) {
            toolCard.increaseFavorPoint(1);
            model.getCurrentPlayer().reduceFavorTokens(1);
            FavorTokensEvent favorTokensEvent = new FavorTokensEvent(String.valueOf(model.getCurrentPlayer().getFavorTokensNumber()), String.valueOf(model.getCurrentPlayer()));
            model.mySetChanged();
            model.notifyObservers(favorTokensEvent);
            ToolCardEvent toolCardEvent = new ToolCardEvent(model.toolCardsToString());
            model.mySetChanged();
            model.notifyObservers(toolCardEvent);


        } else {
            toolCard.increaseFavorPoint(2);
            model.getCurrentPlayer().reduceFavorTokens(2);
            FavorTokensEvent favorTokensEvent = new FavorTokensEvent(String.valueOf(model.getCurrentPlayer().getFavorTokensNumber()), String.valueOf(model.getCurrentPlayer()));
            model.mySetChanged();
            model.notifyObservers(favorTokensEvent);
            ToolCardEvent toolCardEvent = new ToolCardEvent(model.toolCardsToString());
            model.mySetChanged();
            model.notifyObservers(toolCardEvent);
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
            DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString());
            model.getCurrentPlayer().setToolCardUsed(true);
            model.mySetChanged();
            model.notifyObservers(draftPoolEvent);
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non puoi decrementare un dado con valore 1\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }


    private void grozingPliersAddOne(Dice diceChosen){
        try {
            diceChosen.addOne();
            DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString());
            model.getCurrentPlayer().setToolCardUsed(true);
            model.mySetChanged();
            model.notifyObservers(draftPoolEvent);
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non puoi incrementare un dado con valore 6\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }

    /**
     * Tool card 1 method
     */
    private void grozingPliers() {
        if(model.getCurrentPlayer().getFavorTokensNumber() >= searchToolCard(1).getFavorPoint()) {
            GrozingPliersEvent grozingPliersEvent = (GrozingPliersEvent) event;
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
        }
        else {
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }



    private void eglomiseBrushValidRestriction( Position initialPosition, Position finalPosition ){
        try {
            model.getCurrentPlayer().getDicePattern().moveDice(initialPosition, finalPosition);
            DicePatternEvent dicePatternEvent = new DicePatternEvent(model.getCurrentPlayer().getDicePattern().dicePatternToString(), model.playersToString());
            model.getCurrentPlayer().setToolCardUsed(true);
            model.mySetChanged();
            model.notifyObservers(dicePatternEvent);
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non stai rispettando le altre restrizioni di piazzamento\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }

    //TODO CONTROLLARE LE ECCEZIONI
    /**
     * Tool card 2 method
     */
    private void eglomiseBrush() {
        if(model.getCurrentPlayer().getFavorTokensNumber() >= searchToolCard(2).getFavorPoint()) {
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
                view.getInput();
            }
        }
        else {
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }


    private void copperFoilBurnisherValidRestriction(Position initialPosition, Position finalPosition){
        try {
            model.getCurrentPlayer().getDicePattern().moveDice(initialPosition, finalPosition);
            DicePatternEvent dicePatternEvent = new DicePatternEvent(model.getCurrentPlayer().getDicePattern().dicePatternToString(), model.playersToString());
            model.getCurrentPlayer().setToolCardUsed(true);
            model.mySetChanged();
            model.notifyObservers(dicePatternEvent);
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non stai rispettando le altre restrizioni di piazzamento\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }

    /**
     * Tool card 3 method
     */
    private void copperFoilBurnisher() {
        if(model.getCurrentPlayer().getFavorTokensNumber() >= searchToolCard(3).getFavorPoint()) {
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
                view.getInput();
            }
        }
        else {
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }

private void lathekinValidRestriction(Position initialPosition1, Position finalPosition1, Position initialPosition2, Position finalPosition2){
    try {
        model.getCurrentPlayer().getDicePattern().moveDice(initialPosition1, finalPosition1);
        model.getCurrentPlayer().getDicePattern().moveDice(initialPosition2, finalPosition2);
        DicePatternEvent dicePatternEvent = new DicePatternEvent(model.getCurrentPlayer().getDicePattern().dicePatternToString(), model.playersToString());
        model.getCurrentPlayer().setToolCardUsed(true);
        model.mySetChanged();
        model.notifyObservers(dicePatternEvent);
    } catch (IllegalArgumentException exception) {
        ErrorEvent errorEvent = new ErrorEvent("Non stai rispettando le restrizioni di piazzamento\n");
        view.showError(errorEvent);
        view.getInput();
    }
}


    /**
     * Tool card 4 method
     */
    private void lathekin() {
        if(model.getCurrentPlayer().getFavorTokensNumber() >= searchToolCard(4).getFavorPoint()) {
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
                    lathekinValidRestriction(initialPosition1, finalPosition1, initialPosition2, finalPosition2);
                    handleFavorTokensNumber(searchToolCard(4));
            } else {
                ErrorEvent errorEvent = new ErrorEvent("Non stai rispettando le altre restrizioni di piazzamento\n");
                view.showError(errorEvent);
                view.getInput();
            }
        }
        else{
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
            view.getInput();

        }
    }


    //TODO: CONTROLLARE SE VA BENE CHE QUESTA RILANCI L'ECCEZIONE E SIA POI LENS CUTTER A RICHIEDERE L'INPUT

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
            DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString());
            model.getCurrentPlayer().setToolCardUsed(true);
            model.mySetChanged();
            model.notifyObservers(draftPoolEvent);
            RoundTrackEvent roundTrackEvent = new RoundTrackEvent(model.getRoundTrack().toString());
            model.mySetChanged();
            model.notifyObservers(roundTrackEvent);
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
            view.getInput();
        }
    }

    /**
     * Tool card 5 method
     */
    private void lensCutter() {
        if(model.getCurrentPlayer().getFavorTokensNumber() >= searchToolCard(5).getFavorPoint()) {
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
            view.getInput();
        }
    }

    //TODO: rivedere il nome del metodo
    //il metodo inizialmente era void e c'era l'attributo successfulMove che però era del metodo chiamante
    private boolean fluxBrushPutDiceHelper(Position finalPosition, Dice diceChosen){
        try {
            model.getCurrentPlayer().getDicePattern().placeDice(finalPosition, diceChosen);
            //successfulMove = true;
            model.getDraftPool().remove(diceChosen);
            DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString());
            model.mySetChanged();
            model.notifyObservers(draftPoolEvent);
            DicePatternEvent dicePatternEvent = new DicePatternEvent(model.getCurrentPlayer().getDicePattern().dicePatternToString(), model.playersToString());
            model.getCurrentPlayer().setToolCardUsed(true);
            model.mySetChanged();
            model.notifyObservers(dicePatternEvent);
            return true;
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non puoi inserire il dado in questa posizione\n");
            view.showError(errorEvent);
            view.getInput();
            //successfulMove = false;
            return false;
        }
    }

    private void fluxBrushPlaceDice(){
        FluxBrushPlaceDiceEvent fluxBrushEvent = (FluxBrushPlaceDiceEvent) event;
        Position finalPosition = fluxBrushEvent.getFinalPosition();
        int i;
        boolean successfulMove = false;
        for (i = 0; i < model.getCurrentPlayer().getDicePattern().emptySpaces() && !successfulMove; i++) {
            //inizialmente non c'era l'assegnamento a successfulMove, visto che il metodo helper restituiva void
            successfulMove = fluxBrushPutDiceHelper(finalPosition, diceForFluxBrush);
        }
        if (!successfulMove) {
            ErrorEvent errorEvent = new ErrorEvent("Non potevi inserire il dado in nessuna posizione\n");
            view.showError(errorEvent);
        }
        else
            handleFavorTokensNumber(searchToolCard(6));
    }

    //TODO: controllare se può andare bene la gestione con il metodo helper che ritorna un booleano
    /**
     * Tool card 6 method
     */
    //mi tengo io la posizione
    private void fluxBrushChooseDice() {
        if (model.getCurrentPlayer().getFavorTokensNumber() >= searchToolCard(6).getFavorPoint()) {
            FluxBrushChooseDiceEvent fluxBrushEvent = (FluxBrushChooseDiceEvent) event;
            Dice diceChosen = getDiceFromDraftPool(fluxBrushEvent.getDiceIndexInDraftPool());
            diceChosen.roll();
            DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString());
            model.mySetChanged();
            model.notifyObservers(draftPoolEvent);
            this.diceForFluxBrush = diceChosen;
            view.fluxBrushChoice();
        } else {
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }

    /**
     * Tool card 7 method
     */
    private void glazingHammer() {
        if(model.getCurrentPlayer().getFavorTokensNumber() >= searchToolCard(7).getFavorPoint()) {
            int i;
            if (model.getLap() == 1) {
                for (i = 0; i < model.getDraftPool().size(); i++)
                    model.getDraftPool().get(i).roll();
                DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString());
                model.getCurrentPlayer().setToolCardUsed(true);
                model.mySetChanged();
                model.notifyObservers(draftPoolEvent);
                handleFavorTokensNumber(searchToolCard(7));
            } else {
                ErrorEvent errorEvent = new ErrorEvent("Non puoi usare questa carta durante il primo turno\n");
                view.showError(errorEvent);
                view.getInput();
            }
        }
        else{
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }


    private void runnerPliersHelper(Position finalPosition, Dice diceChosen){
        try {
            model.getCurrentPlayer().getDicePattern().placeDice(finalPosition, diceChosen);
            model.getDraftPool().remove(diceChosen);
            model.getCurrentPlayer().setLap(1);
            DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString());
            model.getCurrentPlayer().setToolCardUsed(true);
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

    /**
     * Tool card 8 method
     */
    private void runnerPliers() {
        if(model.getCurrentPlayer().getFavorTokensNumber() >= searchToolCard(8).getFavorPoint()) {
            RunnerPliersEvent runnerPliersEvent = (RunnerPliersEvent) event;
            Dice diceChosen = getDiceFromDraftPool(runnerPliersEvent.getDiceIndex());
            Position finalPosition = runnerPliersEvent.getPosition();
            runnerPliersHelper(finalPosition, diceChosen);
            handleFavorTokensNumber(searchToolCard(8));
        }
        else{
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }


    //DUBBIO

    /**
     * Tool card 9 method
     */
    private void corkBakedStraightedge() {
        if(model.getCurrentPlayer().getFavorTokensNumber() >= searchToolCard(9).getFavorPoint()) {
            CorkBakedStraightedgeEvent corkBakedStraightedgeEvent = (CorkBakedStraightedgeEvent) event;
            Dice diceChosen = getDiceFromDraftPool(corkBakedStraightedgeEvent.getIndexInDraftPool());
            Position finalPosition = corkBakedStraightedgeEvent.getFinalPosition();
            if (model.getCurrentPlayer().getWindowPattern().checkCell(finalPosition, diceChosen)) {
                model.getCurrentPlayer().getDicePattern().setDice(finalPosition, diceChosen);
                model.getDraftPool().remove(diceChosen);
                DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString());
                model.getCurrentPlayer().setToolCardUsed(true);
                model.mySetChanged();
                model.notifyObservers(draftPoolEvent);
                DicePatternEvent dicePatternEvent = new DicePatternEvent(model.getCurrentPlayer().getDicePattern().dicePatternToString(), model.playersToString());
                model.mySetChanged();
                model.notifyObservers(dicePatternEvent);
                handleFavorTokensNumber(searchToolCard(9));
            } else {
                ErrorEvent errorEvent = new ErrorEvent("Non puoi inserire un dado in questa posizione\n");
                view.showError(errorEvent);
                view.getInput();
            }
        } else {
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }


    /**
     * Tool card 10 method
     */
    private void grindingStone() {
        if(model.getCurrentPlayer().getFavorTokensNumber() >= searchToolCard(10).getFavorPoint()) {
            GrindingStoneEvent grindingStoneEvent = (GrindingStoneEvent) event;
            if (grindingStoneEvent.getDicePosition() <= model.getDraftPool().size()) {
                getDiceFromDraftPool(grindingStoneEvent.getDicePosition()).turn();
                DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString());
                model.getCurrentPlayer().setToolCardUsed(true);
                model.mySetChanged();
                model.notifyObservers(draftPoolEvent);
                handleFavorTokensNumber(searchToolCard(10));
            } else {
                ErrorEvent errorEvent = new ErrorEvent("Non c'è nessun dado in questa posizione\n");
                view.showError(errorEvent);
                view.getInput();
            }
        }
        else{
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }

    //DUBBIO

    /**
     * Tool card 11 method
     */
    private void fluxRemover() {
        if(model.getCurrentPlayer().getFavorTokensNumber() >= searchToolCard(11).getFavorPoint()) {
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
                DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString());
                model.getCurrentPlayer().setToolCardUsed(true);
                model.mySetChanged();
                model.notifyObservers(draftPoolEvent);
                DicePatternEvent dicePatternEvent = new DicePatternEvent(model.getCurrentPlayer().getDicePattern().dicePatternToString(), model.playersToString());
                model.mySetChanged();
                model.notifyObservers(dicePatternEvent);
                handleFavorTokensNumber(searchToolCard(11));
            } catch (IllegalArgumentException exception) {
                ErrorEvent errorEvent = new ErrorEvent("Non puoi inserire un dado in questa posizione\n");
                view.showError(errorEvent);
                view.getInput();
            }
        }
        else{
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }


    private void tapWheelOneDiceMoved(Position initialPosition1, Position finalPosition1){
        try {
            model.getCurrentPlayer().getDicePattern().moveDice(initialPosition1, finalPosition1);
            DicePatternEvent dicePatternEvent = new DicePatternEvent(model.getCurrentPlayer().getDicePattern().dicePatternToString(), model.playersToString());
            model.getCurrentPlayer().setToolCardUsed(true);
            model.mySetChanged();
            model.notifyObservers(dicePatternEvent);
        } catch (IllegalArgumentException exception) {
            ErrorEvent errorEvent = new ErrorEvent("Non puoi inserire un dado in questa posizione\n");
            view.showError(errorEvent);
            view.getInput();
        }
    }

    private void tapWheelTwoDiceMoved(Position initialPosition1, Position finalPosition1, Position initialPosition2, Position finalPosition2){
        try {
            model.getCurrentPlayer().getDicePattern().moveDice(initialPosition1, finalPosition1);
            model.getCurrentPlayer().getDicePattern().moveDice(initialPosition2, finalPosition2);
            DicePatternEvent dicePatternEvent = new DicePatternEvent(model.getCurrentPlayer().getDicePattern().dicePatternToString(), model.playersToString());
            model.getCurrentPlayer().setToolCardUsed(true);
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
    private void tapWheel() {
        if(model.getCurrentPlayer().getFavorTokensNumber() >= searchToolCard(12).getFavorPoint()) {
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
                        tapWheelOneDiceMoved(initialPosition1,finalPosition1);
                        handleFavorTokensNumber(searchToolCard(12));
            }

            //if the player wants to move two dices
            else if (initialPosition2 != null && finalPosition2 != null &&
                    model.getCurrentPlayer().getDicePattern().getDice(initialPosition1).getColour().equals(diceChosenFromRoundTrack.getColour()) &&
                    model.getCurrentPlayer().getDicePattern().checkDicePatternLimitations(finalPosition1, diceChosenFromRoundTrack) &&
                    model.getCurrentPlayer().getWindowPattern().checkCell(finalPosition1, diceChosenFromRoundTrack) &&
                    model.getCurrentPlayer().getDicePattern().getDice(initialPosition2).getColour().equals(diceChosenFromRoundTrack.getColour()) &&
                    model.getCurrentPlayer().getDicePattern().checkDicePatternLimitations(finalPosition2, diceChosenFromRoundTrack) &&
                    model.getCurrentPlayer().getWindowPattern().checkCell(finalPosition2, diceChosenFromRoundTrack)) {
                        tapWheelTwoDiceMoved(initialPosition1, finalPosition1, initialPosition2, finalPosition2);
                        handleFavorTokensNumber(searchToolCard(12));

            }
        }
        else{
            ErrorEvent errorEvent = new ErrorEvent("Non hai abbastanza segnalini favore\n");
            view.showError(errorEvent);
            view.getInput();
        }
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
        Dice diceChosen = getDiceFromDraftPool(placeDiceEvent.getDiceIndexDraftPool());
        Position finalPosition = placeDiceEvent.getPositionToPlace();
        model.getCurrentPlayer().getDicePattern().placeDice(finalPosition, diceChosen);
        model.getDraftPool().remove(diceChosen);
        model.getCurrentPlayer().setDiceMoved(true);
        DraftPoolEvent draftPoolEvent = new DraftPoolEvent(model.draftPoolToString());
        model.mySetChanged();
        model.notifyObservers(draftPoolEvent);
        DicePatternEvent dicePatternEvent = new DicePatternEvent(model.getCurrentPlayer().getDicePattern().dicePatternToString(), model.playersToString());
        model.mySetChanged();
        model.notifyObservers(dicePatternEvent);
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
        model.getPlayers().add(model.getPlayersNumber() - 1, model.getPlayers().remove(0)); //remove the first player, shift by one the other elements of players and then add the first player at the end of the array list
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
    private synchronized void skipTurn() {
        turnEnded = true;
        turnTimer.interrupt(); //è finito il turno del giocatore
        playerTurn.interrupt(); //non voglio più input
        notifyAll(); //risveglia il thread principale
    }


    @Override
    public void update(Observable o, Object arg) {
        event = (VCEvent) arg;
        performAction(event);
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
            }
        }
    }

}


