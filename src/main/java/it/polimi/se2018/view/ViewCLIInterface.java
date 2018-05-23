package it.polimi.se2018.view;

import it.polimi.se2018.events.mvevent.*;

/**
 * This is the interface used by ViewCLI and VirtualViewCLI
 * @author fabio
 */
public interface ViewCLIInterface {

    /**
     * Shows all the actions that the player can do
     * @param mvEvent It's the MVEvent received
     */
    void showActionMenu(MVEvent mvEvent);

    /**
     * Opens the standard input, creates an event and notify it to its observer
     */
    void getInput();

    /**
     * Prints the error message if the action selected by the player is not valid
     * @param mvEvent It's the MVEvent received
     */
    void showError(MVEvent mvEvent);

    /**
     * Shows the 4 window patterns among which the player has to choose one (at the start of the game)
     * @param mvEvent It's the MVEvent received
     */
    void showWindowPatterns(MVEvent mvEvent);

    /**
     * Shows everything that it's visible to the player
     * @param mvEvent It's the MVEvent received
     */
    void showAll(MVEvent mvEvent);

    void showEndTurn(MVEvent mvEvent);

    void fluxBrushChoice();

}
