package it.polimi.se2018.events.networkevent;

import it.polimi.se2018.network.client.ClientImplementation;

/**
 * This class represents a request for a new Observer
 * @author Framonti
 */
public class NewObserverEvent extends NetworkEvent{

    private ClientImplementation client;

    public NewObserverEvent(ClientImplementation client){

        super(90);
        this.client = client;
    }

    public ClientImplementation getClient() {
        return client;
    }
}
