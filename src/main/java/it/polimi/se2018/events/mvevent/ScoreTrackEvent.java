package it.polimi.se2018.events.mvevent;

/**
 * This Event contains informations about the ScoreTrack
 */
public class ScoreTrackEvent extends MVEvent {

    private String scoreTrackString;

    /**
     * Constructor
     * @param scoreTrackString A String representing the ScoreTrack
     */
    public ScoreTrackEvent(String scoreTrackString){

        super(5);
        this.scoreTrackString = scoreTrackString;
    }

    /**
     * Gets the scoreTrackString
     * @return The scoreTrackString
     */
    public String getScoreTrackString() {

        return scoreTrackString;
    }

}
