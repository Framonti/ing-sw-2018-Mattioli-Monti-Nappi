package it.polimi.se2018.network.server;

import it.polimi.se2018.events.vcevent.SinglePlayerEvent;
import it.polimi.se2018.utilities.ConfigurationParametersLoader;
import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.mvevent.*;
import it.polimi.se2018.events.vcevent.SkipTurnEvent;
import it.polimi.se2018.events.vcevent.VCEvent;
import it.polimi.se2018.model.GameSetupSingleton;
import it.polimi.se2018.model.GameSingleton;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.network.client.ClientInterfaceRMI;
import it.polimi.se2018.network.client.ClientInterfaceSocket;
import it.polimi.se2018.view.VirtualViewCLI;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the implementation of the server
 * @author fabio
 */
public class ServerImplementation extends UnicastRemoteObject implements ServerInterface {

    private static final String CONNECTION_LOST = "Connection lost with ";
    private static final String ABANDONED_GAME = " ha abbandonato la partita.";

    private transient List<ClientInterfaceRMI> rmiClients = new ArrayList<>();
    private transient List<ClientInterfaceSocket> socketClients = new ArrayList<>();
    private transient List<Player> players = new ArrayList<>();

    private transient Controller controller;
    private transient GameSingleton model;
    private final transient Object lock = new Object();
    private transient VirtualViewCLI virtualViewCLI;
    private transient Timer timer = new Timer();
    private boolean gameStarted = false;
    private int setupDuration;
    private int turnDuration;

    /**
     * Constructor of this class. It is package-private.
     * It sets the setupDuration from a configuration file and starts the ClientCollector.
     * @param virtualViewCLI It's the VirtualView attribute of the class
     * @throws RemoteException If it fails to export object
     */
    ServerImplementation(VirtualViewCLI virtualViewCLI) throws RemoteException {
        super(0);
        this.virtualViewCLI = virtualViewCLI;
        virtualViewCLI.setServer(this);
        ConfigurationParametersLoader configurationParametersLoader = new ConfigurationParametersLoader("src/main/java/it/polimi/se2018/xml/ConfigurationParameters.xml");
        setupDuration = configurationParametersLoader.getSetupTimer();
        turnDuration = configurationParametersLoader.getTurnTimer();
        setupDuration = 10000;
        new HeartBeat().start();
    }

    /**
     * This method creates a new game and ask the players to choose their windowPattern
     */
    private void createGame(int difficulty) {

        ConfigurationParametersLoader configurationParametersLoader = new ConfigurationParametersLoader("src/main/java/it/polimi/se2018/xml/ConfigurationParameters.xml");
        int turnDuration = configurationParametersLoader.getTurnTimer();

        GameSetupSingleton.instance();
        GameSetupSingleton.instance().addPlayers(players);
        model = GameSetupSingleton.instance().createNewGame(difficulty);
        controller = new Controller(virtualViewCLI, model.getToolCards(), model, turnDuration);

        virtualViewCLI.addObserver(controller);
        model.addObserver(virtualViewCLI);

        send(new GameStartEvent());

        for(Player player: model.getPlayers()) {
            List<String> windowPatterns = new ArrayList<>();
            List<String> windowPatternsPath = new ArrayList<>();
            int num = 1;
            for (WindowPattern windowPattern: player.getWindowPatterns()) {
                windowPatterns.add("Numero: " + num + "\n" + windowPattern.toString());
                windowPatternsPath.add(windowPattern.toStringPath());
                num++;
            }
            sendTo(new WindowPatternsEvent(windowPatterns, windowPatternsPath, player.getPrivateObjectiveCardsToString(), player.getPrivateObjectiveCardsToStringPath()), player);
        }
        System.out.println("A new game has started...");
    }

    /**
     * This method refreshes the list of players in the waiting room.
     */
    private void waitingRoomRefresh() {
        List<String> names = new ArrayList<>();

        for(Player playerToAdd : players)
            names.add(playerToAdd.getName());
        send(new ClientAlreadyConnectedEvent(names));
    }

