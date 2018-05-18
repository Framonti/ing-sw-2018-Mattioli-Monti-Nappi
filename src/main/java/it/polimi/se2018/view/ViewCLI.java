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

    private List<ToolCard> toolCards;
    private Map<Integer, Event> vcEvents = new HashMap<>();
    private Map<Integer, Runnable> mvEvents = new HashMap<>();
    private String eventParameters;
    private MVEvent mvEvent;

    /**
     * Constructor of this class
     * @param toolCards The list of the toolCards in game
     */
    public ViewCLI(List<ToolCard>toolCards) {
        this.toolCards = toolCards;

        //vcEvents initialization (toolCard id order)
        createVCMap();

        //mvEvents initialization (alphabetic order)
        createMVMap();
    }

    /**
     * Initializes the map between the event's id and the relative method
     */
    private void createMVMap() {
        mvEvents.put(1, this::showAllDicePatterns);
        mvEvents.put(2, this::showDraftPool);
        mvEvents.put(3, this::showToolCards);
        mvEvents.put(4, this::showRoundTrack);
        mvEvents.put(5, this::showScoreTrack);
        mvEvents.put(6, this::showActionMenu);
        mvEvents.put(7, this::showAll);
        mvEvents.put(8, this::showFavorTokens);
        mvEvents.put(9, this::showError);
    }

    /**
     * Initializes the map between the event's id and the event itself
     */
    private void createVCMap() {
        vcEvents.put(-1, new WindowPatternChoiceEvent(eventParameters));
        vcEvents.put(1, new GrozingPliersEvent(eventParameters));
        vcEvents.put(2, new EglomiseBrushEvent(eventParameters));
        vcEvents.put(3, new CopperFoilBurnisherEvent(eventParameters));
        vcEvents.put(4, new LathekinEvent(eventParameters));
        vcEvents.put(5, new LensCutterEvent(eventParameters));
        vcEvents.put(6, new FluxBrushEvent(eventParameters));
        vcEvents.put(7, new GlazingHammerEvent());
        vcEvents.put(8, new RunnerPliersEvent(eventParameters));
        vcEvents.put(9, new CorkBakedStraightedgeEvent(eventParameters));
        vcEvents.put(10, new GrindingStoneEvent(eventParameters));
        vcEvents.put(11, new FluxRemoverEvent(eventParameters));
        vcEvents.put(12, new TapWheelEvent(eventParameters));
    }

    /**
     * Sets the 'mvEvent' attribute casting 'event' parameter
     * @param model It's the class observed by ViewCLI
     * @param event It's the MVEvent received
     */
    @Override
    public void update(Observable model, Object event) {
        mvEvent = (MVEvent) event;
        mvEvents.get(mvEvent.getId()).run();
    }

    /**
     * Shows all the actions that the player can do
     */
    public void showActionMenu() {
        ActionMenuEvent actionMenuEvent = (ActionMenuEvent) mvEvent;
        String menu = (actionMenuEvent.isDiceMoved() ? "X" : "A") +
                "\tPosiziona un dado della riserva nello schema\n" +
                (actionMenuEvent.isToolCardUsed() ? "X" : "B") +
                "\tPassa il turno\n";
        for(ToolCard toolCard: toolCards)
            menu = menu.concat(toolCard.getId() + "\t" + toolCard.getName() + "\n");

        System.out.println(menu);
    }

    /**
     * It's a support method of getInput. Receives a string and creates the correct event
     * @param input It's the string with all the event's information
     * @return The correct event
     */
    private Event createEvent(String input) {
        int event;
        input = input.toLowerCase();

        try {
            eventParameters = input.substring(2);
            if(input.charAt(0) == 'a')
                return new PlaceDiceEvent(eventParameters);
            else if(input.charAt(0) == 'b')
                return new SkipTurnEvent();
            else
                event = Integer.parseInt(input);
            return vcEvents.get(event);
        }
        catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Parametri insufficienti");
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }
    }

    /**
     * Opens the standard input, creates an event and notify it to its observer
     */
    public void getInput() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        try {
            Event event = createEvent(input);
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
     */
    public void showToolCards() {
        System.out.println("CARTE UTENSILI");
        ToolCardEvent toolCardEvent = (ToolCardEvent) mvEvent;
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
     */
    public void showDraftPool() {
        DraftPoolEvent draftPoolEvent = (DraftPoolEvent) mvEvent;
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
     */
    public void showRoundTrack() {
        RoundTrackEvent roundTrackEvent = (RoundTrackEvent) mvEvent;
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
     */
    public void showScoreTrack() {
        ScoreTrackEvent scoreTrackEvent = (ScoreTrackEvent) mvEvent;
        System.out.println("TRACCIATO DEI PUNTI");
        System.out.println(scoreTrackEvent.getScoreTrackString());
    }

    /**
     * Shows the window pattern of the player with the dices on it
     * @param currentPlayer It's the player itself
     */
    private void showMyDicePattern(Player currentPlayer) {
        System.out.println("CARTA SCHEMA DI " + currentPlayer.getName());
        System.out.println(currentPlayer.getDicePattern().toString());
    }

    /**
     * Shows the window patterns and the dices on them of the opponents
     * @param model It's the whole model
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
     */
    public void showAllDicePatterns() {
        DicePatternEvent dicePatternEvent = (DicePatternEvent) mvEvent;
        /*showMyDicePattern(dicePatternEvent.);
        showOthersDicePatterns(dicePatternEvent.);*/
    }

    /**
     * Overloading of showAllDicePatterns, this is only used in showAll method
     * @param dicePatterns It's the list of strings representing the dicePatterns
     */
    private void showAllDicePatterns(List<String> dicePatterns) {
        for(String dicePattern: dicePatterns)
            System.out.println(dicePattern);
    }

    /**
     * Shows the 4 window patterns among which the player has to choose one (at the start of the game)
     * @param windowPatterns It's the list of 4 window patterns
     */
    public void showWindowPatterns(List<WindowPattern> windowPatterns) {
        System.out.println("SELEZIONA UNA CARTA SCHEMA");
        for(WindowPattern windowPattern: windowPatterns)
            System.out.println(windowPattern.toString());
    }

    /**
     * Shows who is the winner of the the match
     * @param winner It's the player who has won
     */
    public void printWinner(Player winner) {
        System.out.println("Il vincitore è " + winner.getName());
    }

    /**
     * Prints the error message if the action selected by the player is not valid
     */
    public void showError() {
        ErrorEvent errorEvent = (ErrorEvent) mvEvent;
        System.out.println(errorEvent.getMessageToDisplay());
    }

    /**
     * Shows the name of the player and how many favor tokens it has
     */
    public void showFavorTokens() {
        FavorTokensEvent favorTokensEvent = (FavorTokensEvent) mvEvent;
        System.out.println(favorTokensEvent.getPlayerAndFavorTokens);
    }

    /**
     * Shows everything that it's visible to the player
     */
    public void showAll() {
        ShowAllEvent showAllEvent = (ShowAllEvent) mvEvent;
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
