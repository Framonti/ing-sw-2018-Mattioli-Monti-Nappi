package it.polimi.se2018.events.vcevent;

public class SinglePlayerEvent extends VCEvent{

    private int difficulty;

    public SinglePlayerEvent(int difficulty){

        super(22);
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
