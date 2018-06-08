package it.polimi.se2018.events.mvevent;

import java.util.List;

/**
 * This Event holds informations about the DicePattern
 * @author Framonti
 */
public class ShowAllEvent extends MVEvent{

    private DicePatternEvent dicePatternEvent;
    private List<String> publicObjectiveCardsString;
    private ToolCardEvent toolCardEvent;
    private DraftPoolEvent draftPoolEvent;
    private RoundTrackEvent roundTrackEvent;
    private String privateObjectiveCardString;
    private String privateObjectiveCardStringGUI;
    private List<String> publicObjectiveCardsGUI;
    private SetWindowPatternsGUIEvent setWindowPatternsGUIEvent;


    /**
     * Constructor
     * @param dicePatternEvent A DicePatternEvent with all the DicePattern in game and their owner, each represented by a String
     * @param publicObjectiveCardsString A List of all the PublicObjectiveCard, each represented by a String
     * @param toolCardEvent A ToolCardEvent with all the ToolCard, each represented by a String
     * @param draftPoolEvent A DraftPoolEvent representing the draftPool
     * @param roundTrackEvent A RoundTrackEvent representing the RoundTrack
     * @param privateObjectiveCardString A String representing a PrivateObjectiveCard
     */
    public ShowAllEvent(DicePatternEvent dicePatternEvent, List<String> publicObjectiveCardsString, List<String> publicObjectiveCardsGUI,
                        ToolCardEvent toolCardEvent, DraftPoolEvent draftPoolEvent, RoundTrackEvent roundTrackEvent, String privateObjectiveCardString, String privateObjectiveCardStringGUI, SetWindowPatternsGUIEvent setWindowPatternsGUIEvent){

        super(7);
        this.dicePatternEvent = dicePatternEvent;
        this.draftPoolEvent = draftPoolEvent;
        this.privateObjectiveCardString = privateObjectiveCardString;
        this.toolCardEvent = toolCardEvent;
        this.publicObjectiveCardsString = publicObjectiveCardsString;
        this.roundTrackEvent = roundTrackEvent;
        this.publicObjectiveCardsGUI = publicObjectiveCardsGUI;
        this.privateObjectiveCardStringGUI = privateObjectiveCardStringGUI;
        this.setWindowPatternsGUIEvent = setWindowPatternsGUIEvent;
    }

    /**
     * Gets all the DicePattern
     * @return A List of String representing all the DicePattern
     */
    public DicePatternEvent getDicePatterns() {

        return dicePatternEvent;
    }

    /**
     * Gets all the PublicObjectiveCard
     * @return A List of String representing all the PublicObjectiveCard
     */
    public List<String> getPublicObjectiveCardsString() {

        return publicObjectiveCardsString;
    }

    /**
     * Gets all the ToolCard
     * @return A List of String representing all the ToolCard
     */
    public ToolCardEvent getToolCards() {

        return toolCardEvent;
    }

    /**
     * Gets the draftPool
     * @return A String representing the draftPool
     */
    public DraftPoolEvent getDraftPool() {

        return draftPoolEvent;
    }

    /**
     * Gets the RoundTrack
     * @return A String representing the RoundTrack
     */
    public RoundTrackEvent getRoundTrack() {

        return roundTrackEvent;
    }

    /**
     * Gets the PrivateObjectiveCard
     * @return A String representing the PrivateObjectiveCard
     */
    public String getPrivateObjectiveCardString() {

        return privateObjectiveCardString;
    }

    public List<String> getPublicObjectiveCardsGUI() {
        return publicObjectiveCardsGUI;
    }

    public SetWindowPatternsGUIEvent getSetWindowPatternsGUIEvent() {
        return setWindowPatternsGUIEvent;
    }

    public String getPrivateObjectiveCardStringGUI() {
        return privateObjectiveCardStringGUI;
    }
}
