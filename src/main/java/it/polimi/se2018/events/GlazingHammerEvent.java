package it.polimi.se2018.events;

/**
 * This class represents the event related to the toolCard Glazing Hammer
 * @author fabio
 */
public class GlazingHammerEvent extends Event {

    /**
     * Constructor of the class
     */
    public GlazingHammerEvent() {
        setId(7);
    }

    /**
     * @return The name of this class
     */
    @Override
    public String toString() {
        return "GlazingHammerEvent";
    }
}
