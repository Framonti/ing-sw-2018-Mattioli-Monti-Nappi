package it.polimi.se2018.view;

import it.polimi.se2018.events.mvevent.*;

public interface ViewCLIInterface {

    void showActionMenu(MVEvent mvEvent);

    void getInput();

    void showToolCards(MVEvent mvEvent);

    void showDraftPool(MVEvent mvEvent);

    void showRoundTrack(MVEvent mvEvent);

    void showScoreTrack(MVEvent mvEvent);

    void showError(MVEvent mvEvent);

    void showAll(MVEvent mvEvent);

}
