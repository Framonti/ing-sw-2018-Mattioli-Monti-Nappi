package it.polimi.se2018.events;

import java.io.Serializable;

public class ConnectionEstablishedEvent implements Serializable{

    private int id;
    private boolean firstTimeNickname;

    public ConnectionEstablishedEvent(boolean firstTimeNickname){
        id = 25;
        this.firstTimeNickname = firstTimeNickname;
    }

    public boolean isFirstTimeNickname() {
        return firstTimeNickname;
    }
}
