package it.polimi.se2018.view.gui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;


public class OurGridPane {
    private GridPane gridPane;
    private String playerName;
    private String windowPatternPath;

    public OurGridPane(GridPane gridPane, String playerName, String windowPatternPath){
        this.gridPane = gridPane;
        this.playerName = playerName;
        this.windowPatternPath = windowPatternPath;
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getWindowPatternPath() {
        return windowPatternPath;
    }

}
