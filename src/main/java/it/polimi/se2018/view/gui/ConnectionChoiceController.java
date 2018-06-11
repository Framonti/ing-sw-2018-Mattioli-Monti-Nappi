package it.polimi.se2018.view.gui;

import it.polimi.se2018.events.ConnectionChoiceEvent;
import it.polimi.se2018.events.ConnectionEstablishedEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;

/**
 * This class manages the ConnectionChoice.fxml file
 * @author Framonti
 */
public class ConnectionChoiceController extends Observable{

    @FXML private Button rmiButton;
    @FXML private Button socketButton;
    @FXML private TextField ipServer;
    private String ipAddressString;

    /**
     * Manages the behaviour of the rmiButton
     * If pressed, the rmiButton generates a connectionChoiceEvent and notifies the Client
     */
    @FXML
    private void rmiButtonAction(){

        ConnectionChoiceEvent connectionChoiceEvent = new ConnectionChoiceEvent(1, ipAddressString);
        setChanged();
        notifyObservers(connectionChoiceEvent);
    }

    /**
     * Manages the behaviour of the socketButton
     * If pressed, the socketButton generates a connectionChoiceEvent and notifies the Client
     */
    @FXML
    private void socketButtonAction(){

        ConnectionChoiceEvent connectionChoiceEvent = new ConnectionChoiceEvent(2, ipAddressString);
        setChanged();
        notifyObservers(connectionChoiceEvent);
    }

    /**
     * Manges the behaviour of the ipServer TextField
     * On action, it saves the value inserted and
     */
    @FXML
    private void getIpServer(){

        if(!ipServer.getText().equals("")){
            ipAddressString = ipServer.getText();
            socketButton.setOpacity(1);
            rmiButton.setOpacity(1);
            socketButton.setDisable(false);
            rmiButton.setDisable(false);
        }
    }

}
