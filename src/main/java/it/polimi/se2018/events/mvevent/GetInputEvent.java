package it.polimi.se2018.events.mvevent;

/**
 * This class represent the event generated when the controller asks for an input
 * @author fabio
 */
public class GetInputEvent extends MVEvent {

    /**
     * The constructor of this class sets only the event id to 11
     */
    public GetInputEvent() {
        super(11);
    }
}