    /**
     * This method refreshes the waiting room and checks the conditions to start a new game
     * @param player It's the player that has just been added.
     */
    private void addClientHelper(Player player) {
        sendTo(new NickNameAcceptedEvent(), player);
        waitingRoomRefresh();

        if(players.size() == 2) {
            timer = new Timer();
            timer.start();
        } else if(players.size() == 4) {
            gameStarted = true;
            timer.interrupt();
            createGame(0);
        }
    }

    /**
     * This method adds the clientInterface to the list of the clients connected via socket.
     * If the conditions are respected it launches the game creation
     * @param client It's the clientInterface to be added
     */
    public void addSocketClient(ClientInterfaceSocket client, String name) {
        synchronized (lock) {
            if(rmiClients.size() + socketClients.size() < 4) {
                if(!gameStarted) {
                    for (Player player: players) {
                        if (player.getName().equals(name)) {
                            sendTo(new ErrorEvent("Nickname già in uso!"), client);
                            return;
                        }
                    }
                    Player player = new Player(name);
                    player.setClientInterface(client);
                    players.add(player);
                    socketClients.add(client);

                    System.out.println("New socketClient added: " + player.getName());
                    addClientHelper(player);
                } else
                    handleSocketLostClient(client);
                return;
            }
        }
        sendTo(new ErrorEvent("ACCESSO NEGATO\nPartita già iniziata!"), client);
    }

    @Override
    public void addClient(ClientInterfaceRMI client) {
        synchronized (lock) {
            if(rmiClients.size() + socketClients.size() < 4) {
                if(!gameStarted) {
                    try {
                        for (Player player: players) {
                            if (player.getName().equals(client.getName()))
                                throw new IllegalArgumentException("Nickname già in uso!");
                        }
                        Player player = new Player(client.getName());
                        player.setClientInterface(client);
                        players.add(player);
                        rmiClients.add(client);

                        System.out.println("New rmiClient added: " + player.getName());
                        addClientHelper(player);
                    }
                    catch (RemoteException e) {
                        System.out.println("Errore di connessione in addClient: " + e.getMessage());
                    }
                } else
                    handleRMILostClients(client);
                return;
            }
        }
        sendTo(new ErrorEvent("ACCESSO NEGATO\nPartita già iniziata!"), client);
    }

    /**
     * This method reset the view the client that has just reconnected
     * @param player It's the player that has reconnected
     */
    private void resetView(Player player) {
        player.setConnectionLost(false);
        System.out.println(player.getName() + " è tornato in partita.");
        sendTo(new AllWindowPatternChosen(turnDuration), player);
        sendTo(new ShowAllEvent(new DicePatternEvent(model.dicePatternsToString(), model.playersToString(), model.dicePatternsToStringPath(), player.getName()),
                        model.publicObjectiveCardsToString(), model.publicObjectiveCardsToStringPath(),
                        new ToolCardEvent(model.toolCardsToString(), model.toolCardsToStringPath(), model.getFavorTokensOnToolCards()),
                        new DraftPoolEvent(model.draftPoolToString(), model.draftPoolToStringPath()),
                        new RoundTrackEvent(model.getRoundTrack().toString(), model.getRoundTrack().toStringPath()),
                        player.getPrivateObjectiveCardsToString(), player.getPrivateObjectiveCardsToStringPath(),
                        new SetWindowPatternsGUIEvent(model.windowPatternsToStringPath(), model.getFavorTokensNumberPlayers())),
                player);
    }

    /**
     * This method checks if the new socketClient is one of them that have lost connection add rejoin it to the game
     * @param client It's the new socketClient
     */
    private void handleSocketLostClient(ClientInterfaceSocket client) {
        for(Player player: players) {
            if(player.isConnectionLost() && player.getName().equals(client.getClientName())) {
                socketClients.add(client);
                player.setClientInterface(client);
                resetView(player);
                return;
            }
        }
    }

