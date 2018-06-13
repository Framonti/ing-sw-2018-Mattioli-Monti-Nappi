package it.polimi.se2018.view.gui;

import it.polimi.se2018.events.mvevent.ClientAlreadyConnectedEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.*;

public class WaitingRoomController extends Observable implements Observer {

    @FXML private ImageView header;
    @FXML private ImageView leftColumn1;
    @FXML private ImageView leftColumn2;
    @FXML private ImageView rightColumn1;
    @FXML private ImageView rightColumn2;
    @FXML private TextArea notificationTextArea;
    @FXML private Label label1;
    @FXML private Label label2;
    @FXML private Label label3;

    @FXML
 /*   private void singlePlayerGame(){

        SinglePlayerEvent singlePlayerEvent = new SinglePlayerEvent(true);
        setChanged();
        notifyObservers(singlePlayerEvent);
    }*/

    private void setClientLabel(List<String> clientConnectedStringList){
        label1.setText(clientConnectedStringList.get(0));
        if(clientConnectedStringList.size() > 1) {
            label2.setText(clientConnectedStringList.get(1));
            if (clientConnectedStringList.size() > 2)
                label3.setText(clientConnectedStringList.get(2));
        }
    }

  /*  private void updateNotificationTextArea(){
        notificationTextArea.setText("Benvenuto su Sagrada. La tua ");
    }*/

    @Override
    public void update(Observable o, Object arg) {

        ClientAlreadyConnectedEvent clientAlreadyConnectedEvent = (ClientAlreadyConnectedEvent) arg;
        List<String> clientAlreadyConnectedList = clientAlreadyConnectedEvent.getClientConnected();
        Platform.runLater(() -> {
            setClientLabel(clientAlreadyConnectedList);
        });
    }

    public void initialize(){
        header.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/sagradaHeader.jpg")));
        rightColumn1.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/rightColumn.png")));
        rightColumn2.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/rightColumn.png")));
        leftColumn1.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/leftColumn.png")));
        leftColumn2.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/leftColumn.png")));

    }
}
