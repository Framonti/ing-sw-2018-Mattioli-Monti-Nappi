package it.polimi.se2018;

public class ScoreTrack {

    //attributi non ancora pensati

    //Per questo metodo Player deve avere un metodo getName
    public void showScoreTrack(Player currentPlayer) {
        System.out.printf("%s: %d\n", currentPlayer.getName(), currentPlayer.getScore());
    }

    //Metodo alternativo che non richiede il metodo getName
    public void showScoreTrackAlternative(Player currentPlayer) {
        System.out.printf("%d\n", currentPlayer.getScore());
    }
}
