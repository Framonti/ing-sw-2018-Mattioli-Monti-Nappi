package it.polimi.se2018.network.client;

import it.polimi.se2018.events.mvevent.MVEvent;

import java.io.IOException;
/**
 * This interface is used for ClientImplementation if the chosen connection is Socket
 * @author fabio
 */
public interface ClientInterfaceSocket {

    /**
     * @return The name of the related client
     */
    String getClientName();

    /**
     * This method sets the ClientImplementation as changed, this causes the call of the update method of viewCLI
     * @param mvEvent It's the event that must be forwarded to the view
     */
    void notify(MVEvent mvEvent);

    void testIfConnected() throws IOException;
}
