package it.polimi.se2018.network.server;

import it.polimi.se2018.ConfigurationParametersLoader;
import it.polimi.se2018.controller.ControllerCLI;
import it.polimi.se2018.events.mvevent.ErrorEvent;
import it.polimi.se2018.events.mvevent.MVEvent;
import it.polimi.se2018.events.mvevent.WindowPatternsEvent;
import it.polimi.se2018.events.vcevent.SkipTurnEvent;
import it.polimi.se2018.events.vcevent.VCEvent;
import it.polimi.se2018.model.GameSetupSingleton;
import it.polimi.se2018.model.GameSingleton;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.network.client.ClientInterfaceRMI;
import it.polimi.se2018.network.client.ClientInterfaceSocket;
import it.polimi.se2018.view.VirtualViewCLI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the implementation of the server
 * @author fabio
 */
public class ServerImplementation extends UnicastRemoteObject implements ServerInterface {

    private transient List<ClientInterfaceRMI> rmiClients = new ArrayList<>();
    private transient List<ClientInterfaceRMI> connectionRMILostClients = new ArrayList<>();
    private transient List<ClientInterfaceSocket> socketClients = new ArrayList<>();
    private transient List<ClientInterfaceSocket> connectionSocketLostClients = new ArrayList<>();
    private transient List<Player> players = new ArrayList<>();

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
        new ClientCollector().start();
    }

    /**
     * @return The list of the connected clients
     * @deprecated
     */
    public List<ClientInterfaceRMI> getClients() {
        return rmiClients;
    }

    /**
     * This method creates a new game and ask the players to choose their windowPattern
     */
    private void createGame() {

        ConfigurationParametersLoader configurationParametersLoader = new ConfigurationParametersLoader("src/main/java/it/polimi/se2018/xml/ConfigurationParameters.xml");
        int turnDuration = configurationParametersLoader.getTurnTimer();

        GameSetupSingleton.instance();
        GameSetupSingleton.instance().addPlayers(createPlayerList(rmiClients, socketClients));
        GameSingleton model = GameSetupSingleton.instance().createNewGame();
        ControllerCLI controllerCLI = new ControllerCLI(virtualViewCLI, model.getToolCards(), model, turnDuration);

        virtualViewCLI.addObserver(controllerCLI);
        model.addObserver(virtualViewCLI);

        for(Player player: model.getPlayers()) {
            List<String> windowPatterns = new ArrayList<>();
            int num = 1;
            for (WindowPattern windowPattern: player.getWindowPatterns()) {
                windowPatterns.add("Numero: " + num + "\n" + windowPattern.toString());
                num++;
            }
            sendTo(new WindowPatternsEvent(windowPatterns), player);

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

        controllerCLI.game();
    }

    /**
     * This method creates as many players as clients are and return them in a list
     * @param rmiClients It's the list of clients connected via RMI
     * @param socketClients It's the list of clients connected via socket
     * @return The list of players in the game
     */
    private List<Player> createPlayerList(List<ClientInterfaceRMI> rmiClients, List<ClientInterfaceSocket> socketClients) {
        for(ClientInterfaceRMI client: rmiClients) {
            try {
                Player player = new Player(client.getName());
                player.setClientInterface(client);
                players.add(player);
            } catch (RemoteException e) {
                System.out.println("Errore di connessione in createPlayerList: " + e.getMessage());
            }
        }

        for(ClientInterfaceSocket client: socketClients) {
            Player player = new Player(client.getName());
            player.setClientInterface(client);
            players.add(player);
        }

        return players;
    }

    /**
     * This method adds the clientInterface to the list of the clients connected via socket.
     * If the conditions are respected it launches the game creation
     * @param client It's the clientInterface to be added
     */
    public void addSocketClient(ClientInterfaceSocket client) {
        synchronized (lock) {
            if(rmiClients.size() + socketClients.size() < 4) {
                if(!gameStarted) {
                    socketClients.add(client);
                    System.out.println("socketClient added.");

                    if(rmiClients.size() + socketClients.size() == 2) {
                        timer = new Timer();
                        timer.start();
                    }

                    if(rmiClients.size() + socketClients.size() == 4) {
                        timer.interrupt();
                        gameStarted = true;
                        createGame();
                    }

                    return;

                } else {
                    handleSocketLostClient(client);
                    return;
                }
            }
        }
        cantPlay(new ErrorEvent("ACCESSO NEGATO\nPartita già iniziata!"), client);
    }

    @Override
    public void addClient(ClientInterfaceRMI client) {
        synchronized (lock) {
            if(rmiClients.size() + socketClients.size() < 4) {
                if(!gameStarted) {
                    rmiClients.add(client);

                    System.out.println("rmiClient added.");

                    if (rmiClients.size() + socketClients.size() == 2) {
                        timer = new Timer();
                        timer.start();
                    }

                    if (rmiClients.size() + socketClients.size() == 4) {
                        timer.interrupt();
                        gameStarted = true;
                        createGame();
                    }

                    return;

                } else {
                    handleRMILostClients(client);
                    return;
                }
            }
        }
        cantPlay(new ErrorEvent("ACCESSO NEGATO\nPartita già iniziata!"), client);
    }

    /**
     * This method checks if the new socketClient is one of them that have lost connection add rejoin it to the game
     * @param client It's the new socketClient
     */
    private void handleSocketLostClient(ClientInterfaceSocket client) {
        for(Player player: players) {
            if(player.getName().equals(client.getName())) {
                socketClients.add(client);
                player.setClientInterface(client);
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
                if(player.getName().equals(client.getName())) {
                    rmiClients.add(client);
                    player.setClientInterface(client);
                    connectionRMILostClients.remove(client);
                    return;
                }
            }
            catch (RemoteException e) {
                System.out.println("Errore di connessione in handleLostClients: " + e.getMessage());
            }
        }
    }

    /**
     * This method is used if the game is already started and a new client tries to connect
     * @param mvEvent It's the error event that must be sent
     * @param clientInterface It's the RMIClient that can't access to the game
     */
    private void cantPlay(MVEvent mvEvent, ClientInterfaceRMI clientInterface) {
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
    private void cantPlay(MVEvent mvEvent, ClientInterfaceSocket clientInterface) {
        clientInterface.notify(mvEvent);
    }

    @Override
    public void sendTo(MVEvent mvEvent, Player currentPlayer) {
        if(currentPlayer.getClientInterfaceRMI() != null) {
            try {
                currentPlayer.getClientInterfaceRMI().notify(mvEvent);
            } catch (RemoteException e) {
                if (connectionRMILostClients.contains(currentPlayer.getClientInterfaceRMI()))
                    notify(new SkipTurnEvent());
                else {
                    connectionRMILostClients.add(currentPlayer.getClientInterfaceRMI());
                    rmiClients.remove(currentPlayer.getClientInterfaceRMI());
                    System.out.println("Connection lost with " + currentPlayer.getName());
                }
            }
        } else {
            currentPlayer.getClientInterfaceSocket().notify(mvEvent);
        }
    }

    @Override
    public void send(MVEvent mvEvent) {
        for(ClientInterfaceRMI clientInterface: rmiClients) {
            try {
                clientInterface.notify(mvEvent);
            }
            catch (RemoteException e){
                if (!connectionRMILostClients.contains(clientInterface)) {
                    connectionRMILostClients.add(clientInterface);
                    rmiClients.remove(clientInterface);
                    System.out.println("Client removed");
                }
            }
        }

        for(ClientInterfaceSocket clientInterface: socketClients)
            clientInterface.notify(mvEvent);
    }


    @Override
    public void notify(VCEvent vcEvent) {
        virtualViewCLI.forwardVCEvent(vcEvent);
    }

    /**
     * This class represents the timer for the start of a new game
     */
    class Timer extends Thread {

        @Override
        public void run() {
            try {
                sleep(setupDuration);
                if(rmiClients.size() + socketClients.size() > 1) {
                    gameStarted = true;
                    createGame();
                } else {
                    timer = new Timer();
                    timer.start();
                }
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * This class collect all the clients that have lost the connection
     */
    class ClientCollector extends Thread {

        @Override
        public void run() {
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            synchronized (lock) {
                for (ClientInterfaceRMI client: rmiClients) {
                    try {
                        client.testIfConnected();
                    } catch (RemoteException e) {
                        if(gameStarted) {
                            connectionRMILostClients.add(client);
                        } else {
                            rmiClients.remove(client);
                        }
                        System.out.println("Connection lost with a client.");
                    }
                }
                //TODO testare anche per SocketClients

                for(ClientInterfaceRMI client: connectionRMILostClients)
                    rmiClients.remove(client);
                for(ClientInterfaceSocket client: connectionSocketLostClients)
                    socketClients.remove(client);

                if(rmiClients.size() + socketClients.size() < 2 && timer.isAlive())
                    timer.interrupt();
            }

            new ClientCollector().start();
        }
    }

}