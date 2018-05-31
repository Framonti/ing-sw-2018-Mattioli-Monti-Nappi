package it.polimi.se2018.view.gui;

import it.polimi.se2018.events.mvevent.MVEvent;
import it.polimi.se2018.events.mvevent.ScoreTrackEvent;
import it.polimi.se2018.events.mvevent.WinnerEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.net.MalformedURLException;
import java.util.*;

public class EndScreenController extends Observable implements Observer{
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
    private ImageView yellowScoreMarker = new ImageView();
    private ImageView yellowScoreMarker2 = new ImageView();
    private ImageView redScoreMarker = new ImageView();
    private ImageView redScoreMarker2 = new ImageView();
    private ImageView blueScoreMarker = new ImageView();
    private ImageView blueScoreMarker2 = new ImageView();
    private ImageView greenScoreMarker = new ImageView();
    private ImageView greenScoreMarker2 = new ImageView();


    @FXML
    private void exitButtonAction(){

        GUIManager.closeProgram();
    }


    //TODO implement this method
    @FXML
    private void playAgainButtonAction(){

        List<Integer> list = new ArrayList<>();
        list.add(56);
        list.add(46);
        list.add(24);
        list.add(18);
        placeScoreMarkersOnScene(list);

    }

    private void setImageOnScene(ImageView marker, int X, int Y){

        marker.setX(X);
        marker.setY(Y);
        scene.getChildren().add(marker);
    }

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

    private void setRankingLabels(ScoreTrackEvent scoreTrackEvent){

        firstPlaceLabel.setText(scoreTrackEvent.getPlayersNames().get(0));
        firstPlacePoints.setText(String.valueOf(scoreTrackEvent.getScores().get(0)) + " pts");
        secondPlaceLabel.setText(scoreTrackEvent.getPlayersNames().get(1));
        secondPlacePoints.setText(String.valueOf(scoreTrackEvent.getScores().get(1)) + " pts");
        if(scoreTrackEvent.getPlayersNames().size() > 2){
            thirdPlaceLabel.setText(scoreTrackEvent.getPlayersNames().get(2));
            thirdPlacePoints.setText(String.valueOf(scoreTrackEvent.getScores().get(2)) + " pts");
        }
        if(scoreTrackEvent.getPlayersNames().size() > 3){
            forthPlaceLabel.setText(scoreTrackEvent.getPlayersNames().get(3));
            forthPlacePoints.setText(String.valueOf(scoreTrackEvent.getScores().get(3)) + " pts");
        }

    }

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
        }
        if(scores.size() > 3){
            setScoreMarkerDecine(yellowScoreMarker, scores.get(3));
            setScoreMarkerUnit(yellowScoreMarker2, scores.get(3));
            yellowScoreMarker.toFront();
            yellowScoreMarker2.toFront();
        }


    }

    private void scoreTrackEventHandler(MVEvent mvEvent){

        ScoreTrackEvent scoreTrackEvent = (ScoreTrackEvent) mvEvent;
        sortPlayers(scoreTrackEvent);
        setRankingLabels(scoreTrackEvent);
        placeScoreMarkersOnScene(scoreTrackEvent.getScores());
    }

    private void sortPlayers(ScoreTrackEvent scoreTrackEvent){

        int n = scoreTrackEvent.getPlayersNames().size();
        int i;
        while(n!= 0){
            i = 0;
            for(int j = 1; j < n; j++) {
                if(scoreTrackEvent.getScores().get(j-1) < scoreTrackEvent.getScores().get(j)){
                    Collections.swap(scoreTrackEvent.getScores(), j-1, j);
                    Collections.swap(scoreTrackEvent.getPlayersNames(), j-1, j);
                    i = j;
                }
            }
            n = i;
        }
    }

    private void winnerEventHandler(MVEvent mvEvent){
        WinnerEvent winnerEvent = (WinnerEvent) mvEvent;
        winnerLabel.setText(winnerEvent.getWinner());
    }

    public void initialize(){

        imageLoader("src/main/Images/ScoreMarker/ScoreMarkerB.png", blueScoreMarker);
        imageLoader("src/main/Images/ScoreMarker/ScoreMarkerB.png", blueScoreMarker2);
        imageLoader("src/main/Images/ScoreMarker/ScoreMarkerR.png", redScoreMarker);
        imageLoader("src/main/Images/ScoreMarker/ScoreMarkerR.png", redScoreMarker2);
        imageLoader("src/main/Images/ScoreMarker/ScoreMarkerY.png", yellowScoreMarker);
        imageLoader("src/main/Images/ScoreMarker/ScoreMarkerY.png", yellowScoreMarker2);
        imageLoader("src/main/Images/ScoreMarker/ScoreMarkerG.png", greenScoreMarker);
        imageLoader("src/main/Images/ScoreMarker/ScoreMarkerG.png", greenScoreMarker2);
    }

    private void imageLoader(String path, ImageView imageView){
        File imageFile = new File(path);
        String url = null;
        try {
            url = imageFile.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        imageView.setImage(new Image(url));
    }

    @Override
    public void update(Observable o, Object arg) {
        MVEvent mvEvent = (MVEvent) arg;
        if(arg.getClass() == ScoreTrackEvent.class)
            scoreTrackEventHandler(mvEvent);
        else winnerEventHandler(mvEvent);
    }


}
