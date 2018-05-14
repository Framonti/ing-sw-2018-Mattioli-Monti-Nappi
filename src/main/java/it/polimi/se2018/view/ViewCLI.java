package it.polimi.se2018.view;

import it.polimi.se2018.model.*;

import java.util.*;

/**
 * This class prints in the command line anything that can be visualized by the players.
 * @author fabio
 */

public class ViewCLI extends Observable implements Observer{

    @Override
    public void update(Observable model, Object stringMessage) {
        System.out.println(stringMessage);
    }

    /**
     * Shows all the actions that the player can do
     * @param isDiceMoved It's true if the player has already moved a dice from the draft pool to his window pattern in his turn, false otherwise
     * @param isToolCardUsed It's true if the player has already used a tool card in his turn, false otherwise
     */
    public void showActionMenu(boolean isDiceMoved, boolean isToolCardUsed) {
        System.out.println("1\tVedi carte utensile\n" +
                "2\tVedi carte obiettivo pubblico\n" +
                "3\tVedi la carta obiettivo privato\n" +
                "4\tVedi la tua carta schema\n" +
                "5\tVedi le carte schema degli avversari\n" +
                "6\tVedi tutte le carte schema in gioco\n" +
                "7\tGuarda la riserva\n" +
                "8\tGuarda il tracciato dei round\n" +
                "9\tGuarda tutto il tavolo di gioco\n" +
                        (isToolCardUsed ? "X" : "10") +
                "\tUsa una carta utensile\n" +
                        (isDiceMoved ? "X" : "11") +
                "\tPosiziona un dado della riserva nello schema\n" +
                "12\tPassa il turno");
    }

    /**
     * Opens the standard input
     * @return The player's decision
     */
    public String getInput() {
        Scanner input = new Scanner(System.in);
        return input.next();
    }

    /**
     * Shows all the tool cards in the game
     * @param toolCards It's the list of all the tool cards in the game
     */
    public void showToolCards(List<ToolCard> toolCards) {
        int num = 1;
        System.out.println("CARTE UTENSILE");
        for(ToolCard toolCard: toolCards) {
            System.out.println("Numero: " + num + "\n" + toolCard.toString() + "\n");
            num++;
        }
    }

    /**
     * Shows all the public objective cards in the game
     * @param publicObjectiveCards It's the list of all the public objective cards in the game
     */
    public void showPublicObjectiveCards(List<PublicObjectiveCard> publicObjectiveCards) {
        System.out.println("CARTE OBIETTIVO PUBBLICO");
        for(PublicObjectiveCard publicObjectiveCard: publicObjectiveCards) {
            System.out.println(publicObjectiveCard.toString() + "\n");
        }
    }

    /**
     * Shows the private objective card of the player itself
     * @param privateObjectiveCard It's the player's private objective card
     */
    public void showPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
        System.out.println("CARTA OBIETTIVO PRIVATO");
        System.out.println(privateObjectiveCard.toString() + "\n");
    }

    /**
     * Shows all the dices in the draft pool
     * @param draftPool It's the list of the dices in the draft pool
     */
    public void showDraftPool(List<Dice> draftPool) {
        System.out.println("RISERVA");
        for(Dice dice: draftPool)
            System.out.println(dice.toString());
        System.out.println();
    }

    /**
     * Shows the round track and all the dices on it
     * @param roundTrack It's the round track in game
     */
    public void showRoundTrack(RoundTrack roundTrack) {
        System.out.println("TRACCIATO DEI ROUND");
        System.out.println("\t\t\t1\t2\t3\t4\t5\t6\t7\t8\t9\n");
        int round;
        for(round = 0; round < 10; round++)
            System.out.println("Round " + (round+1) + ":\t" + roundTrack.roundToString(round));
        System.out.println();
    }

    /**
     * Shows the score of each player at the end of the game
     * @param scoreTrack It's the score track in game
     */
    public void showScoreTrack(ScoreTrack scoreTrack) {
        System.out.println("TRACCIATO DEI PUNTI");
        System.out.println(scoreTrack.toString());
    }

    /**
     * Shows the window pattern of the player with the dices on it
     * @param currentPlayer It's the player itself
     */
    public void showMyDicePattern(Player currentPlayer) {
        System.out.println("CARTA SCHEMA DI " + currentPlayer.getName());
        System.out.println(currentPlayer.getDicePattern().toString());
    }

    /**
     * Shows the window patterns and the dices on them of the opponents
     * @param model It's the whole model
     */
    public void showOthersDicePatterns(GameSingleton model) {
        for(Player player: model.getPlayers()) {
            if(!player.equals(model.getCurrentPlayer())) {
                System.out.println("CARTA SCHEMA DI " + player.getName());
                System.out.println(player.getDicePattern().toString());
            }
        }
    }

    /**
     * Shows all the window pattern and the dices on them of all players
     * @param model It's the whole model
     */
    public void showAllDicePatterns(GameSingleton model) {
        showMyDicePattern(model.getCurrentPlayer());
        showOthersDicePatterns(model);
    }

    /**
     * Shows the 4 window patterns among which the player has to choose one (at the start of the game)
     * @param windowPatterns It's the list of 4 window patterns
     */
    public void showWindowPatterns(List<WindowPattern> windowPatterns) {
        System.out.println("SELEZIONA UNA CARTA SCHEMA");
        for(WindowPattern windowPattern: windowPatterns)
            System.out.println(windowPattern.toString());
    }

    /**
     * Shows who is the winner of the the match
     * @param winner It's the player who has won
     */
    public void printWinner(Player winner) {
        System.out.println("Il vincitore è " + winner.getName());
    }

    /**
     * Prints the error message if the action selected by the player is not valid
     * @param move It's the invalid action
     */
    public void printInvalidAction(String move) {
        System.out.println("ATTENZIONE\n'" + move + "' non è un'opzione valida.");
    }

    /**
     * Shows everything that it's visible to the player
     * @param model It's the whole model
     */
    public void showAll(GameSingleton model) {
        showRoundTrack(model.getRoundTrack());
        showAllDicePatterns(model);
        showDraftPool(model.getDraftPool());
        showPublicObjectiveCards(model.getPublicObjectiveCards());
        showPrivateObjectiveCard(model.getCurrentPlayer().getPrivateObjectiveCard());
        showToolCards(model.getToolCards());
    }

    /**
     * Prints the parameter
     * @param message The string that will be printed
     */
    public void printMessage(String message) {
        System.out.println(message);
    }
}
