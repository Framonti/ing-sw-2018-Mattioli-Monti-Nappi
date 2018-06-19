package it.polimi.se2018.events.networkevent;

/**
 * This class is a nack that the server gives to an unsuccessfully connected client
 * @author Framonti
 */
public class ConnectionRefusedEvent extends NetworkEvent{

    public ConnectionRefusedEvent(){
        super(80);
    }
}
