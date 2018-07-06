package it.polimi.se2018.events.vcevent;

import java.io.Serializable;

/**
 * Abstract Class that every VCEvent extends
 * @author Framonti
 */
public abstract class VCEvent implements Serializable{

    static final long serialVersionUID = 44L;
    private int id;

    /**
     * @param id The id associated with a specific event
     */
    public VCEvent(int id){

        this.id = id;
    }

    /**
     * @return The Id of the Event
     */
    public int getId() {
        return id;
    }

}
