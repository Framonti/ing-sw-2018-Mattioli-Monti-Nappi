package it.polimi.se2018.events.vcevent;

import java.io.Serializable;

public abstract class Event implements Serializable{

    static final long serialVersionUID = 44L;
    private int id;

    public Event(int id){

        this.id = id;
    }

    public int getId() {
        return id;
    }

}
