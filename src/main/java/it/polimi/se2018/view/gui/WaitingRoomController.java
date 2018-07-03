package it.polimi.se2018.view.gui;

import it.polimi.se2018.events.mvevent.ClientAlreadyConnectedEvent;
import it.polimi.se2018.events.vcevent.SinglePlayerEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    @FXML private ChoiceBox<Integer> difficultySinglePlayer;
    @FXML private Button singlePlayerButton;

    /**
     * Manages the behaviour of the singlePlayerButton
     * When pressed, the button asks the server to start a new Single Player game
     */
    @FXML
    private void singlePlayerButtonAction(){

        setChanged();
        notifyObservers(new SinglePlayerEvent(difficultySinglePlayer.getValue()));
    }

    /**
     * Sets the names of the client connected in the labels
     * @param clientConnectedStringList A List containing all the Names of the connected clients
     */
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

    /**
     * Disable the option to start a single player game
     */
    private void deactivateSinglePlayerOption(){
        difficultySinglePlayer.setDisable(true);
        singlePlayerButton.setDisable(true);
        singlePlayerButton.setStyle("-fx-background-color:#d80909");
    }

    @Override
    public void update(Observable o, Object arg) {

        ClientAlreadyConnectedEvent clientAlreadyConnectedEvent = (ClientAlreadyConnectedEvent) arg;
        List<String> clientAlreadyConnectedList = clientAlreadyConnectedEvent.getClientConnected();
        Platform.runLater(() -> setClientLabel(clientAlreadyConnectedList));
        if(clientAlreadyConnectedEvent.getClientConnected().size() > 1)
            Platform.runLater(this::deactivateSinglePlayerOption);
    }

    /**
     * Initializes the scene
     */
    public void initialize(){

        header.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/sagradaHeader.jpg")));
        rightColumn1.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/rightColumn.png")));
        rightColumn2.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/rightColumn.png")));
        leftColumn1.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/leftColumn.png")));
        leftColumn2.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/leftColumn.png")));
        difficultySinglePlayer.getItems().addAll(1,2,3,4,5);
        difficultySinglePlayer.setValue(1);
    }
}
