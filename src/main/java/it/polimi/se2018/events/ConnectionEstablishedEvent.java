package it.polimi.se2018.events;

import java.io.Serializable;

public class ConnectionEstablishedEvent implements Serializable{

    private int id;

    public ConnectionEstablishedEvent(){
        id = 25;
    }
}
