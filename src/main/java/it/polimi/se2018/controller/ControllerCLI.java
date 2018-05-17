package it.polimi.se2018.controller;

import it.polimi.se2018.events.*;
import it.polimi.se2018.model.*;
import it.polimi.se2018.view.ViewCLI;


import java.util.*;

//da controllare: eccezioni, update()

public class ControllerCLI implements Observer {
    private GameSingleton model;
    private ViewCLI view;
    private List<ToolCard> toolCards;
    private boolean isGameSetupEnded = false; //used for update method
    private Map<Integer, Runnable> eventsHandler = new HashMap<>();
    private Event event;

    //constructor TODO DA METTERE METODO choosePlayerOrder di GameSetup
    public ControllerCLI(ViewCLI view, List<ToolCard> toolCards) {
        this.view = view;
        this.toolCards = toolCards;
        createMap();
    }


    //TODo: mappare anche skipTurn
    private void createMap() {
        eventsHandler.put(6, this::fluxBrush);
        eventsHandler.put(11, this::fluxRemover);
        eventsHandler.put(7, this::glazingHammer);
        eventsHandler.put(10, this::grindingStone);
        eventsHandler.put(1, this::grozingPliers);
        eventsHandler.put(5, this::lensCutter);
        eventsHandler.put(2, this::eglomiseBrush);
        eventsHandler.put(3, this::copperFoilBurnisher);
        eventsHandler.put(4, this::lathekin);
        eventsHandler.put(99, this::placeDiceFromDraftPoolToDicePattern);
        eventsHandler.put(8, this::runnerPliers);
        eventsHandler.put(12, this::tapWheel);
        eventsHandler.put(9, this::corkBakedStraightedge);
        eventsHandler.put(-1, this::setWindowPatternPlayer);
        eventsHandler.put(100, this::skipTurn);
    }

    //TODO rivedere TUTTO!!!
    /*private void game() {
        while (model.getRound() < 10){
           while( true ) {
                view.showActionMenu(model.getCurrentPlayer().isDiceMoved(), model.getCurrentPlayer().isToolCardUsed());
                String input = view.getInput();
                update(model, input);
                if (input.equals("12"))
                    break;
            }
       }
        computeAllScores();
        view.showScoreTrack(model.getScoreTrack());
        view.printWinner(model.selectWinner());
    }*/


        //returns the private objective card the player has asked for
        private PrivateObjectiveCard getPrivateObjectiveCard () {
            return model.getCurrentPlayer().getPrivateObjectiveCard();
        }

        //returns the tool card chosen by a player
   /* public ToolCard getChosenToolCard(String userInput)  {
        int choice = Integer.parseInt(userInput);
        if ( choice == 1 || choice == 2 || choice == 3 || choice == 4 )
            return toolCards.get((choice-1));
        else
            throw new IllegalArgumentException("The number you chose is not available");
    }*/

        //returns the window pattern chosen by a player during setup
    /*private WindowPattern getWindowPattern(String userInput) {
        int choice = Integer.parseInt(userInput);
        if( choice == 1 || choice == 2 || choice == 3 || choice == 4)
            return model.getCurrentPlayer().getWindowPatterns().get(choice-1);
        else
            throw new IllegalArgumentException("The number you chose is no available");
    }*/

        //returns draft pool dice in userInput-1 position
        private Dice getDiceFromDraftPool ( int diceIndex){
            return model.getDraftPool().get(diceIndex - 1);
        }

        //returns the dice chosen by the  player, in its round track, in position (round-1, index-1)
        private Dice getDiceFromRoundTrack ( int round, int index){
            return model.getRoundTrack().getDice(round - 1, index - 1);
        }

        //returns the dice chosen by the  player in its dice pattern in position position
        private Dice getDiceFromDicePattern (Position position){
            return model.getCurrentPlayer().getDicePattern().getDice(position);
        }

