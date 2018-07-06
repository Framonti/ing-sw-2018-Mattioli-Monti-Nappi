package it.polimi.se2018.events.mvevent;

import java.util.List;

/**
 * This event is only sent during a singlePlayer game, before computing the player's score
 * It asks to the player to choose a PrivateObjectiveCard
 * @author  fabio
 */
public class AskPrivateObjectiveCard extends MVEvent {

    private List<String> privateObjectiveCardsString;

    /**
     * @param privateObjectiveCardsString A List representing the privateObjectiveCard of a player
     */
    public AskPrivateObjectiveCard(List<String> privateObjectiveCardsString) {
        super(17);
        this.privateObjectiveCardsString = privateObjectiveCardsString;
    }

    /**
     * Gets the privateObjectiveCard
     * @return The privateObjectiveCard
     */
    public List<String> getPrivateObjectiveCardsString() {
        return privateObjectiveCardsString;
    }

}
