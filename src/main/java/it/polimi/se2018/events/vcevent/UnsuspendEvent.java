package it.polimi.se2018.events.vcevent;

/**
 * This class represents the event generated when a suspended player can rejoin the game
 * @author fabio
 */
public class UnsuspendEvent extends VCEvent {

    private String name;

    public UnsuspendEvent(String playerName) {
        super(15);
        this.name = playerName;
    }

    /**
     * @return The name of the player that is rejoining the game
     */
    public String getName() {
        return name;
    }
}
