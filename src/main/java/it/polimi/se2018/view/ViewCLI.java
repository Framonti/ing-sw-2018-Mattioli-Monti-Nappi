package it.polimi.se2018.view;

import it.polimi.se2018.events.mvevent.*;
import it.polimi.se2018.events.vcevent.*;
import it.polimi.se2018.model.*;

import java.util.*;

/**
 * This class prints in the command line anything that can be visualized by the players.
 * @author fabio
 */

public class ViewCLI extends Observable implements Observer, ViewCLIInterface{

    private Map<Integer, Runnable> vcEvents = new HashMap<>();
    private Map<Integer, Runnable> mvEvents = new HashMap<>();
    private String eventParameters;
    private MVEvent mvEvent;
    private VCEvent vcEvent;

    /**
     * Constructor of this class
     */
    public ViewCLI() {

        //vcEvents initialization (toolCard id order)
        createVCMap();

        //mvEvents initialization (alphabetic order)
        createMVMap();
    }

    /**
     * This method asks the player's name
     * @return The player's name
     */
    public String askName() {
        System.out.println("Inserisci username");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();

    }

    /**
     * Initializes the map between the event's id and the relative method
     */
    private void createMVMap() {
        mvEvents.put(-1, ()-> showWindowPatterns(mvEvent));
        mvEvents.put(1, ()-> showAllDicePatterns(mvEvent));
        mvEvents.put(2, ()-> showDraftPool(mvEvent));
        mvEvents.put(3, ()-> showToolCards(mvEvent));
        mvEvents.put(4, ()-> showRoundTrack(mvEvent));
        mvEvents.put(5, ()-> showScoreTrack(mvEvent));
        mvEvents.put(6, ()-> showActionMenu(mvEvent));
        mvEvents.put(7, ()-> showAll(mvEvent));
        mvEvents.put(8, ()-> showFavorTokens(mvEvent));
        mvEvents.put(9, ()-> showError(mvEvent));
        mvEvents.put(10, ()-> printWinner(mvEvent));
        mvEvents.put(11, this::getInput);
    }

    /**
     * Initializes the map between the event's id and the event itself
     */
    private void createVCMap() {
        vcEvents.put(-1,()-> vcEvent = new WindowPatternChoiceEvent(eventParameters));
        vcEvents.put(1, ()-> vcEvent = new GrozingPliersEvent(eventParameters));
        vcEvents.put(2, ()-> vcEvent = new EglomiseBrushEvent(eventParameters));
        vcEvents.put(3, ()-> vcEvent = new CopperFoilBurnisherEvent(eventParameters));
        vcEvents.put(4, ()-> vcEvent = new LathekinEvent(eventParameters));
        vcEvents.put(5, ()-> vcEvent = new LensCutterEvent(eventParameters));
        vcEvents.put(6, ()-> vcEvent = new FluxBrushEvent(eventParameters));
        vcEvents.put(7, ()-> vcEvent = new GlazingHammerEvent());
        vcEvents.put(8, ()-> vcEvent = new RunnerPliersEvent(eventParameters));
        vcEvents.put(9, ()-> vcEvent = new CorkBakedStraightedgeEvent(eventParameters));
        vcEvents.put(10, ()-> vcEvent = new GrindingStoneEvent(eventParameters));
        vcEvents.put(11, ()-> vcEvent = new FluxRemoverEvent(eventParameters));
        vcEvents.put(12, ()-> vcEvent = new TapWheelEvent(eventParameters));
    }

    @Override
    public void update(Observable model, Object event) {
        mvEvent = (MVEvent) event;
        mvEvents.get(mvEvent.getId()).run();
    }

    @Override
    public void showActionMenu(MVEvent event) {
        ActionMenuEvent actionMenuEvent = (ActionMenuEvent) event;
        String menu = "\n" + (actionMenuEvent.isDiceMoved() ? "X)" : "A)") +
                "\tPosiziona un dado della riserva nello schema\n" +
                (actionMenuEvent.isToolCardUsed() ? "X)" : "B)") +
                "\tPassa il turno\nCARTE UTENSILI\n";
        for(String toolCard: actionMenuEvent.getToolCards())
            menu = menu.concat(toolCard);

        System.out.println(menu);
    }

