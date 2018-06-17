package it.polimi.se2018.events.networkevent;

/**
 * @author Framonti
 */
public class ConnectionEstablishedEvent extends NetworkEvent{

    private boolean firstTimeNickname;

    public ConnectionEstablishedEvent(boolean firstTimeNickname){
        super(25);
        this.firstTimeNickname = firstTimeNickname;
    }

    public boolean isFirstTimeNickname() {
        return firstTimeNickname;
    }
}
