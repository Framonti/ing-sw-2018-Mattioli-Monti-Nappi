package it.polimi.se2018.view.gui;

import it.polimi.se2018.events.networkevent.NewGameEvent;
import it.polimi.se2018.events.mvevent.MVEvent;
import it.polimi.se2018.events.mvevent.ScoreTrackEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.util.*;

/**
 * This class manages the EndScreen.fxml file
 * @author Framonti
 */
public class EndScreenController extends Observable implements Observer {

    @FXML private Button exitButton;
    @FXML private Button playAgainButton;
    @FXML private Label winnerLabel;
    @FXML private Label firstPlaceLabel;
    @FXML private Label secondPlaceLabel;
    @FXML private Label thirdPlaceLabel;
    @FXML private Label forthPlaceLabel;
    @FXML private Label firstPlacePoints;
    @FXML private Label secondPlacePoints;
    @FXML private Label thirdPlacePoints;
    @FXML private Label forthPlacePoints;
    @FXML private AnchorPane scene;
    @FXML private ImageView scoreTrack;
    private ImageView yellowScoreMarker = new ImageView();
    private ImageView yellowScoreMarker2 = new ImageView();
    private ImageView redScoreMarker = new ImageView();
    private ImageView redScoreMarker2 = new ImageView();
    private ImageView blueScoreMarker = new ImageView();
    private ImageView blueScoreMarker2 = new ImageView();
    private ImageView greenScoreMarker = new ImageView();
    private ImageView greenScoreMarker2 = new ImageView();

    /**
     * Manages the behaviour of ExitButton
     */
    @FXML
    private void exitButtonAction(){

        ViewGUI.closeProgram();
    }

    @FXML
    private void playAgainButtonAction(){

       setChanged();
       notifyObservers(new NewGameEvent());
    }

    /**
     * Sets a ImageView on the scene, given the required X and Y values
     * @param marker The imageView to place
     * @param X The X value in pixel
     * @param Y The Y value in pixel
     */
    private void setImageOnScene(ImageView marker, int X, int Y){

        marker.setX(X);
        marker.setY(Y);
        scene.getChildren().add(marker);
    }

    /**
     * Sets all the scoreMarker on the right part of the ScoreTrack
     * @param marker The market of the player
     * @param score The score of the player
     */
    private void setScoreMarkerUnit(ImageView marker, int score){

        switch (score%10){
            case 1 : setImageOnScene(marker, 1090, 90);
                break;
            case 2 : setImageOnScene(marker, 1250, 90);
                break;
            case 3 : setImageOnScene(marker, 1430, 90);
                break;
            case 4 : setImageOnScene(marker, 1090, 225);
                break;
            case 5 : setImageOnScene(marker, 1250, 225);
                break;
            case 6 : setImageOnScene(marker, 1430, 225);
                break;
            case 7 : setImageOnScene(marker, 1090, 375);
                break;
            case 8 : setImageOnScene(marker, 1250, 375);
                break;
            case 9 : setImageOnScene(marker, 1430, 375);
                break;
            default: break;
        }
    }

    /**
     * Sets all the scoreMarker on the left part of the ScoreTrack
     * @param marker The market of the player
     * @param score The score of the player
     */
    private void setScoreMarkerDecine(ImageView marker, int score){

        switch (score/10){
            case 1 : setImageOnScene(marker, 100, 80);
                break;
            case 2 : setImageOnScene(marker, 270, 80);
                break;
            case 3 : setImageOnScene(marker, 450, 80);
                break;
            case 4 : setImageOnScene(marker, 100, 225);
                break;
            case 5 : setImageOnScene(marker, 270, 225);
                break;
            case 6 : setImageOnScene(marker, 450, 225);
                break;
            case 7 : setImageOnScene(marker, 100, 375);
                break;
            case 8 : setImageOnScene(marker, 270, 375);
                break;
            case 9 : setImageOnScene(marker, 450, 375);
                break;
            default: break;
        }
    }