    /**
     * This method checks if the new RMIClient is one of them that have lost connection add rejoin it to the game
     * @param client It's the new RMIClient
     */
    private void handleRMILostClients(ClientInterfaceRMI client) {
        for(Player player: players) {
            try {
                if(player.isConnectionLost() && player.getName().equals(client.getName())) {
                    rmiClients.add(client);
                    player.setClientInterface(client);
                    resetView(player);
                    return;
                }
            }
            catch (RemoteException e) {
                System.out.println("Connection error in handleLostClients: " + e.getMessage());
            }
        }
    }

    /**
     * This method is used if the game is already started and a new client tries to connect
     * @param mvEvent It's the error event that must be sent
     * @param clientInterface It's the RMIClient that can't access to the game
     */
    private void sendTo(MVEvent mvEvent, ClientInterfaceRMI clientInterface) {
        try {
            clientInterface.notify(mvEvent);
        } catch (RemoteException e) {
            System.out.println("Warning failed.");
        }
    }

    /**
     * This method is used if the game is already started and a new client tries to connect
     * @param mvEvent It's the error event that must be sent
     * @param clientInterface It's the socketClient that can't access to the game
     */
    private void sendTo(MVEvent mvEvent, ClientInterfaceSocket clientInterface) {
        clientInterface.notify(mvEvent);
    }

    @Override
    public void sendTo(MVEvent mvEvent, Player currentPlayer) {
        clientInterfaceNotify(currentPlayer, mvEvent, true);
        if (mvEvent.getId() == 16) {
            for (Player player: players) {
                if (!player.getName().equals(currentPlayer.getName()))
                    sendTo(new ErrorEvent(currentPlayer.getName() + " è stato sospeso."), player);
            }
        }
    }

    /**
     * This method sends a mvEvent to a player.
     * @param player It's the player that must receive the event
     * @param mvEvent It's the mvEvent that must be sent
     * @param isAction Used if a RemoteException occurs. It is true if is required an action by the player
     */
    private void clientInterfaceNotify(Player player, MVEvent mvEvent, boolean isAction) {
        if (!player.getName().equals("sagrada") && !player.isConnectionLost()) {
            if (player.getClientInterfaceRMI() != null) {
                try {
                    player.getClientInterfaceRMI().notify(mvEvent);
                } catch (RemoteException e) {
                    if (!player.isConnectionLost()) {
                        rmiClients.remove(player.getClientInterfaceRMI());
                        player.setConnectionLost(true);
                        System.out.println(CONNECTION_LOST + player.getName());
                    }
                    if (rmiClients.size() + socketClients.size() < 2)
                        virtualViewCLI.endGame();
                    else if (isAction)
                        notify(new SkipTurnEvent());
                }
            } else {
                player.getClientInterfaceSocket().notify(mvEvent);
            }
        }
    }

    @Override
    public void send(MVEvent mvEvent) {
        for (Player player: players)
            clientInterfaceNotify(player, mvEvent, false);
        if (mvEvent.getId() == 5) {
            serverCleaner();
        }
    }


    @Override
    public void notify(VCEvent vcEvent) {
        if (vcEvent.getId() == 22) {
            SinglePlayerEvent singlePlayerEvent = (SinglePlayerEvent) vcEvent;
            gameStarted = true;
            createGame(singlePlayerEvent.getDifficulty());
        }
        else
            virtualViewCLI.forwardVCEvent(vcEvent);
    }

