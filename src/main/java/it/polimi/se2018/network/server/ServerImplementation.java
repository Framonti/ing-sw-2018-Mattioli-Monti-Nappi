package it.polimi.se2018.network.server;

import it.polimi.se2018.ConfigurationParametersLoader;
import it.polimi.se2018.controller.ControllerCLI;
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

    private transient List<ClientInterfaceRMI> rmiClients = new ArrayList<>();
    private transient List<ClientInterfaceSocket> socketClients = new ArrayList<>();
    private transient List<Player> players = new ArrayList<>();

    private transient ControllerCLI controllerCLI;
    private final transient Object lock = new Object();
    private transient VirtualViewCLI virtualViewCLI;
    private transient Timer timer = new Timer();
    private boolean gameStarted = false;
    private int setupDuration;

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
        setupDuration = 5000;
        new ClientCollector().start();
    }

    /**
     * This method creates a new game and ask the players to choose their windowPattern
     */
    private void createGame() {

        ConfigurationParametersLoader configurationParametersLoader = new ConfigurationParametersLoader("src/main/java/it/polimi/se2018/xml/ConfigurationParameters.xml");
        int turnDuration = configurationParametersLoader.getTurnTimer();

        GameSetupSingleton.instance();
        GameSetupSingleton.instance().addPlayers(players);
        GameSingleton model = GameSetupSingleton.instance().createNewGame();
        controllerCLI = new ControllerCLI(virtualViewCLI, model.getToolCards(), model, turnDuration);

        virtualViewCLI.addObserver(controllerCLI);
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
            sendTo(new WindowPatternsEvent(windowPatterns, windowPatternsPath, player.getPrivateObjectiveCard().toString(), player.getPrivateObjectiveCardToString()), player);

            synchronized (Server.windowPatternLock) {
                while (player.getWindowPattern() == null) {
                    try {
                        Server.windowPatternLock.wait();
                    } catch (InterruptedException e) {
                        System.out.println("This thread should not be interrupted!");
                        Thread.currentThread().interrupt();
                    }
                }
            }

        }

        send(new AllWindowPatternChosen());

        controllerCLI.game();
    }

    private void addClientHelper(Player player) {
        sendTo(new NickNameAcceptedEvent(), player);
        List<String> names = new ArrayList<>();

        for(Player playerToAdd : players)
            names.add(playerToAdd.getName());
        send(new ClientAlreadyConnectedEvent(names));

        if(players.size() == 2) {
            timer = new Timer();
            timer.start();
        } else if(players.size() == 4) {
            timer.interrupt();
            gameStarted = true;
            createGame();
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
                    player.getClientInterfaceSocket().setClientName(name);
                    players.add(player);
                    socketClients.add(client);

                    System.out.println("socketClient added.");
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

                        System.out.println("rmiClient added.");
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
     * This method checks if the new socketClient is one of them that have lost connection add rejoin it to the game
     * @param client It's the new socketClient
     */
    private void handleSocketLostClient(ClientInterfaceSocket client) {
        for(Player player: players) {
            if(player.isConnectionLost() && player.getName().equals(client.getClientName())) {
                socketClients.add(client);
                player.setClientInterface(client);
                player.getClientInterfaceSocket().setClientName(client.getClientName());
                player.setConnectionLost(false);
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
                    player.setConnectionLost(false);
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
    }

    private void clientInterfaceNotify(Player player, MVEvent mvEvent, boolean isAction) {
        if (!player.isConnectionLost()) {
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
                        controllerCLI.endGame();
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
        for (Player player: players) {
            clientInterfaceNotify(player, mvEvent, false);
        }
    }


    @Override
    public void notify(VCEvent vcEvent) {
        virtualViewCLI.forwardVCEvent(vcEvent);
    }

    private void tryConnections(List<Player> playersToBeRemoved) {
        for (Player player : players) {
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

    private void testConnections() {
        List<Player> playersToBeRemoved = new ArrayList<>();
        synchronized (lock) {
            tryConnections(playersToBeRemoved);

            for (Player player : playersToBeRemoved)
                players.remove(player);

            if (players.size() < 2 && timer.isAlive())
                timer.interrupt();
        }
    }

    /**
     * This class represents the timer for the start of a new game
     */
    class Timer extends Thread {

        @Override
        public void run() {
            while (!gameStarted) {
                try {
                    sleep(setupDuration);
                    if (gameStarted)
                        return;
                    if (rmiClients.size() + socketClients.size() > 1) {
                        testConnections();
                        gameStarted = true;
                        createGame();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * This class collect all the clients that have lost the connection
     */
    class ClientCollector extends Thread {

        @Override
        public void run() {
            while (!gameStarted) {
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

    public void removeClient(ClientInterfaceSocket client) {
        Player toBeRemoved = null;
        socketClients.remove(client);
        for (Player player: players) {
            if (gameStarted && player.getName().equals(client.getClientName())) {
                toBeRemoved = player;
                player.setConnectionLost(true);
                if (socketClients.size() + rmiClients.size() < 2)
                    controllerCLI.endGame();
                else
                    notify(new SkipTurnEvent());
                break;
            } else if (player.getName().equals(client.getClientName())) {
                toBeRemoved = player;
            }
        }
        if (!gameStarted)
            players.remove(toBeRemoved);
        if (toBeRemoved != null)
            System.out.println(CONNECTION_LOST + toBeRemoved.getName());
    }

}