package it.polimi.se2018.events.vcevent;

import java.io.Serializable;

public abstract class VCEvent implements Serializable{

    static final long serialVersionUID = 44L;
    private int id;

    public VCEvent(int id){

        this.id = id;
    }

    public int getId() {
        return id;
    }

}
