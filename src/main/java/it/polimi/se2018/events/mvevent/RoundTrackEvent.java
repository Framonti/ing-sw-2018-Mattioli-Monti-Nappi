package it.polimi.se2018.events.mvevent;

/**
 * This Event contains informations about the RoundTrack
 * @author Framonti
 */
public class RoundTrackEvent extends MVEvent {

    private String roundTrackString;

    /**
     * Constructor
     * @param roundTrackString A String representing the RoundTrack
     */
    public RoundTrackEvent(String roundTrackString){

        super(4);
        this.roundTrackString = roundTrackString;
    }

    /**
     * Gets the roundTrackString
     * @return The roundTrackString
     */
    public String getRoundTrackString() {

        return roundTrackString;
    }
}

