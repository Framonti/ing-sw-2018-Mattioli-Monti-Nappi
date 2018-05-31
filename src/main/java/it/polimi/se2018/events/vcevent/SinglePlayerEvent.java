package it.polimi.se2018.events.vcevent;

public class SinglePlayerEvent extends VCEvent{

    private boolean singlePlayerGame;

    public SinglePlayerEvent(boolean singlePlayerMode){

        super(22);
        singlePlayerGame = singlePlayerMode;
    }

    public boolean isSinglePlayerGame() {
        return singlePlayerGame;
    }
}
