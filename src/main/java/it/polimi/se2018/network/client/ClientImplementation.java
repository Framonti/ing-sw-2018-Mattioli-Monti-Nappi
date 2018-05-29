package it.polimi.se2018.network.client;

import it.polimi.se2018.events.mvevent.ErrorEvent;
import it.polimi.se2018.events.mvevent.MVEvent;
import it.polimi.se2018.events.vcevent.VCEvent;
import it.polimi.se2018.network.server.ServerInterface;

import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;

/**
 * This class represents the implementation of the client
 * @author fabio
 */
public class ClientImplementation extends Observable implements ClientInterfaceRMI, ClientInterfaceSocket, Observer {

    private ServerInterface server;
    private String name;


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void notify(MVEvent mvEvent) {
        setChanged();
        notifyObservers(mvEvent);
    }

    /**
     * This method sets the name attribute
     * @param name It's the string representing the name that has to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setServer(ServerInterface server) {
        this.server = server;
    }

    @Override
    public void testIfConnected() {
        //This method is used only to test if the connection of a client is lost.
    }

    @Override
    public void update(Observable o, Object event){
        VCEvent vcEvent = (VCEvent) event;
        try {
            server.notify(vcEvent);
        } catch (RemoteException e) {
            setChanged();
            notifyObservers(new ErrorEvent("Errore di connessione: " + e.getMessage() + "!"));
        }
    }

}