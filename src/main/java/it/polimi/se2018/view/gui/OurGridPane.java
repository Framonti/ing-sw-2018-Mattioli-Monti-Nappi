package it.polimi.se2018.view.gui;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class OurGridPane {
    private GridPane gridPane;
    private String playerName;
    private String windowPatternPath;
    private Label playerNameLabel;
    private int favorTokens;
    private Label favorTokensLabel;

    public OurGridPane(GridPane gridPane, String playerName, String windowPatternPath, Label playerNameLabel, int favorTokens, Label favorTokensLabel){
        this.gridPane = gridPane;
        this.playerName = playerName;
        this.windowPatternPath = windowPatternPath;
        this.playerNameLabel = playerNameLabel;
        this.favorTokens = favorTokens;
        this.favorTokensLabel = favorTokensLabel;
        setFavorTokensLabel(favorTokens);
        setPlayerNameLabel(playerName);

    }

    public OurGridPane(GridPane gridPane, String playerName, String windowPatternPath, Label playerNameLabel) {

        this.gridPane = gridPane;
        this.playerName = playerName;
        this.windowPatternPath = windowPatternPath;
        this.playerNameLabel = playerNameLabel;
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public String getPlayerName() {
        return playerName;
    }

    private void setPlayerNameLabel(String name){
        Platform.runLater(() -> playerNameLabel.setText(name));
    }

    public void setFavorTokensLabel(int favorTokens){
        Platform.runLater(() -> favorTokensLabel.setText(Integer.toString(favorTokens)));
    }

}
