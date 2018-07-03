package it.polimi.se2018.events.vcevent;

/**
 * This event is sent when a player choose to play a singlePlayer game
 * @author Framonti
 */
public class SinglePlayerEvent extends VCEvent{

    private int difficulty;

    /**
     * @param difficulty The difficulty chosen
     */
    public SinglePlayerEvent(int difficulty){

        super(22);
        this.difficulty = difficulty;
    }

    /**
     * Gets the difficulty selected
     * @return The difficulty chosen
     */
    public int getDifficulty() {
        return difficulty;
    }
}
