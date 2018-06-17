package it.polimi.se2018.events.networkevent;

import java.io.Serializable;

/**
 * This abstract class is extended by the various network events
 * @author Framonti
 */
public abstract class NetworkEvent implements Serializable {

    static final long serialVersionUID = 49L;
    private int id;

    public NetworkEvent(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
