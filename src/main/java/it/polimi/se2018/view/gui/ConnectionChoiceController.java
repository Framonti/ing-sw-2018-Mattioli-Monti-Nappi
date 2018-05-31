package it.polimi.se2018.view.gui;

import it.polimi.se2018.events.vcevent.ConnectionChoiceEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.Observable;


public class ConnectionChoiceController extends Observable{

    @FXML private Button rmiButton;
    @FXML private Button socketButton;

    //private int choice;

    @FXML
    private void rmiButtonAction(){

        try {
            GUIManager.setLobbyScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ConnectionChoiceEvent connectionChoiceEvent = new ConnectionChoiceEvent(1);
        setChanged();
        notifyObservers(connectionChoiceEvent);

    }

    @FXML
    private void socketButtonAction(){

        try {
            GUIManager.setLobbyScene();
        } catch (IOException e) {

        }
        ConnectionChoiceEvent connectionChoiceEvent = new ConnectionChoiceEvent(2);
        setChanged();
        notifyObservers(connectionChoiceEvent);

    }

}
