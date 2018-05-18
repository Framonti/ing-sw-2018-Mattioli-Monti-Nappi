package it.polimi.se2018.view;

import it.polimi.se2018.events.mvevent.*;

public interface ViewCLIInterface {

    void showActionMenu(ActionMenuEvent actionMenuEvent);

    void getInput();

    void showToolCards(ToolCardEvent toolCardEvent);

    void showDraftPool(DraftPoolEvent draftPoolEvent);

    void showRoundTrack(RoundTrackEvent roundTrackEvent);

    void showScoreTrack(ScoreTrackEvent scoreTrackEvent);

    void showError(ErrorEvent errorEvent);

    void showAll(ShowAllEvent showAllEvent);

}