    /**
     * This method adds the players that lost their connection to playersToBeRemoved.
     * @param playersToBeRemoved Its' the list of players that must be removed.
     */
    private void tryConnections(List<Player> playersToBeRemoved) {
        for (Player player : players) {
            if (!player.getName().equals("sagrada") && !player.isConnectionLost()) {
                if (player.getClientInterfaceRMI() != null) {
                    try {
                        player.getClientInterfaceRMI().testIfConnected();
                    } catch (RemoteException e) {
                        if (gameStarted) {
                            player.setConnectionLost(true);
                        } else {
                            playersToBeRemoved.add(player);
                        }
                        rmiClients.remove(player.getClientInterfaceRMI());
                        System.out.println(CONNECTION_LOST + player.getName());
                    }
                } else {
                    try {
                        player.getClientInterfaceSocket().testIfConnected();
                    } catch (IOException e) {
                        if (gameStarted) {
                            player.setConnectionLost(true);
                        } else {
                            playersToBeRemoved.add(player);
                        }
                        socketClients.remove(player.getClientInterfaceSocket());
                        System.out.println(CONNECTION_LOST + player.getName());
                    }
                }
            }
        }
    }

    /**
     * Tests if the connections are ok.
     * If they aren't, they are handled.
     */
    private void testConnections() {
        List<Player> playersToBeRemoved = new ArrayList<>();
        List<Player> connectionLostPlayers = new ArrayList<>();
        tryConnections(playersToBeRemoved);
        for (Player player: players) {
            if (player.isConnectionLost())
                connectionLostPlayers.add(player);
        }

        synchronized (lock) {
            if (!gameStarted) {
                for (Player player : playersToBeRemoved)
                    players.remove(player);

                if (!playersToBeRemoved.isEmpty())
                    waitingRoomRefresh();

                if (players.size() < 2 && timer.isAlive())
                    timer.interrupt();
            } else {
                for (Player player: connectionLostPlayers)
                    lostConnectionHandler(player);
            }
        }
    }

    /**
     * This method cleans the server's attributes making it ready for another game.
     */
    private void serverCleaner() {
        rmiClients.clear();
        socketClients.clear();
        synchronized (lock) {
            players.clear();
        }
        controller = null;
        gameStarted = false;
        if (timer.isAlive())
            timer.interrupt();
        GameSetupSingleton.instanceToNull();
        GameSingleton.instanceToNull();
        virtualViewCLI.deleteObservers();
        System.out.println("The game is ended.\n");
    }

    /**
     * This class represents the timer for the start of a new game
     */
    class Timer extends Thread {

        @Override
        public void run() {
            try {
                while (!gameStarted) {
                    sleep(setupDuration);
                    if (gameStarted)
                        return;
                    if (rmiClients.size() + socketClients.size() > 1) {
                        testConnections();
                        gameStarted = true;
                        createGame(0);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * This class collect all the clients that have lost the connection
     */
    class HeartBeat extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    sleep(5000);
                    testConnections();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private void lostConnectionHandler(Player player) {
        if (gameStarted) {
            if (socketClients.size() + rmiClients.size() < 2) {
                virtualViewCLI.endGame();
            }
            else if (player.getName().equals(model.getCurrentPlayer().getName())) {
                notify(new SkipTurnEvent());
                send(new ErrorEvent(player.getName() + ABANDONED_GAME));
            }
            else
                send(new ErrorEvent(player.getName() + ABANDONED_GAME));
        }
    }

    /**
     * This method removes a socket client that has lost the connection.
     * @param client The client that has lost the connection.
     */
    public void removeClient(ClientInterfaceSocket client) {
        Player toBeRemoved = null;
        socketClients.remove(client);
        for (Player player: players) {
            if (player.getName().equals(client.getClientName())) {
                toBeRemoved = player;
                if (gameStarted) {
                    player.setConnectionLost(true);
                    lostConnectionHandler(player);
                }
                break;
            }
        }
        if (!gameStarted) {
            synchronized (lock) {
                players.remove(toBeRemoved);
            }
            waitingRoomRefresh();
        }
        if (toBeRemoved != null)
            System.out.println(CONNECTION_LOST + toBeRemoved.getName());
    }

}