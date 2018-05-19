package it.polimi.se2018.events.vcevent;

/**
 * This class represents the event related to the toolCard Glazing Hammer
 * @author fabio
 */
public class GlazingHammerEvent extends VCEvent {

    /**
     * Constructor of the class
     */
    public GlazingHammerEvent() {
        super(7);
    }

    /**
     * @return The name of this class
     */
    @Override
    public String toString() {
        return "GlazingHammerEvent";
    }
}
