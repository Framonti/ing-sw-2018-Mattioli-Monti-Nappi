package it.polimi.se2018.rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.se2018.events.mvevent.MVEvent;
import it.polimi.se2018.events.vcevent.VCEvent;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.rmi.client.ClientInterface;

/**
 * This interface is used as a remote object in the client
 * @author fabio
 */
public interface ServerInterface extends Remote {

    /**
     * This method adds a new client to the clients' list.
     * If the the clients are 4 or the timeout ends it creates a new game
     * @param client It's the client that must be added
     * @throws RemoteException If there is a problem with the connection
     */
    void addClient(ClientInterface client) throws RemoteException;

    /**
     * This method sends the event in the parameter to all the clients
     * @param event It's the event that must be sent
     * @throws RemoteException If there is a problem with the connection
     */
    void send(MVEvent event) throws RemoteException;

    /**
     * This method sends the event in the parameter to just one client
     * @param event It's the event that must be sent
     * @param currentPlayer It's the player that must receive the event
     * @throws RemoteException If there is a problem with the connection
     */
    void sendTo(MVEvent event, Player currentPlayer) throws RemoteException;

    /**
     * This method makes the view to forward the event to the controller
     * @param vcEvent It's the event that must be forwarded
     * @throws RemoteException If there is a problem with the connection
     */
    void notify(VCEvent vcEvent) throws RemoteException;

}
