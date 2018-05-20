package it.polimi.se2018.rmi.client;

import it.polimi.se2018.events.mvevent.MVEvent;
import it.polimi.se2018.events.vcevent.VCEvent;
import it.polimi.se2018.rmi.server.ServerInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface is used as a remote object in the server
 * @author fabio
 */
public interface ClientInterface extends Remote {

    /**
     * @return The name of this client
     * @throws RemoteException If there is a problem with the connection
     */
    String getName() throws RemoteException;

    /**
     * @return The vcEvent attribute
     * @throws RemoteException If there is a problem with the connection
     */
    VCEvent getVCEvent() throws RemoteException;

    /**
     * This method sets the server attribute
     * @param server It's the serverInterface that has to be set
     * @throws RemoteException If there is a problem with the connection
     */
    void setServer(ServerInterface server) throws RemoteException;

    /**
     * This method calls the update method of viewCLI
     * @param mvEvent It's the event that must be forwarded
     * @throws RemoteException If there is a problem with the connection
     */
    void notify(MVEvent mvEvent) throws RemoteException;

}

