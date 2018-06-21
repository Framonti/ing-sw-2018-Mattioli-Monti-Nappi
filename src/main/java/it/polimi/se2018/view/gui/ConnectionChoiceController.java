package it.polimi.se2018.view.gui;

import it.polimi.se2018.events.networkevent.ConnectionChoiceEvent;
import it.polimi.se2018.events.networkevent.ConnectionRefusedEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.Observable;
import java.util.Observer;

/**
 * This class manages the ConnectionChoice.fxml file
 * @author Framonti
 */
public class ConnectionChoiceController extends Observable implements Observer{

    @FXML private Button rmiButton;
    @FXML private Button socketButton;
    @FXML private TextField ipServer;
    @FXML private Label serverNotFound1;
    @FXML private Label serverNotFound2;
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

    @Override
    public void update(Observable o, Object arg) {

        if(arg.getClass() == ConnectionRefusedEvent.class){
            serverNotFound1.setOpacity(1);
            serverNotFound2.setOpacity(1);
            socketButton.setOpacity(0.5);
            rmiButton.setOpacity(0.5);
            socketButton.setDisable(true);
            rmiButton.setDisable(true);
        }
    }
}