        //DA CONTROLLARE ECCEZIONI
        //sets window pattern of the player
        private void setWindowPatternPlayer () {
            WindowPatternChoiceEvent windowPatternChoiceEvent = (WindowPatternChoiceEvent) event;
            int choice = windowPatternChoiceEvent.getChoice();
            if (choice == 1 || choice == 2 || choice == 3 || choice == 4)
                model.getCurrentPlayer().setWindowPattern(model.getCurrentPlayer().getWindowPatterns().get(choice - 1));
            else
                throw new IllegalArgumentException("Number out of range");
        }


        //MANCA GESTIONE ECCEZIONI
        //method used to handle player's favorTokensNumber when he decides to use a tool card
        private void handleFavorTokensNumber (ToolCard toolCard){
            if (toolCard.getFavorPoint() == 0) {
                try {
                    toolCard.increaseFavorPoint(1);
                    model.getCurrentPlayer().reduceFavorTokens(1);
                } catch (UnsupportedOperationException exception) {
                    view.printMessage(exception.toString());
                }
            } else {
                try {
                    toolCard.increaseFavorPoint(2);
                    model.getCurrentPlayer().reduceFavorTokens(2);
                } catch (UnsupportedOperationException exception) {
                    view.printMessage(exception.toString());
                }
            }
        }

        //tool card 1 method
        private void grozingPliers () {
            GrozingPliersEvent grozingPliersEvent = (GrozingPliersEvent) event;
            Dice diceChosen = getDiceFromDraftPool(grozingPliersEvent.getDiceIndexDraftPool());
            int choice = grozingPliersEvent.getChoice();
            //if player wants to decrease the value of the dice by one
            if (choice == 1) {
                try {
                    diceChosen.subOne();
                } catch (IllegalArgumentException exception) {
                    view.printMessage(exception.toString());
                    throw new IllegalArgumentException("non valid input");
                }

            }
            if (choice == 2) {
                try {
                    diceChosen.addOne();
                } catch (IllegalArgumentException exception) {
                    view.printMessage(exception.toString());
                    throw new IllegalArgumentException("non valid input");
                }
            }
        }

        //tool card 2 method
        public void eglomiseBrush () {
            EglomiseBrushEvent eglomiseBrushEvent = (EglomiseBrushEvent) event;
            Dice diceChosen = getDiceFromDicePattern(eglomiseBrushEvent.getInitialPosition());
            Position finalPosition = eglomiseBrushEvent.getFinalPosition();
            if (model.getCurrentPlayer().getWindowPattern().checkCellValueRestriction(finalPosition, diceChosen) &&
                    model.getCurrentPlayer().getDicePattern().checkAdjacency(finalPosition) &&
                    model.getCurrentPlayer().getDicePattern().checkAdjacentColour(finalPosition, diceChosen) &&
                    model.getCurrentPlayer().getDicePattern().checkAdjacentValue(finalPosition, diceChosen)) {
                try {
                    model.getCurrentPlayer().getDicePattern().moveDice(eglomiseBrushEvent.getInitialPosition(), finalPosition);
                } catch (IllegalArgumentException exception) {
                    view.printMessage(exception.toString());
                    throw new IllegalArgumentException("non valid input");
                }
            }
        }

        //tool card 3 method
        public void copperFoilBurnisher () {
            CopperFoilBurnisherEvent copperFoilBurnisherEvent = (CopperFoilBurnisherEvent) event;
            Dice diceChosen = getDiceFromDicePattern(copperFoilBurnisherEvent.getInitialPosition());
            Position finalPosition = copperFoilBurnisherEvent.getFinalPosition();
            if (model.getCurrentPlayer().getWindowPattern().checkCellColourRestriction(finalPosition, diceChosen) &&
                    model.getCurrentPlayer().getDicePattern().checkAdjacency(finalPosition) &&
                    model.getCurrentPlayer().getDicePattern().checkAdjacentColour(finalPosition, diceChosen) &&
                    model.getCurrentPlayer().getDicePattern().checkAdjacentValue(finalPosition, diceChosen)) {
                try {
                    model.getCurrentPlayer().getDicePattern().moveDice(copperFoilBurnisherEvent.getInitialPosition(), finalPosition);
                } catch (IllegalArgumentException exception) {
                    view.printMessage(exception.toString());
                    throw new IllegalArgumentException("non valid input");
                }
            }

        }


