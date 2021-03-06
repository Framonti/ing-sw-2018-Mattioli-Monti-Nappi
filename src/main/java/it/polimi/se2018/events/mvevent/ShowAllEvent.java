package it.polimi.se2018.events.mvevent;

import java.util.List;

/**
 * This Event holds information about everything visible during a game
 * @author Framonti
 */
public class ShowAllEvent extends MVEvent{

    private DicePatternEvent dicePatternEvent;
    private List<String> publicObjectiveCardsString;
    private ToolCardEvent toolCardEvent;
    private DraftPoolEvent draftPoolEvent;
    private RoundTrackEvent roundTrackEvent;
    private List<String> privateObjectiveCardsString;
    private List<String> privateObjectiveCardsStringGUI;
    private List<String> publicObjectiveCardsGUI;
    private SetWindowPatternsGUIEvent setWindowPatternsGUIEvent;


    /**
     * Constructor
     * @param dicePatternEvent A DicePatternEvent with all the DicePattern in game and their owner, each represented by a String
     * @param publicObjectiveCardsString A List of all the PublicObjectiveCard, each represented by a String
     * @param toolCardEvent A ToolCardEvent with all the ToolCard, each represented by a String
     * @param draftPoolEvent A DraftPoolEvent representing the draftPool
     * @param roundTrackEvent A RoundTrackEvent representing the RoundTrack
     * @param privateObjectiveCardsString A List of Strings representing a PrivateObjectiveCard
     * @param privateObjectiveCardsStringGUI A List of Strings representing a path for loading a PrivateObjectiveCard on the GUI
     * @param publicObjectiveCardsGUI A List of String representing paths for loading PublicObjectiveCards on the GUI
     * @param setWindowPatternsGUIEvent A setWindowPatternGUIEvent for the GUI
     */
    public ShowAllEvent(DicePatternEvent dicePatternEvent, List<String> publicObjectiveCardsString, List<String> publicObjectiveCardsGUI,
                        ToolCardEvent toolCardEvent, DraftPoolEvent draftPoolEvent, RoundTrackEvent roundTrackEvent,
                        List<String> privateObjectiveCardsString, List<String> privateObjectiveCardsStringGUI, SetWindowPatternsGUIEvent setWindowPatternsGUIEvent){

        super(7);
        this.dicePatternEvent = dicePatternEvent;
        this.draftPoolEvent = draftPoolEvent;
        this.privateObjectiveCardsString = privateObjectiveCardsString;
        this.toolCardEvent = toolCardEvent;
        this.publicObjectiveCardsString = publicObjectiveCardsString;
        this.roundTrackEvent = roundTrackEvent;
        this.publicObjectiveCardsGUI = publicObjectiveCardsGUI;
        this.privateObjectiveCardsStringGUI = privateObjectiveCardsStringGUI;
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
    public List<String> getPrivateObjectiveCardsString() {

        return privateObjectiveCardsString;
    }

    /**
     * Gets the PublicObjectiveCardsGUI
     * @return A List of path for loading the PublicObjectiveCards for the GUI
     */
    public List<String> getPublicObjectiveCardsGUI() {

        return publicObjectiveCardsGUI;
    }

    /**
     * Gets the windowPatternsGUIEvent
     * @return A WindowPatternGUIEvent
     */
    public SetWindowPatternsGUIEvent getSetWindowPatternsGUIEvent() {

        return setWindowPatternsGUIEvent;
    }

    /**
     * Gets the PrivateObjectiveCardStringGUI
     * @return The privateObjectiveCardStringGUI
     */
    public List<String> getPrivateObjectiveCardsStringGUI() {
        return privateObjectiveCardsStringGUI;
    }
}
