package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.ViewCLI;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

//da controllare: eccezioni, update()

public class ControllerCLI implements Observer {
    private GameSingleton model;
    private ViewCLI view;
    private List<ToolCard> toolCards;
    private boolean isGameSetupEnded = false; //used for update method


    //constructor TODO
    public ControllerCLI( ViewCLI view, List<ToolCard> toolCards ){
        this.view = view;
        this.toolCards = toolCards;
    }


    public void game(){
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
    }


    //returns the private objective card the player has asked for
    public PrivateObjectiveCard getPrivateObjectiveCard(){
        return model.getCurrentPlayer().getPrivateObjectiveCard();
    }

    //returns the tool card chosen by a player
    public ToolCard getChosenToolCard(String userInput)  {
        int choice = Integer.parseInt(userInput);
        if ( choice == 1 || choice == 2 || choice == 3 || choice == 4 )
            return toolCards.get((choice-1));
        else
            throw new IllegalArgumentException("The number you chose is not available");
    }

    //returns the window pattern chosen by a player during setup
    public WindowPattern getWindowPattern(String userInput) {
        int choice = Integer.parseInt(userInput);
        if( choice == 1 || choice == 2 || choice == 3 || choice == 4)
            return model.getCurrentPlayer().getWindowPatterns().get(choice-1);
        else
            throw new IllegalArgumentException("The number you chose is no available");
    }

    //returns draft pool dice in userInput-1 position
    public Dice getDiceFromDraftPool(String userInput){
        int index = Integer.parseInt(userInput);
        return model.getDraftPool().get(index-1);
    }

    //returns the dice chosen by the  player, in its round track, in position (round-1, index-1)
    public Dice getDiceFromRoundTrack(int round, int index){
        return model.getRoundTrack().getDice( round-1, index-1 );
    }

    //returns the dice chosen by the  player in its dice pattern in position
    public Dice getDiceFromDicePattern(Position position){
        return model.getCurrentPlayer().getDicePattern().getDice(position);
    }

    //DA CONTROLLARE ECCEZIONI
    //sets window pattern of the player
    public void setWindowPatternPlayer(String userInput){
        int choice = Integer.parseInt(userInput);
        if ( choice == 1 || choice == 2 || choice == 3 || choice == 4)
            model.getCurrentPlayer().setWindowPattern(model.getCurrentPlayer().getWindowPatterns().get(choice-1));
    }


    //MANCA GESTIONE ECCEZIONI
    //method used to handle player's favorTokensNumber when he decides to use a tool card
    public void handleFavorTokensNumber(String toolCardName){
        int i;
        i = 0;
        while(!this.toolCards.get(i).getName().equals(toolCardName))
            i++;
        if(this.toolCards.get(i).getFavorPoint() == 0) {
            try {
                this.toolCards.get(i).increaseFavorPoint(1);
                model.getCurrentPlayer().reduceFavorTokens(1);
            } catch (UnsupportedOperationException exception){
                view.printMessage(exception.toString());
            }
        }
        else {
            try {
                this.toolCards.get(i).increaseFavorPoint(2);
                model.getCurrentPlayer().reduceFavorTokens(2);
            } catch (UnsupportedOperationException exception){
                view.printMessage(exception.toString());
            }
        }
    }

    //tool card 1 method
    public void grozingPilers(){
        //choose dice from draft pool
        Dice diceChosen= getDiceFromDraftPool(view.getInput());
        String userInput = view.getInput();
        int choice = Integer.parseInt(userInput);
        //if player wants to decrease the value of the dice by one
        if (choice == 1 ) {
            try{
                diceChosen.subOne();
            } catch (IllegalArgumentException exception){
                view.printMessage(exception.toString());
                grozingPilers();
            }

        }
        if (choice == 2 ) {
            try{
                diceChosen.addOne();
            } catch (IllegalArgumentException exception){
                view.printMessage(exception.toString());
                grozingPilers();
            }

        }

    }

