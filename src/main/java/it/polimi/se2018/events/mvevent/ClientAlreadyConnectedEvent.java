package it.polimi.se2018.events.mvevent;

import java.util.List;

/**
 * This event holds information about the Client that are already connected to the server
 * @author fabio
 */
public class ClientAlreadyConnectedEvent extends MVEvent {

    private List<String> clientConnected;

    /**
     * Constructor
     * @param clientConnected A List containing the names of the clients already connected to the server
     */
    public ClientAlreadyConnectedEvent(List<String> clientConnected){
        super(70);
        this.clientConnected = clientConnected;
    }

    /**
     * Gets the client already connected
     * @return A List containing the names of the clients already connected
     */
    public List<String> getClientConnected() {
        return clientConnected;
    }
}
