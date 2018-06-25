package it.polimi.se2018.events.mvevent;

import java.util.List;

public class AskPrivateObjectiveCard extends MVEvent {

    private List<String> privateObjectiveCardsString;
    private List<String> privateObjectiveCardsPath;

    public AskPrivateObjectiveCard(List<String> privateObjectiveCardsString, List<String> privateObjectiveCardsPath) {
        super(17);
        this.privateObjectiveCardsString = privateObjectiveCardsString;
        this.privateObjectiveCardsPath = privateObjectiveCardsPath;
    }

    public List<String> getPrivateObjectiveCardsString() {
        return privateObjectiveCardsString;
    }

    public List<String> getPrivateObjectiveCardsPath() {
        return privateObjectiveCardsPath;
    }
}