    //tool card 2 method
    public void eglomiseBrush(){
        Position dicePosition = new Position(Integer.parseInt(view.getInput()) -1 ,Integer.parseInt(view.getInput()) -1);
        Dice diceChosen = getDiceFromDicePattern(dicePosition);
        int row = Integer.parseInt(view.getInput());
        int column = Integer.parseInt(view.getInput());
        Position finalPosition = new Position(row-1, column-1);
        if ( model.getCurrentPlayer().getWindowPattern().checkCellValueRestriction(finalPosition, diceChosen) &&
            model.getCurrentPlayer().getDicePattern().checkAdjacency(finalPosition) &&
            model.getCurrentPlayer().getDicePattern().checkAdjacentColour(finalPosition, diceChosen) &&
            model.getCurrentPlayer().getDicePattern().checkAdjacentValue(finalPosition, diceChosen)) {
            try {
                model.getCurrentPlayer().getDicePattern().moveDice(dicePosition, finalPosition);
            } catch (IllegalArgumentException exception) {
                view.printMessage(exception.toString());
                eglomiseBrush();
            }
        }
    }

    //tool card 3 method
    public void copperFoilBurnisher(){
        Position dicePosition = new Position(Integer.parseInt(view.getInput()) -1 ,Integer.parseInt(view.getInput()) -1);
        Dice diceChosen = getDiceFromDicePattern(dicePosition);
        int row = Integer.parseInt(view.getInput());
        int column = Integer.parseInt(view.getInput());
        Position finalPosition = new Position(row-1, column-1);
        if ( model.getCurrentPlayer().getWindowPattern().checkCellColourRestriction(finalPosition, diceChosen) &&
                model.getCurrentPlayer().getDicePattern().checkAdjacency(finalPosition) &&
                model.getCurrentPlayer().getDicePattern().checkAdjacentColour(finalPosition, diceChosen) &&
                model.getCurrentPlayer().getDicePattern().checkAdjacentValue(finalPosition, diceChosen)) {
            try {
                model.getCurrentPlayer().getDicePattern().moveDice(dicePosition, finalPosition);
            } catch (IllegalArgumentException exception) {
                view.printMessage(exception.toString());
                copperFoilBurnisher();
            }
        }

    }


    //tool card 4 method
    public void lathekin(){
        int i;
        for(i = 0; i < 2; i++) {
            Position dicePosition = new Position(Integer.parseInt(view.getInput()) - 1, Integer.parseInt(view.getInput()) - 1); //position of dice the player wants to move
            Dice diceChosen = getDiceFromDicePattern(dicePosition);
            int row = Integer.parseInt(view.getInput());
            int column = Integer.parseInt(view.getInput());
            Position finalPosition = new Position(row - 1, column - 1);
            if(model.getCurrentPlayer().getDicePattern().checkDicePatternLimitations(finalPosition,diceChosen) && model.getCurrentPlayer().getWindowPattern().checkCell(finalPosition,diceChosen)){
                try {
                    model.getCurrentPlayer().getDicePattern().moveDice(dicePosition, finalPosition);
                } catch (IllegalArgumentException exception){
                    view.printMessage(exception.toString());
                    lathekin();
                }

            }

        }

    }


    //MANCANO le eccezioni, penso vadano messe in getDice
    //method used by tool card 5. Swap a dice in the draft pool with a dice in the round track
    public void swapDice( int round, int indexOfRoundTrack, int indexOfDraftPool) {
        model.getRoundTrack().getList(round).add(model.getDraftPool().get(indexOfDraftPool));
        model.getDraftPool().remove(indexOfDraftPool);
        model.getDraftPool().add(model.getRoundTrack().getDice(round, indexOfRoundTrack ));
        model.getRoundTrack().getList(round).remove(indexOfRoundTrack);
    }

    //MANCANO LE ECCEZIONI
    //tool card 5 method
    public void lensCutter(){
        int round = Integer.parseInt(view.getInput());
        int indexOfRoundTrack = Integer.parseInt(view.getInput());
        int indexOfDraftPool = Integer.parseInt(view.getInput());
        swapDice(round-1, indexOfRoundTrack-1, indexOfDraftPool-1);
    }


    //method used by tool card 6 and 7
    public void throwAgainDiceFromDraftPool( Dice dice){
        dice.roll();
    }


