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


    private void setClientLabel(List<String> clientConnectedStringList) {
        List<Label> labels = new ArrayList<>();
        labels.add(label1);
        labels.add(label2);
        labels.add(label3);
        for (int i = 0; i < labels.size(); i++) {
            if (i < clientConnectedStringList.size())
                labels.get(i).setText(clientConnectedStringList.get(i));
            else
                labels.get(i).setText("");
        }
    }

  /*  private void updateNotificationTextArea(){
        notificationTextArea.setText("Benvenuto su Sagrada. La tua ");
    }*/

    @Override
    public void update(Observable o, Object arg) {

        ClientAlreadyConnectedEvent clientAlreadyConnectedEvent = (ClientAlreadyConnectedEvent) arg;
        List<String> clientAlreadyConnectedList = clientAlreadyConnectedEvent.getClientConnected();
        Platform.runLater(() -> setClientLabel(clientAlreadyConnectedList));
    }

    public void initialize(){

        header.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/sagradaHeader.jpg")));
        rightColumn1.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/rightColumn.png")));
        rightColumn2.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/rightColumn.png")));
        leftColumn1.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/leftColumn.png")));
        leftColumn2.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/leftColumn.png")));
    }
}
