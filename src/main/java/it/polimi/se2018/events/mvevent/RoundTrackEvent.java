package it.polimi.se2018.events.mvevent;

import java.util.List;

/**
 * This Event contains informations about the RoundTrack
 * @author Framonti
 */
public class RoundTrackEvent extends MVEvent {

    private String roundTrackString;
    private List<String> roundTrackGUI;

    /**
     * Constructor
     * @param roundTrackString A String representing the RoundTrack
     */
    public RoundTrackEvent(String roundTrackString, List<String> roundTrackGUI){

        super(4);
        this.roundTrackString = roundTrackString;
        this.roundTrackGUI = roundTrackGUI;
    }

    /**
     * Gets the roundTrackString
     * @return The roundTrackString
     */
    public String getRoundTrackString() {

        return roundTrackString;
    }


    public List<String> getRoundTrackGUI() {
        return roundTrackGUI;
    }
}

