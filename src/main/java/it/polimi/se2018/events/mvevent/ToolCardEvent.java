package it.polimi.se2018.events.mvevent;

import java.util.List;

/**
 * This Event holds informations about the ToolCards
 * @author Framonti
 */
public class ToolCardEvent extends MVEvent{

    private List<String> toolCards;
    private List<String> toolCardsGUI;
    private List<String> favorTokensOnToolCards;

    /**
     * Constructor
     * @param toolCards A List representing all the ToolCard in the game
     */
    public ToolCardEvent(List<String> toolCards, List<String> toolCardsGUI,List<String> favorTokensOnToolCards ){

        super(3);
        this.toolCards = toolCards;
        this.toolCardsGUI = toolCardsGUI;
        this.favorTokensOnToolCards = favorTokensOnToolCards;
    }

    /**
     * Gets the toolCard attribute
     * @return The toolCard attribute
     */
    public List<String> getToolCards() {

        return toolCards;
    }

    public List<String> getToolCardsGUI(){
        return toolCardsGUI;
    }

    public List<String> getFavorTokensOnToolCards() {
        return favorTokensOnToolCards;
    }
}