        //tool card 4 method
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
                } catch (IllegalArgumentException exception) {
                    view.printMessage(exception.toString());
                    throw new IllegalArgumentException("non valid input");
                }
            }

        }


        //MANCANO le eccezioni, penso vadano messe in getDice
        //method used by tool card 5. Swap a dice in the draft pool with a dice in the round track
        public void swapDice ( int round, int indexOfRoundTrack, int indexOfDraftPool){
            model.getRoundTrack().getList(round).add(model.getDraftPool().get(indexOfDraftPool));
            model.getDraftPool().remove(indexOfDraftPool);
            model.getDraftPool().add(model.getRoundTrack().getDice(round, indexOfRoundTrack));
            model.getRoundTrack().getList(round).remove(indexOfRoundTrack);
        }

        //MANCANO LE ECCEZIONI
        //tool card 5 method
        private void lensCutter () {
            LensCutterEvent lensCutterEvent = (LensCutterEvent) event;
            swapDice(lensCutterEvent.getRoundIndex() - 1, lensCutterEvent.getDiceIndexInRoundTrack() - 1, lensCutterEvent.getDiceIndexInDraftPool() - 1);
        }


        //method used by tool card 6 and 7
        private void throwAgainDiceFromDraftPool (Dice dice){
            dice.roll();
        }


        //tool card 6 method
        private void fluxBrush () {
            FluxBrushEvent fluxBrushEvent = (FluxBrushEvent) event;
            Dice diceChosen = getDiceFromDraftPool(fluxBrushEvent.getDiceIndexInDraftPool());
            throwAgainDiceFromDraftPool(diceChosen);
            int i;
            boolean successfulMove = false;
            for (i = 0; i < model.getCurrentPlayer().getDicePattern().emptySpaces() && !successfulMove; i++) {
                view.printMessage("Dove vuoi posizionare il dado?\n");
                try {
                    model.getCurrentPlayer().getDicePattern().placeDice(fluxBrushEvent.getFinalPosition(), diceChosen);
                    successfulMove = true;
                    model.getDraftPool().remove(diceChosen);
                } catch (IllegalArgumentException exception) {
                    view.printMessage(exception.toString());
                    successfulMove = false;
                }
            }
            if (!successfulMove)
                view.printMessage("non potevi inserire il dado in alcuna posizione\n");
        }


        //tool card 7 method
        private void glazingHammer () {
            int i;
            if (model.getCurrentPlayer().getLap() == 1) {
                for (i = 0; i < model.getDraftPool().size(); i++)
                    throwAgainDiceFromDraftPool(model.getDraftPool().get(i));
            } else
                view.printMessage("Non puoi usare questa carta durante il primo turno\n");
        }

        //DUBBIO: posso mettere il metodo placeDiceFromDraftPoolToDicePattern in un try- catch e basta? perchè il codice è lo stesso
        //tool card 8 method
        private void runnerPliers () {
            RunnerPliersEvent runnerPliersEvent = (RunnerPliersEvent) event;
            Dice diceChosen = getDiceFromDraftPool(runnerPliersEvent.getDiceIndex());
            Position finalPosition = runnerPliersEvent.getPosition();
            try {
                model.getCurrentPlayer().getDicePattern().placeDice(finalPosition, diceChosen);
                model.getDraftPool().remove(diceChosen);
                model.getCurrentPlayer().setLap(1);
            } catch (IllegalArgumentException exception) {
                view.printMessage(exception.toString());
                throw new IllegalArgumentException("non valid input");
            }
        }

        //DUBBIO
        //tool card 9 method
        private void corkBakedStraightedge () {
            CorkBakedStraightedgeEvent corkBakedStraightedgeEvent = (CorkBakedStraightedgeEvent) event;
            Dice diceChosen = getDiceFromDraftPool(corkBakedStraightedgeEvent.getIndexInDraftPool());
            Position finalPosition = corkBakedStraightedgeEvent.getFinalPosition();
            if (model.getCurrentPlayer().getWindowPattern().checkCell(finalPosition, diceChosen)) {
                model.getCurrentPlayer().getDicePattern().setDice(finalPosition, diceChosen);
                model.getDraftPool().remove(diceChosen);
            } else
                throw new IllegalArgumentException("non valid input");
        }


        //tool card 10 method
        private void grindingStone () {
            GrindingStoneEvent grindingStoneEvent = (GrindingStoneEvent) event;
            getDiceFromDraftPool(grindingStoneEvent.getDicePosition()).turn();
        }

        //DUBBIO
        //tool card 11 method
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
            } catch (IllegalArgumentException exception) {
                view.printMessage(exception.toString());
                throw new IllegalArgumentException("non valid input");
            }
        }

        //TODO CONTROLLARE SE BISOGNA LANCIARE ECCEZIONI AL LIVELLO PIù ALTO
        //tool card 12 method
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
                } catch (IllegalArgumentException exception) {
                    view.printMessage(exception.toString());
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
                } catch (IllegalArgumentException exception) {
                    view.printMessage(exception.toString());
                }
            }
        }


        //DUBBIO: l'ho messo void al posto che boolean
    /*public void useToolCard(){
        String userInput = view.getInput();
        try {
            ToolCard toolCardToUse = getChosenToolCard(userInput);

            switch (toolCardToUse.getName()) {

                //tool card selected is "Pinza Sgrossatrice"
                case "Pinza Sgrossatrice":
                    grozingPliers();
                    handleFavorTokensNumber(toolCardToUse.getName());
                    break;

                //tool card selected is "Pennello per Eglomise"
                case "Pennello per Eglomise":
                    eglomiseBrush();
                    handleFavorTokensNumber(toolCardToUse.getName());
                    break;

                //tool card selected is "Alesatore per lamina di rame"
                case "Alesatore per lamina di rame":
                    copperFoilBurnisher();
                    handleFavorTokensNumber(toolCardToUse.getName());
                    break;

                //tool card selected is "Lathekin"
                case "Lathekin":
                    lathekin();
                    handleFavorTokensNumber(toolCardToUse.getName());
                    break;

                //tool card selected is "Taglierina circolante"
                case "Taglierina circolante":
                    lensCutter();
                    handleFavorTokensNumber(toolCardToUse.getName());
                    break;

                //tool card selected is "Pennello per Pasta Salda"
                case "Pennello per Pasta Salda":
                    fluxBrush();
                    handleFavorTokensNumber(toolCardToUse.getName());
                    break;

                //tool card selected is "Martelletto"
                case "Martelletto":
                    glazingHammer();
                    handleFavorTokensNumber(toolCardToUse.getName());
                    break;

                //tool card selected is "Tenaglia a rotelle"
                case "Tenaglia a rotelle":
                    runnerPliers();
                    handleFavorTokensNumber(toolCardToUse.getName());
                    break;

                //tool card selected is "Riga in Sughero"
                case "Riga in Sughero":
                    corkBakedStraightedge();
                    handleFavorTokensNumber(toolCardToUse.getName());
                    break;

                //tool card selected is "Tampone Diamantato"
                case "Tampone Diamantato":
                    grindingStone();
                    handleFavorTokensNumber(toolCardToUse.getName());
                    break;

                //tool card selected is "Diluente per Pasta Salda"
                case "Diluente per Pasta Salda":
                    fluxRemover();
                    handleFavorTokensNumber(toolCardToUse.getName());
                    break;

                //tool card selected is "Taglierina Manuale"
                case "Taglierina Manuale":
                    tapWheel();
                    handleFavorTokensNumber(toolCardToUse.getName());
                    break;

                default:
                    break;

            }
            model.getCurrentPlayer().reverseToolCardUsed();
        } catch (IllegalArgumentException exception){
            view.printMessage(exception.toString());
            useToolCard();
        }

    }*/


        private void performAction (Event event){
            eventsHandler.get(event.getId()).run();
        }


        //DUBBIO: da controllare se va bene boolean e come ho assegnato il return
        //prende in ingresso oggetto. eventHandler.get(oggetto passato in ingresso.toString()).run()
   /* public boolean performAction (String userInput ){

        int choice = Integer.parseInt(userInput);
        switch(choice){

            //vedere carte utensile
            case 1:
                view.showToolCards(toolCards);
                break;

            //vedere le carte obiettivo pubblico
            case 2:
                view.showPublicObjectiveCards(model.getPublicObjectiveCards());
                break;

            // vedere la carta obiettivo privato
            case 3:
                view.showPrivateObjectiveCard(this.getPrivateObjectiveCard());
                break;

            //vedere la propria carta schema
            case 4:
                view.showMyDicePattern(model.getCurrentPlayer());
                break;

            //vedere le carte schema degli avversari
            case 5:
                view.showOthersDicePatterns(model);
                break;

            //vedere tutte le carte schema in gioco
            case 6:
                view.showAllDicePatterns(model);
                break;

            //vedere la riserva
            case 7:
                view.showDraftPool(model.getDraftPool());
                break;

            //vedere il tracciato dei round
            case 8:
                view.showRoundTrack(model.getRoundTrack());
                break;

            //vedere tutto il tavolo di gioco
            case 9:
                view.showAll(model);
                break;

            //usare una carta utensile
            case 10:
                if(!model.getCurrentPlayer().isToolCardUsed()) {
                    view.showToolCards(toolCards);
                    useToolCard();
                }
                else{
                    view.printInvalidAction("usare una carta utensile");
                    return performAction(view.getInput());
                }

                break;

            //posizionare un dado della riserva nel proprio schema
            case 11:
                if(!model.getCurrentPlayer().isDiceMoved())
                    placeDiceFromDraftPoolToDicePattern();
                else{
                    view.printInvalidAction("muovere dado");
                    return performAction(view.getInput());
                }
                break;

            //passa turno TODO AGGIUNGERE EVENTO
            case 12:
                skipTurn();
                break;

            //no matches
            default:
                view.printInvalidAction("il numero inserito non corrisponde a nessuna operazione possibile, riprova\n");
                return false;

        }
        return true;
    }*/

        private void placeDiceFromDraftPoolToDicePattern (){
            PlaceDiceEvent placeDiceEvent = (PlaceDiceEvent) event;
            Dice diceChosen = getDiceFromDraftPool(placeDiceEvent.getDiceIndexDraftPool());
            Position finalPosition = placeDiceEvent.getPositionToPlace();
            model.getCurrentPlayer().getDicePattern().placeDice(finalPosition, diceChosen);
            model.getDraftPool().remove(diceChosen);
            model.getCurrentPlayer().reverseDiceMoved();
        }

        //changes the current player
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

        //increase round, change players position, put remaining dices from the draft poll to the round track extract new dices from diceBag and show all
        private void nextRound () {
            model.increaseRound();
            model.getPlayers().add(model.getPlayersNumber() - 1, model.getPlayers().remove(0)); //remove the first player, shift by one the other elements of players and then add the first player at the end of the array list
            model.setCurrentPlayer(model.getPlayers().get(0));
            model.fromDraftPoolToRoundTrack();
            model.extractAndRoll();
            view.showAll(model);
        }

        //compute all scores of the players
        private void computeAllScores () {
            for (Player player : model.getPlayers()) {
                player.computeMyScore(model.getPublicObjectiveCards());
            }

        }

        //
        public void skipTurn () {
            nextPlayer();
        }


        //recupera la stringa passata arg e
    /*@Override
    public void update(Observable o, Object arg) { //o è la view, arg è l'evento. chiama performaction
        String userInput = arg.toString();
        if(!isGameSetupEnded){
            setWindowPatternPlayer(userInput);
            isGameSetupEnded = true;
        }
        else{
            performAction(userInput);
        }*/


        //TODO controllare se va bene
        @Override
        public void update (Observable o, Object arg){
            event = (Event)arg;
            performAction(event);
        }


}
