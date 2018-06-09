package it.polimi.se2018.view.gui;

import it.polimi.se2018.events.ConnectionEstablishedEvent;
import it.polimi.se2018.events.vcevent.NicknameEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.Observable;
import java.util.Observer;

/**
 * This class manages the nickname.fxml file
 */
public class NicknameChoiceController extends Observable implements Observer{

    @FXML private Button confirmButton;
    @FXML private Button exitButton;
    @FXML private TextField textField;

    /**
     * Manages the behaviour of the textField
     */
    @FXML
    public void nicknameEntered(){

        if(!textField.getText().equals("")){
            NicknameEvent nicknameEvent = new NicknameEvent(textField.getText());
            setChanged();
            notifyObservers(nicknameEvent);
        }
    }

    /**
     * Manages the behaviour of the exitButton
     */
    @FXML
    private void exitFromButton(){

        GUIManager.closeProgram();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg.getClass() == ConnectionEstablishedEvent.class){

            ConnectionEstablishedEvent connectionEstablishedEvent = (ConnectionEstablishedEvent) arg;
            //TODO mettere una label
            if(!connectionEstablishedEvent.isFirstTimeNickname()){
                confirmButton.setOpacity(0.2);
            }
        }
    }
}
