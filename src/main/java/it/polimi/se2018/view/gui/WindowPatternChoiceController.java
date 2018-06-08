package it.polimi.se2018.view.gui;

import it.polimi.se2018.events.mvevent.WindowPatternsEvent;
import it.polimi.se2018.events.vcevent.WindowPatternChoiceEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class WindowPatternChoiceController extends Observable implements Observer {

    @FXML private ImageView windowPattern1;
    @FXML private ImageView windowPattern2;
    @FXML private ImageView windowPattern3;
    @FXML private ImageView windowPattern4;
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;
    @FXML private AnchorPane scene;
    private int chosen;

    private void windowPatternSelected(){

        deleteBorderGlow();
        confirmButton.setOpacity(1);
        cancelButton.setOpacity(1);
        confirmButton.setDisable(false);
        cancelButton.setDisable(false);
    }

    private DropShadow setBorderGlow(){
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.BLUE);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        borderGlow.setHeight(45);
        return borderGlow;
    }

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

    @FXML
    private void windowPattern1Chosen(){

        windowPatternSelected();
        windowPattern1.setEffect(setBorderGlow());
        chosen = 1;
    }

    @FXML
    private void windowPattern2Chosen(){

        windowPatternSelected();
        windowPattern2.setEffect(setBorderGlow());
        chosen = 2;
    }

    @FXML
    private void windowPattern3Chosen(){

        windowPatternSelected();
        windowPattern3.setEffect(setBorderGlow());
        chosen = 3;
    }

    @FXML
    private void windowPattern4Chosen(){

        windowPatternSelected();
        windowPattern4.setEffect(setBorderGlow());
        chosen = 4;
    }

    @FXML
    private void confirmButtonPressed(){

        WindowPatternChoiceEvent windowPatternChoiceEvent = new WindowPatternChoiceEvent(String.valueOf(chosen));
        setChanged();
        notifyObservers(windowPatternChoiceEvent);
        scene.setOpacity(0.5);
        scene.setDisable(true);
    }

    @FXML
    private void cancelButtonPressed(){

        deleteBorderGlow();
        confirmButton.setOpacity(0.5);
        cancelButton.setOpacity(0.5);
        confirmButton.setDisable(true);
        cancelButton.setDisable(true);
        chosen = 0;

    }

    public void setWindowPatterns(WindowPatternsEvent windowPatternsEvent){

        List<String> urlStringList = new ArrayList<>();

        for(int i = 0; i<windowPatternsEvent.getWindowPatternsGUI().size(); i++) {

            File fileWindowPattern1 = new File(windowPatternsEvent.getWindowPatternsGUI().get(i));
            String url = null;
            try {
                url = fileWindowPattern1.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            urlStringList.add(url);
        }

        //TODO fare in modo che sia sicuro, nel senso di assicurarsi che la lista sia di 4 elementi
        windowPattern1.setImage(new Image(urlStringList.get(0)));
        windowPattern2.setImage(new Image(urlStringList.get(1)));
        windowPattern3.setImage(new Image(urlStringList.get(2)));
        windowPattern4.setImage(new Image(urlStringList.get(3)));

    }

    @FXML
    public void initialize(){

        windowPattern1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> windowPattern1Chosen());
        windowPattern2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> windowPattern2Chosen());
        windowPattern3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> windowPattern3Chosen());
        windowPattern4.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> windowPattern4Chosen());
    }

    @Override
    public void update(Observable o, Object arg) {

       WindowPatternsEvent windowPatternsEvent = (WindowPatternsEvent) arg;
       this.setWindowPatterns(windowPatternsEvent);
    }
}
