package it.polimi.se2018.events.networkevent;

import it.polimi.se2018.network.client.ClientImplementation;

/**
 * This class represents a request for a new Observer
 * @author Framonti
 */
public class NewObserverEvent {

    final int id = 90;
    private ClientImplementation client;

    public NewObserverEvent(ClientImplementation client){
        this.client = client;
    }

    public ClientImplementation getClient() {
        return client;
    }
}
