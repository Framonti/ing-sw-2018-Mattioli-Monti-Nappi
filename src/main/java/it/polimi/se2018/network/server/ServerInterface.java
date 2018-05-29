package it.polimi.se2018.network.server;

import it.polimi.se2018.events.mvevent.MVEvent;
import it.polimi.se2018.events.vcevent.VCEvent;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.network.client.ClientInterfaceRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface is used by the ServerImplementation and the NetworkHandler
 * @author fabio
 */
public interface ServerInterface extends Remote {

    /**
     * This method adds a new RMIClient to the game
     * @param client It's the RMIClient to be added
     * @throws RemoteException If there is a problem with the connection
     */
    void addClient(ClientInterfaceRMI client) throws RemoteException;

    /**
     * This method forwards a VCEvent from the client to the server
     * @param vcEvent It's the event that must be forwarded
     * @throws RemoteException If there is a problem with the connection
     */
    void notify(VCEvent vcEvent) throws RemoteException;

    /**
     * This method send a MVEvent from the server to one player
     * @param mvEvent It's the event that must be sent
     * @param player It's the player that must receive the event
     * @throws RemoteException If there is a problem with the connection
     */
    void sendTo(MVEvent mvEvent, Player player) throws RemoteException;

    /**
     * This method send a MVEvent from the server to all the players
     * @param mvEvent It's the event that must be sent
     * @throws RemoteException If there is a problem with the connection
     */
    void send(MVEvent mvEvent) throws RemoteException;
}
