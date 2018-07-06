package it.polimi.se2018.view.gui;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * This class represents the graphic information of a player: it contains the window pattern, the name and the favor tokens number of a player
 * @author Daniele Mattioli
 */
public class OurGridPane {
    private GridPane gridPane;
    private String playerName;
    private String windowPatternPath;
    private Label playerNameLabel;
    private int favorTokens;
    private Label favorTokensLabel;

    /**
     * Constructor
     * @param gridPane Player's grid pane
     * @param playerName Player's name
     * @param windowPatternPath Player's window pattern path
     * @param playerNameLabel Label containing player's name
     * @param favorTokens Player's favor tokens
     * @param favorTokensLabel Label containing player's favor tokens
     */
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

    /**
     * Constructor if there is only one player
     * @param gridPane Player's grid pane
     * @param playerName Player's name
     * @param windowPatternPath Player's window pattern path
     * @param playerNameLabel Label containing player's name
     */
    public OurGridPane(GridPane gridPane, String playerName, String windowPatternPath, Label playerNameLabel) {

        this.gridPane = gridPane;
        this.playerName = playerName;
        this.windowPatternPath = windowPatternPath;
        this.playerNameLabel = playerNameLabel;
    }

    /**
     * Gets player's grid pane
     * @return Grid Pane
     */
    public GridPane getGridPane() {
        return gridPane;
    }

    /**
     * Gets player's name
     * @return player's name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets player's name label
     * @param name Player's name
     */
    private void setPlayerNameLabel(String name){
        Platform.runLater(() -> playerNameLabel.setText(name));
    }

    /**
     * Sets player's favor tokens number
     * @param favorTokens Player's favor tokens number
     */
    public void setFavorTokensLabel(int favorTokens){
        Platform.runLater(() -> favorTokensLabel.setText(Integer.toString(favorTokens)));
    }

}
