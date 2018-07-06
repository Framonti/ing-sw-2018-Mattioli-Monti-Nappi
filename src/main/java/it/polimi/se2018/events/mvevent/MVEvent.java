package it.polimi.se2018.events.mvevent;

import java.io.Serializable;

/**
 * This Abstract Class is inherited by all the Events that are exchange from the Model to the View
 * @author Framonti
 */
public abstract class MVEvent implements Serializable{

    static final long serialVersionUID = 45L;

    private int id;

    /**
     * Constructor
     * @param id The event identifier
     */
    public MVEvent(int id){

        this.id = id;
    }

    /**
     * Gets the Id of the event
     * @return The Id of the event
     */
    public int getId(){

        return this.id;
    }
}
