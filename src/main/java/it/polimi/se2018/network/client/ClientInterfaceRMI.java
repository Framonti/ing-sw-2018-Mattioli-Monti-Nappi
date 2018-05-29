package it.polimi.se2018.network.client;

import it.polimi.se2018.events.mvevent.MVEvent;
import it.polimi.se2018.network.server.ServerInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface is used as a remote object of the client in the server
 * @author fabio
 */
public interface ClientInterfaceRMI extends Remote {

    /**
     * @return The name of the related client
     * @throws RemoteException If there is a problem with the connection
     */
    String getName() throws RemoteException;

    /**
     * This method sets the server attribute of ClientImplementation
     * @param server It's the serverInterface that has to be set
     * @throws RemoteException If there is a problem with the connection
     */
    void setServer(ServerInterface server) throws RemoteException;

    /**
     * This method sets the ClientImplementation as changed, this causes the call of the update method of viewCLI
     * @param mvEvent It's the event that must be forwarded to the view
     * @throws RemoteException If there is a problem with the connection
     */
    void notify(MVEvent mvEvent) throws RemoteException;

    /**
     * This method is used only to check that the client is still connected
     * @throws RemoteException If the client is not connected anymore
     */
    void testIfConnected() throws RemoteException;

}

