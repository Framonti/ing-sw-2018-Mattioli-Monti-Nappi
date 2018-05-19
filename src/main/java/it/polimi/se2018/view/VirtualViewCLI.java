package it.polimi.se2018.view;

import it.polimi.se2018.events.mvevent.*;
import java.util.Observable;
import java.util.Observer;

public class VirtualViewCLI extends Observable implements Observer, ViewCLIInterface{

    private MVEvent mvEvent;

    public MVEvent getMvEvent() {
        return mvEvent;
    }

    //TODO
    public void getInput() {

    }


    public void showActionMenu(MVEvent showActionMenu) {

        mvEvent = showActionMenu;
    }


    public void showAll(MVEvent showAllEvent) {

        mvEvent = showAllEvent;
    }

    public void showDraftPool(MVEvent draftPoolEvent) {

        mvEvent = draftPoolEvent;
    }


    public void showError(MVEvent errorEvent) {

        mvEvent = errorEvent;
    }


    public void showRoundTrack(MVEvent roundTrackEvent) {

        mvEvent = roundTrackEvent;
    }


    public void showScoreTrack(MVEvent scoreTrackEvent) {

        mvEvent = scoreTrackEvent;
    }


    public void showToolCards(MVEvent toolCardEvent) {

        mvEvent = toolCardEvent;
    }

    public void showDicePattern(MVEvent dicePatternEvent){

        mvEvent = dicePatternEvent;
    }


    @Override
    public void update(Observable o, Object arg) {

        this.mvEvent = (MVEvent) arg;
    }


}