    //tool card 6 method
    public void fluxBrush(){
        Dice diceChosen= getDiceFromDraftPool(view.getInput());
        throwAgainDiceFromDraftPool(diceChosen);
        int i;
        boolean successfulMove = false;
        for (i = 0; i < model.getCurrentPlayer().getDicePattern().emptySpaces() && !successfulMove; i++) {
            view.printMessage("Dove vuoi posizionare il dado?\n");
            int row = Integer.parseInt(view.getInput());
            int column = Integer.parseInt(view.getInput());
            try{
                model.getCurrentPlayer().getDicePattern().placeDice(new Position(row - 1, column - 1), diceChosen);
                successfulMove = true;
                model.getDraftPool().remove(diceChosen);
            } catch (IllegalArgumentException exception){
                view.printMessage(exception.toString());
                successfulMove = false;
            }
        }
        if (!successfulMove)
            view.printMessage("non potevi inserire il dado in alcuna posizione\n");
    }



    //tool card 7 method
    public void glazingHammer(){
        int i;
        if ( model.getCurrentPlayer().getLap() == 1){
            for (i = 0 ; i < model.getDraftPool().size(); i++)
                throwAgainDiceFromDraftPool(model.getDraftPool().get(i));
        }
        else
            view.printMessage("Non puoi usare questa carta durante il primo turno\n");
    }

    //DUBBIO: posso mettere il metodo placeDiceFromDraftPoolToDicePattern in un try- catch e basta? perchè il codice è lo stesso
    //tool card 8 method
    public void runnerPliers(){
        Dice diceChosen = getDiceFromDraftPool(view.getInput());
        int row = Integer.parseInt(view.getInput());
        int column = Integer.parseInt(view.getInput());
        Position finalPosition = new Position(row-1, column-1 );
        try{
            model.getCurrentPlayer().getDicePattern().placeDice(finalPosition, diceChosen);
            model.getDraftPool().remove(diceChosen);
            model.getCurrentPlayer().setLap(1);
        } catch (IllegalArgumentException exception) {
            view.printMessage(exception.toString());
            runnerPliers();
        }
    }

    //DUBBIO
    //tool card 9 method
    public void  corkBakedStraightedge() {
        Dice diceChosen = getDiceFromDraftPool(view.getInput());
        int row = Integer.parseInt(view.getInput());
        int column = Integer.parseInt(view.getInput());
        Position finalPosition = new Position(row - 1, column - 1);
        if (model.getCurrentPlayer().getWindowPattern().checkCell(finalPosition, diceChosen)) {
            model.getCurrentPlayer().getDicePattern().setDice(finalPosition, diceChosen);
            model.getDraftPool().remove(diceChosen);
        } else
            corkBakedStraightedge();
    }


    //tool card 10 method
    public void grindingStone(){
        getDiceFromDraftPool(view.getInput()).turn();
    }

    //DUBBIO
    //tool card 11 method
    public void fluxRemover(){
        Dice diceChosenFromDraftPool = getDiceFromDraftPool(view.getInput());
        model.getDiceBag().add(diceChosenFromDraftPool);
        model.getDraftPool().remove(diceChosenFromDraftPool);
        model.extractAndRoll();
        Dice diceChosenFromDiceBag = model.getDraftPool().get(model.getDraftPool().size()-1);
        diceChosenFromDiceBag.setValue(Integer.parseInt(view.getInput()));
        int row = Integer.parseInt(view.getInput());
        int column = Integer.parseInt(view.getInput());
        Position finalPosition = new Position(row-1, column-1 );
        try{
            model.getCurrentPlayer().getDicePattern().placeDice(finalPosition, diceChosenFromDiceBag);
            model.getDraftPool().remove(diceChosenFromDraftPool);
        } catch (IllegalArgumentException exception) {
            view.printMessage(exception.toString());
            fluxRemover();
        }
    }

