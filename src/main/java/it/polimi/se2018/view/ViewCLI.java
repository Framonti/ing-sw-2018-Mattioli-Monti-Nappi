package it.polimi.se2018.view;

import it.polimi.se2018.model.*;

import java.util.*;

public class ViewCLI extends Observable implements Observer{

    @Override
    public void update(Observable model, Object stringMessage) {
        System.out.println(stringMessage);
    }

    public void showActionMenu(boolean diceMoved, boolean toolCardUsed) {
        System.out.println("1\tVedi carte utensile\n" +
                "2\tVedi carte obiettivo pubblico\n" +
                "3\tVedi la carta obiettivo privato\n" +
                "4\tVedi la tua carta schema\n" +
                "5\tVedi le carte schema degli avversari\n" +
                "6\tVedi tutte le carte schema in gioco\n" +
                "7\tGuarda la riserva\n" +
                "8\tGuarda il tracciato dei round\n" +
                "9\tGuarda tutto il tavolo di gioco\n" +
                        (toolCardUsed ? "X" : "10") +
                "\tUsa una carta utensile\n" +
                        (diceMoved ? "X" : "11") +
                "\tPosiziona un dado della riserva nello schema\n" +
                "12\tPassa il turno");
    }

    public String getInput() {
        Scanner input = new Scanner(System.in);
        return input.next();
    }

    public void showToolCards(List<ToolCard> toolCards) {
        int num = 1;
        System.out.println("CARTE UTENSILE");
        for(ToolCard toolCard: toolCards) {
            System.out.println("Numero: " + num + "\n" + toolCard.toString() + "\n");
            num++;
        }
    }

    public void showPublicObjectiveCards(List<PublicObjectiveCard> publicObjectiveCards) {
        System.out.println("CARTE OBIETTIVO PUBBLICO");
        for(PublicObjectiveCard publicObjectiveCard: publicObjectiveCards) {
            System.out.println(publicObjectiveCard.toString() + "\n");
        }
    }

    public void showPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
        System.out.println("CARTA OBIETTIVO PRIVATO");
        System.out.println(privateObjectiveCard.toString());
    }

    public void showDraftPool(ArrayList<Dice> draftPool) {
        System.out.println("RISERVA");
        for(Dice dice: draftPool) {
            System.out.println(dice.toString());
        }
    }

    public void showRoundTrack(RoundTrack roundTrack) {
        System.out.println("TRACCIATO DEI ROUND");
        System.out.println("\t\t\t1\t2\t3\t4\t5\t6\t7\t8\t9");
        int round;
        for(round = 1; round < 11; round++)
            System.out.println("Round " + round + ":\t" + roundTrack.roundToString(round));
    }

    public void showScoreTrack(ScoreTrack scoreTrack) {
        System.out.println("TRACCIATO DEI PUNTI");
        System.out.println(scoreTrack.toString());
    }

    public void showMyDicePattern(Player currentPlayer) {
        System.out.println("CARTA SCHEMA DI " + currentPlayer.getName());
        System.out.println(currentPlayer.getDicePattern().toString());
    }

    public void showOthersDicePatterns(GameSingleton model) {
        for(Player player: model.getPlayers()) {
            if(!player.equals(model.getCurrentPlayer())) {
                System.out.println("CARTA SCHEMA DI " + player.getName());
                System.out.println(player.getDicePattern().toString());
            }
        }
    }

    public void showAllDicePatterns(GameSingleton model) {
        showMyDicePattern(model.getCurrentPlayer());
        showOthersDicePatterns(model);
    }

    public void showWindowPatterns(List<WindowPattern> windowPatterns) {
        System.out.println("SELEZIONA UNA CARTA SCHEMA");
        for(WindowPattern windowPattern: windowPatterns)
            System.out.println(windowPattern.toString());
    }

    public void printWinner(Player winner) {
        System.out.println("Il vincitore è " + winner.getName());
    }

    public void printInvalidAction(String move) {
        System.out.println("ATTENZIONE\n'" + move + "' non è un'opzione valida.");
    }

    public void showAll(GameSingleton model) {
        showRoundTrack(model.getRoundTrack());
        showAllDicePatterns(model);
        showDraftPool(model.getDraftPool());
        showPublicObjectiveCards(model.getPublicObjectiveCards());
        showPrivateObjectiveCard(model.getCurrentPlayer().getPrivateObjectiveCard());
        showToolCards(model.getToolCards());
    }

}
