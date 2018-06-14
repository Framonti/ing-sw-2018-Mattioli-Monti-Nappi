package it.polimi.se2018.view;

import it.polimi.se2018.events.mvevent.*;

/**
 * This is the interface used by ViewCLI and VirtualViewCLI
 * @author fabio
 */
public interface ViewCLIInterface {

    /**
     * To be used only during fluxRemover ToolCard
     * Asks which value must be set to the dice and where must be placed
     * @param mvEvent
     */
    void fluxRemoverChoice(MVEvent mvEvent);

    /**
     * To be used only during fluxBrush ToolCard
     * Asks where the player wants to place the dice
     */
    void fluxBrushChoice(MVEvent mvEvent);

    /**
     * Opens the standard input, creates an event and notify it to its observer
     */
    void getInput();

    /**
     * Suspends the input request, notifies the client that it has been suspended and encourage to rejoin the game
     */
    void playerSuspended();

    /**
     * Shows all the actions that the player can do
     * @param mvEvent It's the MVEvent received
     */
    void showActionMenu(MVEvent mvEvent);

    /**
     * Shows everything that it's visible to the player
     * @param mvEvent It's the MVEvent received
     */
    void showAll(MVEvent mvEvent);

    /**
     * Prints the error message if the action selected by the player is not valid
     * @param mvEvent It's the MVEvent received
     */
    void showError(MVEvent mvEvent);

    /**
     * Shows the message of the turn's end
     * @param mvEvent It's the MVEvent received
     */
    void showEndTurn(MVEvent mvEvent);

    /**
     * Shows the 4 window patterns among which the player has to choose one (at the start of the game)
     * @param mvEvent It's the MVEvent received
     */
    void showWindowPatterns(MVEvent mvEvent);

}
