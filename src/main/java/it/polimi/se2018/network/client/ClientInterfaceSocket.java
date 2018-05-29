package it.polimi.se2018.network.client;

import it.polimi.se2018.events.mvevent.MVEvent;

/**
 * This interface is used for ClientImplementation if the chosen connection is Socket
 * @author fabio
 */
public interface ClientInterfaceSocket {

    /**
     * @return The name of the related client
     */
    String getName();

    /**
     * This method sets the ClientImplementation as changed, this causes the call of the update method of viewCLI
     * @param mvEvent It's the event that must be forwarded to the view
     */
    void notify(MVEvent mvEvent);

    //TODO non so se serve davvero, bisogna controllare
    void testIfConnected();
}
