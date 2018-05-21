package it.polimi.se2018.rmi.server;

import it.polimi.se2018.ConfigurationParametersLoader;
import it.polimi.se2018.controller.ControllerCLI;
import it.polimi.se2018.events.mvevent.MVEvent;
import it.polimi.se2018.events.mvevent.WindowPatternsEvent;
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

    private List<ClientInterface> clients = new ArrayList<>();
    private List<ClientInterface> oldClients = new ArrayList<>();
    private VirtualViewCLI virtualViewCLI;

    /**
     * @return The list of client connected
     */
    public List<ClientInterface> getClients() {
        return clients;
    }

    /**
     * Constructor of this class. It is package-private
     * @param virtualViewCLI It's the VirtualView attribute of the class
     * @throws RemoteException If it fails to export object
     */
    ServerImplementation(VirtualViewCLI virtualViewCLI) throws RemoteException {
        super(0);
        this.virtualViewCLI = virtualViewCLI;
        virtualViewCLI.setServer(this);
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
        List<Player> players = new ArrayList<>();
        try {
            for(ClientInterface client: clients) {
                Player player = new Player(client.getName());
                player.setClientInterface(client);
                players.add(player);
            }
        }
        catch (RemoteException e) {
            System.err.println("Errore di connessione: " + e.getMessage() + "!");
        }
        return players;
    }

    @Override
    public void addClient(ClientInterface client) {
        clients.add(client);
        System.out.println("Client aggiunto");
        if (clients.size() > 1) {
            createGame();
        }
    }

    @Override
    public void sendTo(MVEvent mvEvent, Player currentPlayer) throws RemoteException {
        try {
            currentPlayer.getClientInterface().notify(mvEvent);
        }
        catch (ConnectException e) {
            oldClients.add(currentPlayer.getClientInterface());
            clients.remove(currentPlayer.getClientInterface());
        }
    }

    //filtra errorEvent (non più?)
    @Override
    public void send(MVEvent mvEvent) throws RemoteException{
        for(ClientInterface clientInterface: clients) {
            try {
                clientInterface.notify(mvEvent);
            }
            catch (ConnectException e){
                oldClients.add(clientInterface);
                clients.remove(clientInterface);
                //
                //dovrà fare qualcos'altro? Non mi pare
            }
        }
    }


    @Override
    public void notify(VCEvent vcEvent) {
        virtualViewCLI.forwardVCEvent(vcEvent);
    }

}