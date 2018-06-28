package it.polimi.se2018.events.networkevent;

import it.polimi.se2018.network.client.ClientImplementation;

/**
 * This class represents a request for a new Observer
 * @author Framonti
 */
public class NewObserverEvent extends NetworkEvent{

    private transient ClientImplementation client;

    /**
     * @param client A client requesting a new observer
     */
    public NewObserverEvent(ClientImplementation client){

        super(90);
        this.client = client;
    }

    /**
     * Gets the Client
     * @return The Client
     */
    public ClientImplementation getClient() {
        return client;
    }
}
