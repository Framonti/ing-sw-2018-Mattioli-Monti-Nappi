package it.polimi.se2018.events.mvevent;

/**
 * This event informs that a turn is ended for a player
 */
public class EndTurnEvent extends MVEvent {

    public EndTurnEvent() {
        super(12);
    }
}
