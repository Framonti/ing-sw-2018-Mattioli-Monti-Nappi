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
     * @param playersNames A List containing all the players' names
     * @param scores A List containing all the final scores of the players
     */
    public ScoreTrackEvent(List<String> playersNames, List<Integer> scores){

        super(5);
        this.playersNames = playersNames;
        this.scores = scores;
    }

    /**
     * Gets the playersNames
     * @return A List with all the players' names
     */
    public List<String> getPlayersNames() {

        return playersNames;
    }

    /**
     * Gets the scores
     * @return A List with all the scores
     */
    public List<Integer> getScores() {

        return scores;
    }
}
