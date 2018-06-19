package it.polimi.se2018.events.networkevent;

/**
 * This class is an ack that the server gives to a successfully connected client
 * @author Framonti
 */
public class ConnectionEstablishedEvent extends NetworkEvent{

    private boolean firstTimeNickname;

    /**
     * Constructor
     * @param firstTimeNickname True if a client tried to connect to the server for the first time, false otherwise
     */
    public ConnectionEstablishedEvent(boolean firstTimeNickname){
        super(25);
        this.firstTimeNickname = firstTimeNickname;
    }

    /**
     * Gets the firstTimeNickname attribute
     * @return The firstTimeNickname attribute
     */
    public boolean isFirstTimeNickname() {
        return firstTimeNickname;
    }
}
