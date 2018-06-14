package it.polimi.se2018.view;

import it.polimi.se2018.events.ConnectionChoiceEvent;
import it.polimi.se2018.events.ConnectionEstablishedEvent;
import it.polimi.se2018.events.NewObserverEvent;
import it.polimi.se2018.events.mvevent.*;
import it.polimi.se2018.events.vcevent.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * This class prints in the command line anything that can be visualized by the players.
 * @author fabio
 */

public class ViewCLI extends Observable implements Observer, ViewCLIInterface{

    private Map<Integer, Runnable> vcEvents = new HashMap<>();
    private Map<Integer, Runnable> mvEvents = new HashMap<>();
    private String eventParameters;
    private boolean toolCardUsed;
    private boolean diceMoved;
    private MVEvent mvEvent;
    private VCEvent vcEvent;
    private Scanner scanner;
    private BufferedReader reader;
    private boolean firstTimeNick;
    private static final String INVALID_MOVE= "MOSSA NON VALIDA";
    private GetInputClass getInputClass;


    /**
     * Constructor of this class
     */
    public ViewCLI() {

        //vcEvents initialization (toolCard id order)
        createVCMap();

        //mvEvents initialization (alphabetic order)
        createMVMap();

        scanner = new Scanner(System.in);
        reader = new BufferedReader(new InputStreamReader(System.in));

    }

    public void askConnection(){

        System.out.println("Inserisci l'indirizzo IP del server");
        String ipAddress = scanner.nextLine();

        String choice = "";
        int choiceInt;
        while(!choice.equals("1") && !choice.equals("2")) {
            System.out.println("Scegli il tipo di connessione che preferisci\n1)\tRMI\n2)\tSocket");
            choice = scanner.nextLine();
        }
        choiceInt = Integer.parseInt(choice);
        ConnectionChoiceEvent connectionChoiceEvent = new ConnectionChoiceEvent(choiceInt, ipAddress);
        setChanged();
        notifyObservers(connectionChoiceEvent);
    }


    /**
     * This method asks the player's name
     */
    private void askName() {
        System.out.println("Inserisci il tuo nickname:");
        String name = scanner.nextLine();
        if(name.length() < 2) {
            System.out.println("Il nickname deve essere lungo almeno 2 caratteri!\n");
            askName();
        } else if (name.length() > 20) {
            System.out.println("Il nickname può essere lungo al più 20 caratteri!\n");
            askName();
        } else {
            NicknameEvent nicknameEvent = new NicknameEvent(name, firstTimeNick);
            setChanged();
            notifyObservers(nicknameEvent);
        }
    }

    private void showClientsConnected(MVEvent mvEvent){
        ClientAlreadyConnectedEvent clientAlreadyConnectedEvent = (ClientAlreadyConnectedEvent) mvEvent;
        System.out.println("Client attualmente connessi: ");
        for(String clients : clientAlreadyConnectedEvent.getClientConnected())
            System.out.println(clients);
        System.out.println();
    }

