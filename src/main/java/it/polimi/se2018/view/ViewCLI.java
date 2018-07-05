package it.polimi.se2018.view;

import it.polimi.se2018.events.networkevent.*;
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
    private Map<Integer, Runnable> networkEvents = new HashMap<>();
    private String eventParameters;
    private boolean toolCardUsed;
    private boolean diceMoved;
    private MVEvent mvEvent;
    private VCEvent vcEvent;
    private NetworkEvent networkEvent;
    private BufferedReader reader;
    private boolean firstTimeNick;
    private boolean singlePlayer = false;
    private static final String INVALID_MOVE = "MOSSA NON VALIDA";
    private static final String IOE_THROWN = "IOException thrown!";
    private GetInputClass getInputClass = new GetInputClass();
    private SuspendedPlayer suspendedPlayer = new SuspendedPlayer();
    private AskSinglePlayer askSinglePlayer;
    private FluxChoice fluxChoice;
    private SelectWindowPattern selectWindowPattern;


    /**
     * Constructor of this class
     */
    public ViewCLI() {

        //vcEvents initialization (toolCard id order)
        createVCMap();

        //mvEvents initialization (alphabetic order)
        createMVMap();

        createNetworkEventMap();

        reader = new BufferedReader(new InputStreamReader(System.in));

    }

    /**
     * Asks the IP address of the server and the type of connection preferred
     */
    public void askConnection(){
        try {
            System.out.println("Inserisci l'indirizzo IP del server");
            String ipAddress = reader.readLine();

            String choice = "";
            int choiceInt;
            while (!choice.equals("1") && !choice.equals("2")) {
                System.out.println("Scegli il tipo di connessione che preferisci\n1)\tRMI\n2)\tSocket");
                choice = reader.readLine();
            }
            choiceInt = Integer.parseInt(choice);
            ConnectionChoiceEvent connectionChoiceEvent = new ConnectionChoiceEvent(choiceInt, ipAddress);
            setChanged();
            notifyObservers(connectionChoiceEvent);
        } catch (IOException e) {
            System.out.println(IOE_THROWN);
            askConnection();
        }
    }


    /**
     * This method asks the player's name
     */
    private void askName() {
        try {
            System.out.println("Inserisci il tuo nickname:");
            String name = reader.readLine();
            if (name.length() < 2) {
                System.out.println("Il nickname deve essere lungo almeno 2 caratteri!\n");
                askName();
            } else if (name.length() > 20) {
                System.out.println("Il nickname può essere lungo al più 20 caratteri!\n");
                askName();
            } else {
                name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                NicknameEvent nicknameEvent = new NicknameEvent(name, firstTimeNick);
                setChanged();
                notifyObservers(nicknameEvent);
            }
        } catch (IOException e) {
            System.out.println(IOE_THROWN);
            askName();
        }
    }

    /**
     * Shows all the players connected to the server
     * @param mvEvent It's the event containing the names of all the connected players
     */
    private void showClientsConnected(MVEvent mvEvent){
        ClientAlreadyConnectedEvent clientAlreadyConnectedEvent = (ClientAlreadyConnectedEvent) mvEvent;
        System.out.println("Client attualmente connessi: ");
        for(String clients : clientAlreadyConnectedEvent.getClientConnected())
            System.out.println(clients);
        System.out.println();
        if (clientAlreadyConnectedEvent.getClientConnected().size() == 1) {
            System.out.println("Sei l'unico client.\nSe vuoi giocare in solitaria scrivi la difficoltà (da 1 a 5).");
            askSinglePlayer = new AskSinglePlayer();
            askSinglePlayer.start();
        } else if (askSinglePlayer != null && askSinglePlayer.isAlive()) {
            askSinglePlayer.interrupt();
        }

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

            String diceForSinglePlayer = param [1];
            if(param[0].equals("7")) {
                if(!toolCardUsed) {
                    if (singlePlayer) {
                        setChanged();
                        notifyObservers(new DiceChosenSinglePlayer(diceForSinglePlayer));
                    }
                    return new GlazingHammerEvent();
                }
                else
                    throw new UnsupportedOperationException(INVALID_MOVE);
            }

            if (singlePlayer && !param[0].equals("a"))
                System.arraycopy(param, 2, param, 1, param.length - 2);

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
                if (singlePlayer) {
                    setChanged();
                    notifyObservers(new DiceChosenSinglePlayer(diceForSinglePlayer));
                }
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
    private void createNetworkEventMap(){
        networkEvents.put(80, () -> {
            System.out.println("L'indirizzo IP è sbagliato!");
            askConnection();
        });
        networkEvents.put(1, () -> singlePlayer = true);
        networkEvents.put(25, () -> connectionEstablishedHandler(networkEvent));
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
        mvEvents.put(11, this::getInputStarter);
        mvEvents.put(12, ()-> showEndTurn(mvEvent));
        mvEvents.put(13, ()-> fluxBrushChoice(mvEvent));
        mvEvents.put(14, ()-> fluxRemoverChoice(mvEvent));
        mvEvents.put(15, ()-> {});
        mvEvents.put(16, this::playerSuspended);
        mvEvents.put(17, new PrivateObjectiveCardChoice()::start);
        mvEvents.put(70, ()-> showClientsConnected(mvEvent));
        mvEvents.put(30, ()-> System.out.println("Nickname valido.\nATTENDI.\n"));
    }

    /**
     * Initializes the map between the event's id and the event itself
     */
    private void createVCMap() {
//        vcEvents.put(-1,()-> vcEvent = new WindowPatternChoiceEvent(eventParameters));      //può non stare nella mappa
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

    public void fluxBrushChoice(MVEvent event) {
        FluxBrushChoiceEvent fluxBrushChoiceEvent = (FluxBrushChoiceEvent) event;
        System.out.println(fluxBrushChoiceEvent.getDice() + "\nDove vuoi piazzare il dado?");
        fluxChoice = new FluxChoice(false);
        fluxChoice.start();
    }

    public void fluxRemoverChoice(MVEvent event) {
        FluxRemoverChoiceEvent fluxRemoverChoiceEvent = (FluxRemoverChoiceEvent) event;
        System.out.println(fluxRemoverChoiceEvent.getDice() + "\nScegli il valore del dado e piazzalo.");
        fluxChoice = new FluxChoice(true);
        fluxChoice.start();
    }

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
            System.out.println(IOE_THROWN);
        }
    }

    /**
     * Creates a new Thread in which will be asked the input
     */
    private void getInputStarter() {
        getInputClass = new GetInputClass();
        getInputClass.start();
    }


    public void playerSuspended() {
        if (fluxChoice != null && fluxChoice.isAlive())
            fluxChoice.interrupt();
        getInputClass.interrupt();
        suspendedPlayer = new SuspendedPlayer();
        suspendedPlayer.start();
    }


    public void showActionMenu(MVEvent event) {
        ActionMenuEvent actionMenuEvent = (ActionMenuEvent) event;
        diceMoved = actionMenuEvent.isDiceMoved();
        toolCardUsed = actionMenuEvent.isToolCardUsed();

        String menu = "\n" + (diceMoved ? "" : "A)\tPosiziona un dado della riserva nello schema" +
                "\t(Formato mossa: A \"indiceDado\" \"indiceRiga\" \"indiceColonna\"; Es: a 1 2 1)\n")
                +"B)\tPassa il turno\n";
        if(!toolCardUsed) {
            for (String toolCard : actionMenuEvent.getToolCards())
                menu = menu.concat(toolCard);
        }
        menu = menu.concat("\nFai una mossa:");
        System.out.println(menu);
    }


    public void showAll(MVEvent event) {
        ShowAllEvent showAllEvent = (ShowAllEvent) event;
        showRoundTrack(showAllEvent.getRoundTrack());
        showDicePattern(showAllEvent.getDicePatterns());
        showDraftPool(showAllEvent.getDraftPool());
        showPublicObjectiveCards(showAllEvent.getPublicObjectiveCardsString());
        showPrivateObjectiveCards(showAllEvent.getPrivateObjectiveCardsString());
        showToolCards(showAllEvent.getToolCards());
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
        System.out.println();
    }


    public void showEndTurn(MVEvent event) {
        System.out.println("TURNO TERMINATO. ATTENDI.\n");
    }


    public void showError(MVEvent event) {
        ErrorEvent errorEvent = (ErrorEvent) event;
        if (!errorEvent.getMessageToDisplay().equals("OK toolCard 11"))
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

    /**
     * Shows the private objective card of the player itself
     * @param privateObjectiveCards It's the List of player's private objective cards
     */
    private void showPrivateObjectiveCards(List<String> privateObjectiveCards) {
        System.out.println("CART" + (privateObjectiveCards.size() == 1 ? "A" : "E") + " OBIETTIVO PRIVATO");
        for (int i = 0; i < privateObjectiveCards.size(); i++)
            System.out.println( (i+1) + ")\t" + privateObjectiveCards.get(i));
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
        System.out.println("Il vincitore è: " + scoreTrackEvent.getPlayersNames().get(0) + "\n");

        if (fluxChoice != null && fluxChoice.isAlive())
            fluxChoice.interrupt();
        if (suspendedPlayer.isAlive())
            suspendedPlayer.interrupt();
        if (getInputClass.isAlive())
            getInputClass.interrupt();
        if (selectWindowPattern != null && selectWindowPattern.isAlive())
            selectWindowPattern.interrupt();

        new AskNewGame().start();
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


    public void showWindowPatterns(MVEvent event) {
        WindowPatternsEvent windowPatternsEvent = (WindowPatternsEvent) event;
        for (String windowPattern : windowPatternsEvent.getWindowPatterns())
            System.out.println(windowPattern);
        showPrivateObjectiveCards(windowPatternsEvent.getPrivateObjectiveCards());
        new SelectWindowPattern().start();
    }

    /**
     * This method sets the firstTimeNick attribute depending on the NetworkEvent received
     * @param networkEvent It's the event received
     */
    private void connectionEstablishedHandler(NetworkEvent networkEvent){
        firstTimeNick = ((ConnectionEstablishedEvent) networkEvent).isFirstTimeNickname();
        askName();
    }


    public void update(Observable model, Object event) {
        if(event instanceof NetworkEvent){
            networkEvent = (NetworkEvent) event;
            networkEvents.get(networkEvent.getId()).run();
        }
        else if(event.getClass() != NewObserverEvent.class) {
            mvEvent = (MVEvent) event;
            mvEvents.get(mvEvent.getId()).run();
        }
    }

    /**
     * This class waits the player to write its input.
     * It is interrupted when every other player leaves the game or the timer ends
     */
    private class GetInputClass extends Thread {

        @Override
        public void run() {
            try {
                while (!reader.ready())
                    Thread.sleep(200);
                getInput();
            } catch (IOException e) {
                System.out.println(IOE_THROWN);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * This class tells the player that it has been suspended.
     * The player rejoins the game if writes anything
     */
    private class SuspendedPlayer extends Thread {

        @Override
        public void run() {
            System.out.println("\nSEI STATO SOSPESO!\nScrivi qualunque cosa per rientrare in partita.");
            try {
                while (!reader.ready())
                    Thread.sleep(200);
                System.out.println("Sei di nuovo in partita!");
                setChanged();
                notifyObservers(new UnsuspendEvent(null));
            } catch (IOException e) {
                System.out.println(IOE_THROWN);
                run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * This class asks the player which windowPattern he wants
     */
    private class SelectWindowPattern extends Thread {

        @Override
        public void run() {
            try {
                System.out.println("Seleziona una carta schema.");
                while (!reader.ready())
                    Thread.sleep(200);
                VCEvent event = new WindowPatternChoiceEvent(reader.readLine(), null);
                setChanged();
                notifyObservers(event);
                System.out.println("Scelta eseguita. Attendi...\n");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                run();
            } catch (IOException e) {
                System.out.println(IOE_THROWN);
                run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * This class asks the player if he wats to play another game
     */
    private class AskNewGame extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(5000);
                String choice = "";
                boolean firstTime = true;
                while (!choice.equals("1") && !choice.equals("2")) {
                    if (!firstTime)
                        System.out.println("Risposta non valida!");
                    else
                        firstTime = false;
                    System.out.println("Vuoi giocare una nuova partita?\n1)\tSì\n2)\tNo");
                    choice = reader.readLine();
                }
                if (choice.equals("1")) {
                    singlePlayer = false;
                    firstTimeNick = true;
                    askName();
                } else if (choice.equals("2"))
                    System.exit(0);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                System.out.println(IOE_THROWN);
                run();
            }
        }
    }

    /**
     * This class asks if the player wants to play alone.
     * This is possible only if there aren't other players in the waiting room.
     */
    private class AskSinglePlayer extends Thread {

        @Override
        public void run() {
            try {
                int choice = 0;
                boolean firstTime = true;
                while (choice < 1 || choice > 5) {
                    if (!firstTime)
                        System.out.println("Risposta non valida!");
                    else
                        firstTime = false;
                    while (!reader.ready())
                        Thread.sleep(200);
                    choice = Integer.parseInt(reader.readLine());
                }

                setChanged();
                notifyObservers(new SinglePlayerEvent(choice));
            } catch (IOException e) {
                System.out.println(IOE_THROWN);
            } catch (InterruptedException e) {
                System.out.println("Non è più possibile giocare in solitaria!");
                Thread.currentThread().interrupt();
            } catch (IllegalArgumentException e) {
                System.out.println("Parametri non numerici o sbagliati!");
                run();
            }
        }
    }

    /**
     * This class is used to ask the second input for the fluxBrush and the fluxRemover toolCards
     */
    private class FluxChoice extends Thread {

        private boolean isRemover;

        FluxChoice(boolean isRemover) {
            this.isRemover = isRemover;
        }

        @Override
        public void run() {
            try {
                while (!reader.ready())
                    Thread.sleep(200);
                setChanged();
                notifyObservers( (isRemover ? new FluxRemoverPlaceDiceEvent(reader.readLine()) :
                        new FluxBrushPlaceDiceEvent(reader.readLine())) );

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                run();
            } catch (IOException e) {
                System.out.println(IOE_THROWN);
                run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * This class is used in singlePlayerMode to ask which PrivateObjectiveCard the player wants
     */
    private class PrivateObjectiveCardChoice extends Thread {

        @Override
        public void run() {
            AskPrivateObjectiveCard askPrivateObjectiveCard = (AskPrivateObjectiveCard) mvEvent;
            showPrivateObjectiveCards(askPrivateObjectiveCard.getPrivateObjectiveCardsString());
            System.out.println("Scegli una carta obiettivo privato.");
            try {
                setChanged();
                notifyObservers(new PrivateObjectiveCardChosen(reader.readLine()));
            } catch (IOException e) {
                System.out.println(IOE_THROWN);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                run();
            }
        }

    }

}
