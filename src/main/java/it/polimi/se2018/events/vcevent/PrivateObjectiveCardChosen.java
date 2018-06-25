package it.polimi.se2018.events.vcevent;

public class PrivateObjectiveCardChosen extends VCEvent {

    private int indexOfChosenCard;

    public PrivateObjectiveCardChosen(String index) {
        super(17);
        if (index.equals("1") || index.equals("2"))
            indexOfChosenCard = Integer.parseInt(index) - 1;
        else
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
    }

    public int getIndexOfChosenCard() {
        return indexOfChosenCard;
    }
}