    /**
     * It's a support method of getInput. Receives a string and creates the correct event
     * @param input It's the string with all the event's information
     * @return The correct event
     */
    private VCEvent createEvent(String input) {
        int event;
        eventParameters = "";
        String[] param = input.toLowerCase().split("\\s+");

        try {
            if(param[0].equals("b"))
                return new SkipTurnEvent();

            if(param[0].equals("7")) {
                if(!toolCardUsed)
                    return new GlazingHammerEvent();
                else
                    throw new UnsupportedOperationException(INVALID_MOVE);
            }

            int index;
            for(index = 1; index < param.length; index++) {
                eventParameters = eventParameters.concat(param[index] + " ");
            }

            if(param[0].equals("a")) {
                if(!diceMoved)
                    return new PlaceDiceEvent(eventParameters);
                else
                    throw new UnsupportedOperationException(INVALID_MOVE);
            }
            if(!toolCardUsed) {
                event = Integer.parseInt(param[0]);
                vcEvents.get(event).run();
                return vcEvent;
            }
            else {
                throw new UnsupportedOperationException(INVALID_MOVE);
            }
        }
        catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Parametri insufficienti!");
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parametri non numerici o sbagliati!");
        }
    }

    /**
     * Initializes the map between the event's id and the relative method
     */
    private void createMVMap() {
        mvEvents.put(-1, ()-> showWindowPatterns(mvEvent));
        mvEvents.put(1, ()-> showDicePattern(mvEvent));
        mvEvents.put(2, ()-> showDraftPool(mvEvent));
        mvEvents.put(3, ()-> showToolCards(mvEvent));
        mvEvents.put(4, ()-> showRoundTrack(mvEvent));
        mvEvents.put(5, ()-> showScoreTrack(mvEvent));
        mvEvents.put(6, ()-> showActionMenu(mvEvent));
        mvEvents.put(7, ()-> showAll(mvEvent));
        mvEvents.put(8, ()-> showFavorTokens(mvEvent));
        mvEvents.put(9, ()-> showError(mvEvent));
        mvEvents.put(10, ()-> printWinner(mvEvent));
        mvEvents.put(11, this::getInputStarter);
        mvEvents.put(12, ()-> showEndTurn(mvEvent));
        mvEvents.put(13, ()-> fluxBrushChoice(mvEvent));
        mvEvents.put(14, ()-> fluxRemoverChoice(mvEvent));
        mvEvents.put(15, ()->{});
        mvEvents.put(70, () ->showClientsConnected(mvEvent));
    }

    /**
     * Initializes the map between the event's id and the event itself
     */
    private void createVCMap() {
        vcEvents.put(-1,()-> vcEvent = new WindowPatternChoiceEvent(eventParameters));      //può non stare nella mappa
        vcEvents.put(1, ()-> vcEvent = new GrozingPliersEvent(eventParameters));
        vcEvents.put(2, ()-> vcEvent = new EglomiseBrushEvent(eventParameters));
        vcEvents.put(3, ()-> vcEvent = new CopperFoilBurnisherEvent(eventParameters));
        vcEvents.put(4, ()-> vcEvent = new LathekinEvent(eventParameters));
        vcEvents.put(5, ()-> vcEvent = new LensCutterEvent(eventParameters));
        vcEvents.put(6, ()-> vcEvent = new FluxBrushChooseDiceEvent(eventParameters));
        vcEvents.put(7, ()-> vcEvent = new GlazingHammerEvent());                           //può non stare nella mappa
        vcEvents.put(8, ()-> vcEvent = new RunnerPliersEvent(eventParameters));
        vcEvents.put(9, ()-> vcEvent = new CorkBakedStraightedgeEvent(eventParameters));
        vcEvents.put(10, ()-> vcEvent = new GrindingStoneEvent(eventParameters));
        vcEvents.put(11, ()-> vcEvent = new FluxRemoverChooseDiceEvent(eventParameters));
        vcEvents.put(12, ()-> vcEvent = new TapWheelEvent(eventParameters));
        vcEvents.put(13, ()-> vcEvent = new FluxBrushPlaceDiceEvent(eventParameters));      //può non stare nella mappa
        vcEvents.put(14, ()-> vcEvent = new FluxRemoverPlaceDiceEvent(eventParameters));    //può non stare nella mappa
    }

    @Override
    public void fluxBrushChoice(MVEvent event) {
        FluxBrushChoiceEvent fluxBrushChoiceEvent = (FluxBrushChoiceEvent) event;
        System.out.println(fluxBrushChoiceEvent.getDice() + "\nDove vuoi piazzare il dado?");
        try {
            String choice = scanner.nextLine();
            setChanged();
            notifyObservers(new FluxBrushPlaceDiceEvent(choice));
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            fluxBrushChoice(event);
        }
        catch (NoSuchElementException e) {
            System.out.println("noSuchElementException");
            fluxBrushChoice(event);
        }
    }

    @Override
    public void fluxRemoverChoice(MVEvent event) {
        FluxRemoverChoiceEvent fluxRemoverChoiceEvent = (FluxRemoverChoiceEvent) event;
        System.out.println(fluxRemoverChoiceEvent.getDice() + "\nScegli il valore del dado e piazzalo.");
        try {
            String choice = scanner.nextLine();
            setChanged();
            notifyObservers(new FluxRemoverPlaceDiceEvent(choice));
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            fluxRemoverChoice(event);
        }
    }

    public class GetInputClass extends Thread {

        @Override
        public void run() {
            try {
                while (!reader.ready())
                    Thread.sleep(200);
                getInput();
            } catch (IOException e) {
                System.out.println("IOException thrown!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void getInput() {
        try {
            String input = reader.readLine();
            VCEvent event = createEvent(input);
            setChanged();
            notifyObservers(event);
        }
        catch (IllegalArgumentException | UnsupportedOperationException e) {
            System.out.println(e.getMessage());
            getInput();
        }
        catch (IOException e) {
            System.out.println("IOException thrown!");
        }
    }

    private void getInputStarter() {
        getInputClass = new GetInputClass();
        getInputClass.start();
    }

    /**
     * Prints the parameter
     * @param message The string that will be printed
     */
    public void printMessage(String message) {
        System.out.println(message);
    }

    //farsi mandare anche la stringa del giocatore corrente
    /**
     * Shows who is the winner of the the match
     * @param event It's the MVEvent received
     */
    private void printWinner(MVEvent event) {
        WinnerEvent winnerEvent = (WinnerEvent) event;
        System.out.println("Il vincitore è " + winnerEvent.getWinner());
    }

    /**
     * This method asks the player which windowPattern he wants
     */
    private void selectWindowPattern() {
        try {
            System.out.println("Seleziona una carta schema.");
            VCEvent vcEvent = new WindowPatternChoiceEvent(scanner.nextLine());
            setChanged();
            notifyObservers(vcEvent);
            System.out.println("Scelta eseguita. Attendi...\n");
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            selectWindowPattern();
        }
    }

    @Override
    public void showActionMenu(MVEvent event) {
        ActionMenuEvent actionMenuEvent = (ActionMenuEvent) event;
        diceMoved = actionMenuEvent.isDiceMoved();
        toolCardUsed = actionMenuEvent.isToolCardUsed();

        String menu = "\n" + (diceMoved ? "" : "A)\tPosiziona un dado della riserva nello schema\n") +
                "B)\tPassa il turno\n";
        if(!toolCardUsed) {
            for (String toolCard : actionMenuEvent.getToolCards())
                menu = menu.concat(toolCard);
        }
        menu = menu.concat("\nFai una mossa:");
        System.out.println(menu);
    }

    @Override
    public void showAll(MVEvent event) {
        ShowAllEvent showAllEvent = (ShowAllEvent) event;
        showRoundTrack(showAllEvent.getRoundTrack());
        showDicePattern(showAllEvent.getDicePatterns());
        showDraftPool(showAllEvent.getDraftPool());
        showPublicObjectiveCards(showAllEvent.getPublicObjectiveCardsString());
        showPrivateObjectiveCard(showAllEvent.getPrivateObjectiveCardString());
        showToolCards(showAllEvent.getToolCards());
    }

    /**
     * Shows all the window pattern and the dices on them of all players
     * @param event It's the MVEvent received
     */
    private void showAllDicePatterns(MVEvent event) {
        DicePatternEvent dicePatternEvent = (DicePatternEvent) event;
        int playerIndex;
        for(playerIndex = 0; playerIndex < dicePatternEvent.getPlayerNames().size(); playerIndex++) {
            System.out.println("Carta schema di " + dicePatternEvent.getPlayerNames().get(playerIndex));
            System.out.println(dicePatternEvent.getDicePatternsString().get(playerIndex));
        }
    }

    /**
     * Shows the window pattern of the players with the dices on it
     * @param event It's the MVEvent received
     */
    private void showDicePattern(MVEvent event) {
        DicePatternEvent dicePatternEvent = (DicePatternEvent) event;
        int playerIndex;
        for(playerIndex = 0; playerIndex < dicePatternEvent.getDicePatternsString().size(); playerIndex++) {
            System.out.println("Carta schema di " + dicePatternEvent.getPlayerNames().get(playerIndex));
            System.out.println(dicePatternEvent.getDicePatternsString().get(playerIndex));
        }
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

    @Override
    public void showEndTurn(MVEvent event) {
        System.out.println("TURNO TERMINATO. ATTENDI.\n");
    }

    @Override
    public void showError(MVEvent event) {
        ErrorEvent errorEvent = (ErrorEvent) event;
        System.out.println(errorEvent.getMessageToDisplay());
        if (errorEvent.getMessageToDisplay().equals("\nTutti i giocatori hanno abbandonato la partita.\nHAI VINTO!"))
            getInputClass.interrupt();
    }

    /**
     * Shows the name of the player and how many favor tokens it has
     * @param event It's the MVEvent received
     */
    private void showFavorTokens(MVEvent event) {
        FavorTokensEvent favorTokensEvent = (FavorTokensEvent) event;
        System.out.println(favorTokensEvent.getPlayerAndFavorTokens());
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
     * Shows the round track and all the dices on it
     * @param event It's the event received
     */
    private void showRoundTrack(MVEvent event) {
        RoundTrackEvent roundTrackEvent = (RoundTrackEvent) event;
        System.out.println("TRACCIATO DEI ROUND");
        System.out.println(roundTrackEvent.getRoundTrackString());
    }

    /**
     * Shows the score of each player at the end of the game
     * @param event It's the MVEvent received
     */
    private void showScoreTrack(MVEvent event) {
        ScoreTrackEvent scoreTrackEvent = (ScoreTrackEvent) event;
        System.out.println("TRACCIATO DEI PUNTI");
        int i;
        for(i = 0; i < scoreTrackEvent.getPlayersNames().size(); i++)
            System.out.println(scoreTrackEvent.getPlayersNames().get(i) + ": " + scoreTrackEvent.getScores().get(i));
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

    @Override
    public void showWindowPatterns(MVEvent event) {
        WindowPatternsEvent windowPatternsEvent = (WindowPatternsEvent) event;
        for (String windowPattern : windowPatternsEvent.getWindowPatterns())
            System.out.println(windowPattern);
        showPrivateObjectiveCard(windowPatternsEvent.getPrivateObjectiveCard());
        selectWindowPattern();
    }

    @Override
    public void update(Observable model, Object event) {
        if(event.getClass() == ConnectionEstablishedEvent.class){
            firstTimeNick = ((ConnectionEstablishedEvent) event).isFirstTimeNickname();
            askName();
        }
        else if(event.getClass() == NickNameAcceptedEvent.class)
            System.out.println("Nickname valido.\nATTENDI.\n");
        else if(event.getClass() != NewObserverEvent.class) {
            mvEvent = (MVEvent) event;
            mvEvents.get(mvEvent.getId()).run();
        }
    }

}
