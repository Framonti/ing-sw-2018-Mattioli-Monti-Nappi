package it.polimi.se2018.rmi.server;

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
import it.polimi.se2018.rmi.client.ClientInterface;
import it.polimi.se2018.view.VirtualViewCLI;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the implementation of the server
 * @author fabio
 */
public class ServerImplementation extends UnicastRemoteObject implements ServerInterface {

    private transient List<ClientInterface> clients = new ArrayList<>();
    private transient List<ClientInterface> connectionLostClients = new ArrayList<>();
    private transient List<Player> players = new ArrayList<>();

    private transient VirtualViewCLI virtualViewCLI;
    private int setupDuration;
    private transient Timer timer;
    private boolean gameStarted = false;
    private final transient Object lock = new Object();

    /**
     * Constructor of this class. It is package-private
     * @param virtualViewCLI It's the VirtualView attribute of the class
     * @throws RemoteException If it fails to export object
     */
    ServerImplementation(VirtualViewCLI virtualViewCLI) throws RemoteException {
        super(0);
        this.virtualViewCLI = virtualViewCLI;
        virtualViewCLI.setServer(this);
        ConfigurationParametersLoader configurationParametersLoader = new ConfigurationParametersLoader("src/main/java/it/polimi/se2018/xml/ConfigurationParameters.xml");
        setupDuration = configurationParametersLoader.getSetupTimer();
        new ClientCounter().start();
    }

    /**
     * @return The list of client connected
     */
    public List<ClientInterface> getClients() {
        return clients;
    }

    /**
     * This method creates a new game and ask the players to choose their windowPattern
     */
    private void createGame() {

        ConfigurationParametersLoader configurationParametersLoader = new ConfigurationParametersLoader("src/main/java/it/polimi/se2018/xml/ConfigurationParameters.xml");
        int turnDuration = configurationParametersLoader.getTurnTimer();

        GameSetupSingleton.instance();
        GameSetupSingleton.instance().addPlayers(createPlayerList(clients));
        GameSingleton model = GameSetupSingleton.instance().createNewGame();
        ControllerCLI controllerCLI = new ControllerCLI(virtualViewCLI, model.getToolCards(), model, turnDuration);

        virtualViewCLI.addObserver(controllerCLI);
        model.addObserver(virtualViewCLI);

        try {
            for(Player player: model.getPlayers()) {
                List<String> windowPatterns = new ArrayList<>();
                int num = 1;
                for (WindowPattern windowPattern: player.getWindowPatterns()) {
                    windowPatterns.add("Numero: " + num + "\n" + windowPattern.toString());
                    num++;
                }
                sendTo(new WindowPatternsEvent(windowPatterns), player);
            }
        }
        catch (RemoteException e) {
            System.out.println("Errore di connessione: " + e.getMessage());
        }

        controllerCLI.game();
    }

    /**
     * This method creates as many players as clients are and return them in a list
     * @param clients It's the list of connected clients
     * @return The list of players in the game
     */
    private List<Player> createPlayerList(List<ClientInterface> clients) {
        for(ClientInterface client: clients) {
            try {
                Player player = new Player(client.getName());
                player.setClientInterface(client);
                players.add(player);
            } catch (RemoteException e) {
                System.out.println("Errore di connessione in createPlayerList: " + e.getMessage());
            }
        }
        return players;
    }

    @Override
    public void addClient(ClientInterface client) {
        if(clients.size() < 4) {
            if(!gameStarted) {
                synchronized (lock) {
                    clients.add(client);
                }

                System.out.println("Client added.");

                if (clients.size() == 2) {
                    timer = new Timer();
                    timer.start();
                }

                if (clients.size() == 4) {
                    timer.interrupt();
                    gameStarted = true;
                    createGame();
                }

                return;

            } else {
                handleLostClients(client);
                return;
            }
        }
        cantPlay(new ErrorEvent("ACCESSO NEGATO\nPartita già iniziata!"), client);
    }

    private void handleLostClients(ClientInterface client) {
        for(Player player: players) {
            try {
                if(player.getName().equals(client.getName())) {
                    clients.add(client);
                    player.setClientInterface(client);
                    connectionLostClients.remove(client);
                    return;
                }
            }
            catch (RemoteException e) {
                System.out.println("Errore di connessione in handleLostClients: " + e.getMessage());
            }
        }
    }

    private void cantPlay(MVEvent mvEvent, ClientInterface clientInterface) {
        try {
            clientInterface.notify(mvEvent);
        } catch (RemoteException e) {
            System.out.println("Warning failed.");
        }
    }

    @Override
    public void sendTo(MVEvent mvEvent, Player currentPlayer) throws RemoteException {
        try {
            currentPlayer.getClientInterface().notify(mvEvent);
        } catch (ConnectException e) {
            if(connectionLostClients.contains(currentPlayer.getClientInterface()))
                notify(new SkipTurnEvent());
            else {
                connectionLostClients.add(currentPlayer.getClientInterface());
                clients.remove(currentPlayer.getClientInterface());
                System.out.println("Connection lost with " + currentPlayer.getName());
            }
        }
    }

    @Override
    public void send(MVEvent mvEvent) throws RemoteException {
        for(ClientInterface clientInterface: clients) {
            try {
                clientInterface.notify(mvEvent);
            }
            catch (ConnectException e){
                connectionLostClients.add(clientInterface);
                clients.remove(clientInterface);
                System.out.println("Client removed");
            }
        }
    }


    @Override
    public void notify(VCEvent vcEvent) {
        virtualViewCLI.forwardVCEvent(vcEvent);
    }

    class Timer extends Thread {
        @Override
        public void run() {
            try {
                sleep(setupDuration);
                if(clients.size() > 1) {
                    gameStarted = true;
                    createGame();
                }
                new Timer().start();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    class ClientCounter extends Thread {

        @Override
        public void run() {
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            synchronized (lock) {
                for (ClientInterface client: clients) {
                    try {
                        client.testIfConnected();
                    } catch (RemoteException e) {
                        if(gameStarted) {
                            connectionLostClients.add(client);
                        } else {
                            clients.remove(client);
                        }
                        System.out.println("Connection lost with a client.");
                    }
                }
                for(ClientInterface client: connectionLostClients)
                    clients.remove(client);

            }

            new ClientCounter().start();
        }
    }

}