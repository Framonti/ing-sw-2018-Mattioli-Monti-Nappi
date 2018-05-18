package it.polimi.se2018.events.mvevent;

import java.util.List;

/**
 * This Event holds informations about the DicePattern
 * @author Framonti
 */
public class ShowAllEvent extends MVEvent{

    private List<String> dicePatternsString;
    private List<String> namePlayers;
    private List<String> publicObjectiveCardsString;
    private List<String> toolCardsString;
    private String draftPoolString;
    private String roundTrackString;
    private String privateObjectiveCardString;


    /**
     * Constructor
     * @param dicePatternsString A List of all the DicePattern in game, each represented by a String
     * @param namePlayers A List of all the players nickname
     * @param publicObjectiveCardsString A List of all the PublicObjectiveCard, each represented by a String
     * @param toolCardString A List of all the ToolCard, each represented by a String
     * @param draftPoolString A String representing the draftPool
     * @param roundTrackString A String representing the RoundTrack
     * @param privateObjectiveCardString A String representing a PrivateObjectiveCard
     */
    public ShowAllEvent(List<String> dicePatternsString, List<String> namePlayers, List<String> publicObjectiveCardsString,
                        List<String> toolCardString, String draftPoolString, String roundTrackString, String privateObjectiveCardString){

        super(100);
        this.dicePatternsString = dicePatternsString;
        this.draftPoolString = draftPoolString;
        this.namePlayers = namePlayers;
        this.privateObjectiveCardString = privateObjectiveCardString;
        this.toolCardsString = toolCardString;
        this.publicObjectiveCardsString = publicObjectiveCardsString;
        this.roundTrackString = roundTrackString;
    }

    /**
     * Gets all the DicePattern
     * @return A List of String representing all the DicePattern
     */
    public List<String> getDicePatternsString() {

        return dicePatternsString;
    }

    /**
     * Gets all the players' nicknames
     * @return A List of String representing all the players' nicknames
     */
    public List<String> getNamePlayers() {

        return namePlayers;
    }

    /**
     * Gets all the PublicObjectiveCard
     * @return A List of String representing all the PublicObjectiveCard
     */
    public List<String> getPublicObjectivCardsString() {

        return publicObjectiveCardsString;
    }

    /**
     * Gets all the ToolCard
     * @return A List of String representing all the ToolCard
     */
    public List<String> getToolCardsString() {

        return toolCardsString;
    }

    /**
     * Gets the draftPool
     * @return A String representing the draftPool
     */
    public String getDraftPoolString() {

        return draftPoolString;
    }

    /**
     * Gets the RoundTrack
     * @return A String representing the RoundTrack
     */
    public String getRoundTrackString() {

        return roundTrackString;
    }

    /**
     * Gets the PrivateObjectiveCard
     * @return A String representing the PrivateObjectiveCard
     */
    public String getPrivateObjectiveCardString() {

        return privateObjectiveCardString;
    }

}
