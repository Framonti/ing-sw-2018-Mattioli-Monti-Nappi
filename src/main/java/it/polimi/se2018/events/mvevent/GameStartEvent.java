package it.polimi.se2018.events.mvevent;

/**
 * This event informs all clients that the game is started
 * @author Framonti
 */
public class GameStartEvent extends MVEvent {

    public GameStartEvent(){
        super(56);
    }
}
