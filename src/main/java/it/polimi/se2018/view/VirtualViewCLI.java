package it.polimi.se2018.view;

import it.polimi.se2018.events.mvevent.*;

import java.util.Observable;
import java.util.Observer;

public class VirtualViewCLI extends Observable implements Observer, ViewCLIInterface {

    private MVEvent mvEvent;

    public MVEvent getMvEvent() {
        return mvEvent;
    }

    //TODO
    @Override
    public void getInput() {

    }

    @Override
    public void showActionMenu(ActionMenuEvent actionMenuEvent) {

        mvEvent = actionMenuEvent;
    }

    @Override
    public void showAll(ShowAllEvent showAllEvent) {

        mvEvent = showAllEvent;
    }


    @Override
    public void showDraftPool(DraftPoolEvent draftPoolEvent) {

        mvEvent = draftPoolEvent;
    }

    @Override
    public void showError(ErrorEvent errorEvent) {

        mvEvent = errorEvent;
    }

    @Override
    public void showRoundTrack(RoundTrackEvent roundTrackEvent) {

        mvEvent = roundTrackEvent;
    }

    @Override
    public void showScoreTrack(ScoreTrackEvent scoreTrackEvent) {

        mvEvent = scoreTrackEvent;
    }

    @Override
    public void showToolCards(ToolCardEvent toolCardEvent) {

        mvEvent = toolCardEvent;
    }


    @Override
    public void update(Observable o, Object arg) {

    }
}
