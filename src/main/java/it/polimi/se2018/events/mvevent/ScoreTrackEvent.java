package it.polimi.se2018.events.mvevent;

import java.util.List;

/**
 * This Event contains informations about the ScoreTrack
 * @author Framonti
 */
public class ScoreTrackEvent extends MVEvent {

    private List<String> playersNames;
    private List<Integer> scores;

    /**
     * Constructor
     * @param playersNames A String representing the ScoreTrack
     */
    public ScoreTrackEvent(List<String> playersNames, List<Integer> scores){

        super(5);
        this.playersNames = playersNames;
        this.scores = scores;
    }

    public List<String> getPlayersNames() {

        return playersNames;
    }

    public List<Integer> getScores() {

        return scores;
    }
}
