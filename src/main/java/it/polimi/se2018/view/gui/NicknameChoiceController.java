package it.polimi.se2018.view.gui;

import it.polimi.se2018.events.mvevent.ErrorEvent;
import it.polimi.se2018.events.mvevent.MVEvent;
import it.polimi.se2018.events.networkevent.ConnectionEstablishedEvent;
import it.polimi.se2018.events.vcevent.NicknameEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.Observable;
import java.util.Observer;

/**
 * This class manages the nickname.fxml file
 * @author Framonti
 */
public class NicknameChoiceController extends Observable implements Observer{

    @FXML private Button confirmButton;
    @FXML private Button exitButton;
    @FXML private TextField textField;
    @FXML private Label nickAlreadyTaken1;
    @FXML private Label nickAlreadyTaken2;
    @FXML private Label nickAlreadyTaken3;
    private boolean firstTime;

    /**
     * Manages the behaviour of the textField
     */
    @FXML
    public void nicknameEntered(){

        if(textField.getText().length() < 2){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setContentText("Il Nickname deve avere almeno due caratteri");
            alert.showAndWait();
        }
        else{
            String nickname = textField.getText();
            nickname = Character.toUpperCase(nickname.charAt(0)) + nickname.substring(1);
            NicknameEvent nicknameEvent = new NicknameEvent(nickname, firstTime);
            setChanged();
            notifyObservers(nicknameEvent);
        }
    }

    /**
     * Manages the behaviour of the exitButton
     */
    @FXML
    private void exitFromButton(){

        ViewGUI.closeProgram();
    }

    /**
     * Shows an error from the server
     * @param errorEvent The errorEvent to be shown
     */
    private void showError(ErrorEvent errorEvent){

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setContentText(errorEvent.getMessageToDisplay());
            alert.showAndWait();
        });
    }

    @Override
    public void update(Observable o, Object arg) {

        if(arg.getClass() == ConnectionEstablishedEvent.class){

            ConnectionEstablishedEvent connectionEstablishedEvent = (ConnectionEstablishedEvent) arg;
            firstTime = connectionEstablishedEvent.isFirstTimeNickname();

            if(!connectionEstablishedEvent.isFirstTimeNickname()){
                nickAlreadyTaken1.setOpacity(1);
                nickAlreadyTaken2.setOpacity(1);
                nickAlreadyTaken3.setOpacity(1);
            }
        }
        else if (arg.getClass() == ErrorEvent.class) {
            ErrorEvent errorEvent = (ErrorEvent) arg;
            showError(errorEvent);
        }
    }
}