    //tool card 12 method
    public void tapWheel(){
        int round = Integer.parseInt(view.getInput());
        int indexOfRoundTrack = Integer.parseInt(view.getInput());
        Dice diceChosenFromRoundTrack = getDiceFromRoundTrack(round, indexOfRoundTrack);
        int i;
        for (i = 0; i < 2; i++){
            int initialRow = Integer.parseInt(view.getInput());
            int initialColumn = Integer.parseInt(view.getInput());
            Position initialPosition = new Position (initialRow-1, initialColumn-1 );
            int finalRow = Integer.parseInt(view.getInput());
            int finalColumn = Integer.parseInt(view.getInput());
            Position finalPosition = new Position ( finalRow-1, finalColumn-1);
            if(model.getCurrentPlayer().getDicePattern().getDice(initialPosition).getColour().equals(diceChosenFromRoundTrack.getColour()) &&
                model.getCurrentPlayer().getDicePattern().checkDicePatternLimitations(finalPosition,diceChosenFromRoundTrack) &&
                model.getCurrentPlayer().getWindowPattern().checkCell(finalPosition,diceChosenFromRoundTrack)){
                try{
                    model.getCurrentPlayer().getDicePattern().moveDice(initialPosition, finalPosition);
                } catch (IllegalArgumentException exception) {
                    view.printMessage(exception.toString());
                    if(i == 0)
                        tapWheel();
                    else
                        i=1;
                }
            }
        }
    }


    //DUBBIO: l'ho messo void al posto che boolean
    public void useToolCard(){
        String userInput = view.getInput();
        try {
            ToolCard toolCardToUse = getChosenToolCard(userInput);

            switch (toolCardToUse.getName()) {

                //tool card selected is "Pinza Sgrossatrice"
                case "Pinza Sgrossatrice":
                    grozingPilers();
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

            }
            model.getCurrentPlayer().reverseToolCardUsed();
        } catch (IllegalArgumentException exception){
            view.printMessage(exception.toString());
            useToolCard();
        }

    }



    //DUBBIO: da controllare se va bene boolean e come ho assegnato il return
    public boolean performAction (String userInput){
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

            //passa turno
            case 12:
                skipTurn();
                break;

            //no matches
            default:
                view.printInvalidAction("il numero inserito non corrisponde a nessuna operazione possibile, riprova\n");
                return false;

        }
        return true;
    }

    public void placeDiceFromDraftPoolToDicePattern(){
        Dice diceChosen = getDiceFromDraftPool(view.getInput());
        int row = Integer.parseInt(view.getInput());
        int column = Integer.parseInt(view.getInput());
        Position finalPosition = new Position(row-1, column-1 );
        model.getCurrentPlayer().getDicePattern().placeDice(finalPosition, diceChosen);
        model.getDraftPool().remove(diceChosen);
        model.getCurrentPlayer().reverseDiceMoved();
    }

    //changes the current player
    public void nextPlayer(){
        if (model.getLap() == 0){
            for( Player player : model.getPlayers()){
                if(!player.equals(model.getPlayers().get(model.getPlayersNumber()-1))){  //if player isn't the last element of the array list
                    model.setCurrentPlayer(model.getPlayers().get(model.getPlayers().indexOf(player)+1)); //currentPlayer is the one following player
                }
                else{
                    model.setCurrentPlayer(player); //currentPlayer is still player
                    model.setLap(1); //begins second turn of the round
                }

            }

        }
        else if (model.getLap() == 1){
            for( Player player : model.getPlayers()){
                if(!player.equals(model.getPlayers().get(0))){  //if player isn't the first element of the array list
                    model.setCurrentPlayer(model.getPlayers().get(model.getPlayers().indexOf(player)-1)); //currentPlayer is the previous of player
                }
                else{
                    model.setLap(0); //end of second turn
                    nextRound();
                }

            }
        }
    }

    //increase round, change players position, put remaining dices from the draft poll to the round track extract new dices from diceBag and show all
    public void nextRound(){
        model.increaseRound();
        model.getPlayers().add(model.getPlayersNumber()-1 , model.getPlayers().remove(0)); //remove the first player, shift by one the other elements of players and then add the first player at the end of the array list
        model.setCurrentPlayer(model.getPlayers().get(0));
        model.fromDraftPoolToRoundTrack();
        model.extractAndRoll();
        view.showAll(model);
    }

    //compute all scores of the players
    public void computeAllScores(){
        for ( Player player: model.getPlayers()){
            player.computeMyScore(model.getPublicObjectiveCards());
        }

    }

    //
    public void skipTurn(){
        nextPlayer();
    }


    //recupera la stringa passata arg e
    @Override
    public void update(Observable o, Object arg) {
        String userInput = arg.toString();
        if(!isGameSetupEnded){
            setWindowPatternPlayer(userInput);
            isGameSetupEnded = true;
        }
        else{
            performAction(userInput);
        }


    }









}
