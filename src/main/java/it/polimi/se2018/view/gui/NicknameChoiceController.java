package it.polimi.se2018.view.gui;

import it.polimi.se2018.events.vcevent.NicknameEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.Observable;

public class NicknameChoiceController extends Observable{

    @FXML private Button confirmButton;
    @FXML private Button exitButton;
    @FXML private TextField textField;

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


    @FXML
    private void exitFromButton(){

        GUIManager.closeProgram();
    }

}