    /**
     * It's a support method of getInput. Receives a string and creates the correct event
     * @param input It's the string with all the event's information
     * @return The correct event
     */
    private VCEvent createEvent(String input) {
        int event;
        input = input.toLowerCase();

        try {
          //  if(Integer.parseInt(input)>)
            eventParameters = input.substring(2);
            if(input.charAt(0) == 'a')
                return new PlaceDiceEvent(eventParameters);
            else if(input.charAt(0) == 'b')
                return new SkipTurnEvent();
            else {
                event = Integer.parseInt(input);
                vcEvents.get(event).run();
                return vcEvent;
            }
        }
        catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Parametri insufficienti");
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }
    }

    @Override
    public void getInput() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        try {
            VCEvent event = createEvent(input);
            setChanged();
            notifyObservers(event);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            getInput();
        }

    }

    /**
     * Shows all the tool cards in the game
     * @param event It's the MVEvent received
     */
    private void showToolCards(MVEvent event) {
        ToolCardEvent toolCardEvent = (ToolCardEvent) event;
        System.out.println("CARTE UTENSILI");
        for(String toolCard: toolCardEvent.getToolCards())
            System.out.println(toolCard);
    }

    /**
     * Overloading of showToolCards, this is only used in showAll method
     * @param toolCards It's the list of strings representing the toolCards in game
     */
    private void showToolCards(List<String> toolCards) {
        System.out.println("CARTE UTENSILI");
        for(String toolCard: toolCards)
            System.out.println(toolCard);
    }

    /**
     * Shows all the public objective cards in the game
     * @param publicObjectiveCards It's the list of all the public objective cards in the game
     */
    private void showPublicObjectiveCards(List<String> publicObjectiveCards) {
        System.out.println("CARTE OBIETTIVO PUBBLICO");
        for(String publicObjectiveCard: publicObjectiveCards) {
            System.out.println(publicObjectiveCard);
        }
    }

    /**
     * Shows the private objective card of the player itself
     * @param privateObjectiveCard It's the player's private objective card
     */
    private void showPrivateObjectiveCard(String privateObjectiveCard) {
        System.out.println("CARTA OBIETTIVO PRIVATO");
        System.out.println(privateObjectiveCard);
    }

    /**
     * Shows all the dices in the draft pool
     * @param event It's the MVEvent received
     */
    private void showDraftPool(MVEvent event) {
        DraftPoolEvent draftPoolEvent = (DraftPoolEvent) event;
        System.out.println("RISERVA");
        System.out.println(draftPoolEvent.getDraftPoolString());
    }

    /**
     * Overloading of showDraftPool, this is only used in showAll method
     * @param draftPool It's the string representing the DraftPool
     */
    private void showDraftPool(String draftPool) {
        System.out.println("RISERVA\n" + draftPool);
    }

    /**
     * Shows the round track and all the dices on it
     * @param event It's the event received
     */
    private void showRoundTrack(MVEvent event) {
        RoundTrackEvent roundTrackEvent = (RoundTrackEvent) event;
        System.out.println("TRACCIATO DEI ROUND");
        System.out.println(roundTrackEvent.getRoundTrackString());
    }

    /**
     * Overloading of showRoundTrack, this is only used in showAll method
     * @param roundTrack It's the string representing the RoundTrack
     */
    private void showRoundTrack(String roundTrack) {
        System.out.println("TRACCIATO DEI ROUND");
        System.out.println(roundTrack);
    }

    /**
     * Shows the score of each player at the end of the game
     * @param event It's the MVEvent received
     */
    private void showScoreTrack(MVEvent event) {
        ScoreTrackEvent scoreTrackEvent = (ScoreTrackEvent) event;
        System.out.println("TRACCIATO DEI PUNTI");
        System.out.println(scoreTrackEvent.getScoreTrackString());
    }

    /**
     * Shows the window pattern of the player with the dices on it
     * @param event It's the MVEvent received
     * @deprecated
     */
    private void showDicePattern(MVEvent event) {
        DicePatternEvent dicePatternEvent = (DicePatternEvent) event;
        System.out.println("CARTA SCHEMA DI " + dicePatternEvent.getPlayerNames().get(0));
        System.out.println(dicePatternEvent.getDicePatternsString().get(0));
    }

    /**
     * Shows the window patterns and the dices on them of the opponents
     * @param model It's the whole model
     * @deprecated
     */
    private void showOthersDicePatterns(GameSingleton model) {
        for(Player player: model.getPlayers()) {
            if(!player.equals(model.getCurrentPlayer())) {
                System.out.println("CARTA SCHEMA DI " + player.getName());
                System.out.println(player.getDicePattern().toString());
            }
        }
    }

    /**
     * Shows all the window pattern and the dices on them of all players
     * @param event It's the MVEvent received
     */
    private void showAllDicePatterns(MVEvent event) {
        DicePatternEvent dicePatternEvent = (DicePatternEvent) event;
        int playerIndex;
        for(playerIndex = 0; playerIndex < dicePatternEvent.getPlayerNames().size(); playerIndex++) {
            System.out.println("CARTA SCHEMA DI " + dicePatternEvent.getPlayerNames().get(playerIndex));
            System.out.println(dicePatternEvent.getDicePatternsString().get(playerIndex));
        }
    }

    /**
     * Overloading of showAllDicePatterns, this is only used in showAll method
     * @param dicePatterns It's the list of strings representing the dicePatterns
     * @deprecated
     */
    private void showAllDicePatterns(List<String> dicePatterns) {
        for(String dicePattern: dicePatterns)
            System.out.println(dicePattern);
    }

    @Override
    public void showWindowPatterns(MVEvent event) {
        WindowPatternsEvent windowPatternsEvent = (WindowPatternsEvent) event;
        for (String windowPattern : windowPatternsEvent.getWindowPatterns())
            System.out.println(windowPattern);
        selectWindowPattern();
    }

    /**
     * This method asks the player which windowPattern he wants
     */
    private void selectWindowPattern() {
        try {
            System.out.println("SELEZIONA UNA CARTA SCHEMA");
            Scanner scanner = new Scanner(System.in);
            VCEvent vcEvent = new WindowPatternChoiceEvent(scanner.next());
            setChanged();
            notifyObservers(vcEvent);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            selectWindowPattern();
        }
    }

    //farsi mandare anche la stringa del giocatore corrente
    /**
     * Shows who is the winner of the the match
     * @param winner It's the player who has won
     */
    private void printWinner(MVEvent event) {
        WinnerEvent winnerEvent = (WinnerEvent) event;
        System.out.println("Il vincitore è " + winnerEvent.getWinner());
    }

    @Override
    public void showError(MVEvent event) {
        ErrorEvent errorEvent = (ErrorEvent) event;
        System.out.println(errorEvent.getMessageToDisplay());
    }

    /**
     * Shows the name of the player and how many favor tokens it has
     * @param event It's the MVEvent received
     */
    private void showFavorTokens(MVEvent event) {
        FavorTokensEvent favorTokensEvent = (FavorTokensEvent) event;
        System.out.println(favorTokensEvent.getPlayerAndFavorTokens());
    }

    @Override
    public void showAll(MVEvent event) {
        ShowAllEvent showAllEvent = (ShowAllEvent) event;
        showRoundTrack(showAllEvent.getRoundTrackString());
        showAllDicePatterns(showAllEvent.getDicePatternsString());
        showDraftPool(showAllEvent.getDraftPoolString());
        showPublicObjectiveCards(showAllEvent.getPublicObjectivCardsString());
        showPrivateObjectiveCard(showAllEvent.getPrivateObjectiveCardString());
        showToolCards(showAllEvent.getToolCardsString());
    }

    /**
     * Prints the parameter
     * @param message The string that will be printed
     */
    public void printMessage(String message) {
        System.out.println(message);
    }
}
