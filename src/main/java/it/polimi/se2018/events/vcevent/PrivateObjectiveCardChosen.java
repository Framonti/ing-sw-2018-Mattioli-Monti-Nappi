package it.polimi.se2018.events.vcevent;

/**
 * This event is generated when a player choose a privateObjectiveCard at the end of a game in singlePlayer
 */
public class PrivateObjectiveCardChosen extends VCEvent {

    private int indexOfChosenCard;

    /**
     * @param index The index of the PrivateObjectiveCard
     */
    public PrivateObjectiveCardChosen(String index) {
        super(17);
        if (index.equals("1") || index.equals("2"))
            indexOfChosenCard = Integer.parseInt(index) - 1;
        else
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
    }

    /**
     * Gets the chosenCard
     * @return The ChosenCard
     */
    public int getIndexOfChosenCard() {
        return indexOfChosenCard;
    }
}
