package it.polimi.se2018.view.gui;

import it.polimi.se2018.events.mvevent.WindowPatternsEvent;
import it.polimi.se2018.events.vcevent.WindowPatternChoiceEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.paint.Color;

/**
 * This controller class manages the WindowPatternChoice.fxml file
 * @author Framonti
 */
public class WindowPatternChoiceController extends Observable implements Observer {

    @FXML private ImageView windowPattern1;
    @FXML private ImageView windowPattern2;
    @FXML private ImageView windowPattern3;
    @FXML private ImageView windowPattern4;
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;
    @FXML private ImageView privateObjectiveCard;
    @FXML private AnchorPane scene;
    @FXML private Label waitYourTurn1;
    @FXML private Label waitYourTurn2;
    private int chosen;

    /**
     * Sets visual effects when a windowPattern is selected and enable the Confirm and Cancel buttons
     */
    private void windowPatternSelected(){

        deleteBorderGlow();
        confirmButton.setOpacity(1);
        cancelButton.setOpacity(1);
        confirmButton.setDisable(false);
        cancelButton.setDisable(false);
    }

    /**
     * Create a DropShadow effect
     * @return A DropShadow effect
     */
    private DropShadow setBorderGlow(){
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.BLUE);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        borderGlow.setHeight(45);
        return borderGlow;
    }

    /**
     * Deletes an already existing DropShadow effect
     */
    private void deleteBorderGlow(){

        switch (chosen){
            case 1 : windowPattern1.setEffect(null);
                break;
            case 2 : windowPattern2.setEffect(null);
                break;
            case 3 : windowPattern3.setEffect(null);
                break;
            case 4 : windowPattern4.setEffect(null);
                break;
            default : break;
        }
    }

    /**
     * Sets the first windowPattern as selected
     */
    @FXML
    private void windowPattern1Chosen(){

        windowPatternSelected();
        windowPattern1.setEffect(setBorderGlow());
        chosen = 1;
    }

    /**
     * Sets the second windowPattern as selected
     */
    @FXML
    private void windowPattern2Chosen(){

        windowPatternSelected();
        windowPattern2.setEffect(setBorderGlow());
        chosen = 2;
    }

    /**
     * Sets the third windowPattern as selected
     */
    @FXML
    private void windowPattern3Chosen(){

        windowPatternSelected();
        windowPattern3.setEffect(setBorderGlow());
        chosen = 3;
    }

    /**
     * Sets the forth windowPattern as selected
     */
    @FXML
    private void windowPattern4Chosen(){

        windowPatternSelected();
        windowPattern4.setEffect(setBorderGlow());
        chosen = 4;
    }

    /**
     * Manages the behaviour of the confirmButton
     * It creates an WindowPatternChoiceEvent when pressed
     */
    @FXML
    private void confirmButtonPressed(){

        WindowPatternChoiceEvent windowPatternChoiceEvent = new WindowPatternChoiceEvent(String.valueOf(chosen));
        setChanged();
        notifyObservers(windowPatternChoiceEvent);
        scene.setOpacity(0.5);
        scene.setDisable(true);
    }

    /**
     * Manages the behaviour of the cancelButton
     * It deselected everything and disable the buttons
     */
    @FXML
    private void cancelButtonPressed(){

        deleteBorderGlow();
        confirmButton.setOpacity(0.5);
        cancelButton.setOpacity(0.5);
        confirmButton.setDisable(true);
        cancelButton.setDisable(true);
        chosen = 0;

    }

    /**
     * Sets on screen the images of the possible WindowPatterns, from which an user have to choose
     * @param windowPatternsEvent The windowPatternEvent sent by the server
     */
    public void setWindowPatterns(WindowPatternsEvent windowPatternsEvent){

        List<String> urlStringList = new ArrayList<>();

        for(int i = 0; i<windowPatternsEvent.getWindowPatternsGUI().size(); i++)
            urlStringList.add(ViewGUI.getUrlFromPath(windowPatternsEvent.getWindowPatternsGUI().get(i)));

        windowPattern1.setImage(new Image(urlStringList.get(0)));
        windowPattern2.setImage(new Image(urlStringList.get(1)));
        windowPattern3.setImage(new Image(urlStringList.get(2)));
        windowPattern4.setImage(new Image(urlStringList.get(3)));

    }

    public void setBackground(){

        Image background = new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/background2.jpg"));
        BackgroundImage myBGI = new BackgroundImage(background, null, null,null, null);
        scene.setBackground(new Background(myBGI));
    }

    @Override
    public void update(Observable o, Object arg) {

       WindowPatternsEvent windowPatternsEvent = (WindowPatternsEvent) arg;
       privateObjectiveCard.setImage(new Image(ViewGUI.getUrlFromPath(windowPatternsEvent.getPrivateObjectiveCardPath())));
       setWindowPatterns(windowPatternsEvent);
       waitYourTurn1.setVisible(false);
       waitYourTurn2.setVisible(false);
       windowPattern1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> windowPattern1Chosen());
       windowPattern2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> windowPattern2Chosen());
       windowPattern3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> windowPattern3Chosen());
       windowPattern4.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> windowPattern4Chosen());
    }

    /**
     * Initializes the scene, by setting its background
     */
    public void initialize(){

        /*Image background = new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/background2.jpg"));
        BackgroundImage myBGI = new BackgroundImage(background, null, null,null, null);
        scene.setBackground(new Background(myBGI));*/
    }
}