    /**
     * Sets all the scoreMarker on the ScoreTrack and
     * @param scoreTrackEvent the event that contains all the info required
     */
    private void setRankingLabels(ScoreTrackEvent scoreTrackEvent){

        firstPlaceLabel.setText(scoreTrackEvent.getPlayersNames().get(0));
        firstPlacePoints.setText(String.valueOf(scoreTrackEvent.getScores().get(0)) + " pts");
        secondPlaceLabel.setText(scoreTrackEvent.getPlayersNames().get(1));
        secondPlacePoints.setText(String.valueOf(scoreTrackEvent.getScores().get(1)) + " pts");
        if(scoreTrackEvent.getPlayersNames().size() > 2){
            thirdPlaceLabel.setText(scoreTrackEvent.getPlayersNames().get(2));
            thirdPlacePoints.setText(String.valueOf(scoreTrackEvent.getScores().get(2)) + " pts");
            if(scoreTrackEvent.getPlayersNames().size() > 3){
                forthPlaceLabel.setText(scoreTrackEvent.getPlayersNames().get(3));
                forthPlacePoints.setText(String.valueOf(scoreTrackEvent.getScores().get(3)) + " pts");
            }
        }
    }

    /**
     * Place all the scoreMarker on the scene
     * @param scores the Score of the players
     */
    private void placeScoreMarkersOnScene(List<Integer> scores){

        setScoreMarkerDecine(blueScoreMarker, scores.get(0));
        setScoreMarkerUnit(blueScoreMarker2, scores.get(0));
        setScoreMarkerDecine(greenScoreMarker, scores.get(1));
        setScoreMarkerUnit(greenScoreMarker2, scores.get(1));
        greenScoreMarker.toFront();
        greenScoreMarker2.toFront();
        if(scores.size() > 2) {
            setScoreMarkerDecine(redScoreMarker, scores.get(2));
            setScoreMarkerUnit(redScoreMarker2, scores.get(2));
            redScoreMarker.toFront();
            redScoreMarker2.toFront();
            if(scores.size() > 3){
                setScoreMarkerDecine(yellowScoreMarker, scores.get(3));
                setScoreMarkerUnit(yellowScoreMarker2, scores.get(3));
                yellowScoreMarker.toFront();
                yellowScoreMarker2.toFront();
            }
        }
    }

    /**
     * Updates the GUI, placing the ScoreMarker on the ScoreTrack and ranking the players
     * @param mvEvent A scoreTrackEvent with all the information needed
     */
    private void scoreTrackEventHandler(MVEvent mvEvent){

        ScoreTrackEvent scoreTrackEvent = (ScoreTrackEvent) mvEvent;
        Platform.runLater(() -> setRankingLabels(scoreTrackEvent));
        Platform.runLater(() -> placeScoreMarkersOnScene(scoreTrackEvent.getScores()));
        Platform.runLater(() -> winnerEventHandler(scoreTrackEvent));
    }

    private void winnerEventHandler(ScoreTrackEvent scoreTrackEvent){

        winnerLabel.setText(scoreTrackEvent.getPlayersNames().get(0));
    }

    /**
     * Initialized the scene, loading all the ScoreMarker from files
     */
    public void initialize(){

        blueScoreMarker.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/ScoreMarker/ScoreMarkerB.png")));
        blueScoreMarker2.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/ScoreMarker/ScoreMarkerB.png")));
        redScoreMarker2.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/ScoreMarker/ScoreMarkerR.png")));
        redScoreMarker.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/ScoreMarker/ScoreMarkerR.png")));
        greenScoreMarker2.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/ScoreMarker/ScoreMarkerG.png")));
        greenScoreMarker.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/ScoreMarker/ScoreMarkerG.png")));
        yellowScoreMarker2.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/ScoreMarker/ScoreMarkerY.png")));
        yellowScoreMarker.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/ScoreMarker/ScoreMarkerY.png")));
        scoreTrack.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/ScoreTrack.jpg")));
    }

    @Override
    public void update(Observable o, Object arg) {
        MVEvent mvEvent = (MVEvent) arg;
        if(mvEvent.getId() == 5) {
            scoreTrackEventHandler(mvEvent);
        }
    }

}
