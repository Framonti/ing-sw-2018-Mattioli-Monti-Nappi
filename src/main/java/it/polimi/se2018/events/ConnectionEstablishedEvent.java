package it.polimi.se2018.events;

import java.io.Serializable;

/**
 * @author Framonti
 */
public class ConnectionEstablishedEvent implements Serializable{

    private int id;
    private boolean firstTimeNickname;
    static final long serialVersionUID = 44L;

    public ConnectionEstablishedEvent(boolean firstTimeNickname){
        id = 25;
        this.firstTimeNickname = firstTimeNickname;
    }

    public boolean isFirstTimeNickname() {
        return firstTimeNickname;
    }
}
