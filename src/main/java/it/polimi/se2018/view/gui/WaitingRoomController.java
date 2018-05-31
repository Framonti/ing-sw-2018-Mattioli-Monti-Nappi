package it.polimi.se2018.view.gui;

import it.polimi.se2018.events.mvevent.ClientAlreadyConnectedEvent;
import it.polimi.se2018.events.mvevent.MVEvent;
import it.polimi.se2018.events.vcevent.SinglePlayerEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.*;

public class WaitingRoomController extends Observable implements Observer{

    @FXML private TextArea notificationTextArea;
    @FXML private Button singlePlayerButton;
    @FXML private Label label1;
    @FXML private Label label2;
    @FXML private Label label3;
    @FXML private Label label4; //Serve?
    private Map<Integer, Runnable> eventHandler = new HashMap();
    private MVEvent mvEvent;

    @FXML
    private void singlePlayerGame(){

        SinglePlayerEvent singlePlayerEvent = new SinglePlayerEvent(true);
        setChanged();
        notifyObservers(singlePlayerEvent);
    }

    @Override
    public void update(Observable o, Object arg) {

        this.mvEvent = (MVEvent) arg;
        eventHandler.get(mvEvent.getId()).run();
    }

    private void showOtherClientsAlreadyConnected(){

        ClientAlreadyConnectedEvent clientAlreadyConnectedEvent = (ClientAlreadyConnectedEvent) mvEvent;
        List<String> clientAlreadyConnectedList = clientAlreadyConnectedEvent.getClientConnected();
        label1.setText(clientAlreadyConnectedList.get(0));
        if(clientAlreadyConnectedList.size() > 1){
            label2.setText(clientAlreadyConnectedList.get(1));
            if (clientAlreadyConnectedList.size() > 2){
                label3.setText(clientAlreadyConnectedList.get(2));
                //Parte subito?
                //if (clientAlreadyConnectedList.size() > 3){
                  //  cl
                //}
            }
        }

    }

    private void createMap(){

        eventHandler.put(14, this::showOtherClientsAlreadyConnected);
    }

    public void initialize(){
        createMap();
    }
}
