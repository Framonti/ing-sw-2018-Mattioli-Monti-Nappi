package it.polimi.se2018.view.gui;

import it.polimi.se2018.events.vcevent.NicknameEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.Observable;

/**
 * This class manages the nickname.fxml file
 */
public class NicknameChoiceController extends Observable{

    @FXML private Button confirmButton;
    @FXML private Button exitButton;
    @FXML private TextField textField;

    /**
     * Manages the behaviour of the textField
     */
    @FXML
    public void nicknameEntered(){

        if(!textField.getText().equals("")){
            try{
                GUIManager.setConnectionChoiceScene();
                NicknameEvent nicknameEvent = new NicknameEvent(textField.getText());
                setChanged();
                notifyObservers(nicknameEvent);
            }catch (IOException e){
                System.out.println("errore");
            }
        }
    }

    /**
     * Manages the behaviour of the exitButton
     */
    @FXML
    private void exitFromButton(){

        GUIManager.closeProgram();
    }

}
