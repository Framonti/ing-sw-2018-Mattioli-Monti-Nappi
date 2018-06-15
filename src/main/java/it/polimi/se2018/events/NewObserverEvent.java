package it.polimi.se2018.events;

import it.polimi.se2018.network.client.ClientImplementation;

/**
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
