package it.polimi.se2018.view.gui;

import it.polimi.se2018.events.ConnectionEstablishedEvent;
import it.polimi.se2018.events.NewObserverEvent;
import it.polimi.se2018.events.mvevent.MVEvent;
import it.polimi.se2018.network.client.ClientImplementation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;

public class ViewGUI  extends Observable implements Observer{

    private static Stage window;
    private Scene scene;
    private Observable guiControllerObservable;
    private Observer guiControllerObserver;

    public void startGUI(Stage primaryStage){

        window = primaryStage;
        window.setTitle("Sagrada");
        File icon = new File("src/main/Images/Others/sagradaIcon.png");
        String url = null;
        try {
            url = icon.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        window.getIcons().add(new Image(url));
        window.setResizable(false);

        window.setOnCloseRequest(event -> {
            event.consume();
            closeProgram();
        });

        setConnectionChoiceScene();
    }

    public Observable getGuiControllerObservable() {
        return guiControllerObservable;
    }

    private static void closeProgram() {

        if (ConfirmChoiceBox.confirmChoice("Conferma chiusura", "Sei sicuro di volere uscire?"))
            window.close();
    }

    private void setConnectionChoiceScene(){

        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File("src/main/java/it/polimi/se2018/view/gui/ConnectionChoice.fxml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Parent root = null;
        try {
            root = loader.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        guiControllerObservable = loader.getController();

        scene = new Scene(root);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double resolutionX = 1600;
        double resolutionY = 900;
        Scale scale = new Scale(screenSize.width/resolutionX, screenSize.height/resolutionY);
        scale.setPivotX(0);
        scale.setPivotY(0);
        scene.getRoot().getTransforms().setAll(scale);
        window.setMaximized(true);
        window.setScene(scene);
        window.show();
    }

    private void setNicknameChoiceScene(){

        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File("src/main/java/it/polimi/se2018/view/gui/nickname.fxml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Parent root = null;
        try {
            root = loader.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        guiControllerObservable = loader.getController();
        guiControllerObserver = loader.getController();
        this.addObserver(guiControllerObserver);

        scene.setRoot(root);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double resolutionX = 1600;
        double resolutionY = 900;
        Scale scale = new Scale(screenSize.width/resolutionX, screenSize.height/resolutionY);
        scale.setPivotX(0);
        scale.setPivotY(0);
        scene.getRoot().getTransforms().setAll(scale);
    }

    private void setLobbyScene(){

        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File("src/main/java/it/polimi/se2018/view/gui/waitingRoom.fxml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Parent root = null;
        try {
            root = loader.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.deleteObservers();
        guiControllerObservable = loader.getController();
        guiControllerObserver = loader.getController();
        this.addObserver(guiControllerObserver);

        scene.setRoot(root);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double resolutionX = 1600;
        double resolutionY = 900;
        Scale scale = new Scale(screenSize.width/resolutionX, screenSize.height/resolutionY);
        scale.setPivotX(0);
        scale.setPivotY(0);
        scene.getRoot().getTransforms().setAll(scale);
    }

    private void setWindowPatternChoiceScene() {

        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File("src/main/java/it/polimi/se2018/view/gui/windowPatternChoice.fxml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Parent root = null;
        try {
            if (fileInputStream != null) {
                root = loader.load(fileInputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.deleteObservers();
        guiControllerObservable = loader.getController();
        guiControllerObserver = loader.getController();
        this.addObserver(guiControllerObserver);

        scene.setRoot(root);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double resolutionX = 1600;
        double resolutionY = 900;
        Scale scale = new Scale(screenSize.width/resolutionX, screenSize.height/resolutionY);
        scale.setPivotX(0);
        scale.setPivotY(0);
        scene.getRoot().getTransforms().setAll(scale);
    }

    private void setGameScene(){

        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File("src/main/java/it/polimi/se2018/view/gui/Prova.fxml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Parent root = null;
        try {
            root = loader.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.deleteObservers();
        guiControllerObservable = loader.getController();
        guiControllerObserver = loader.getController();
        this.addObserver(guiControllerObserver);

        scene.setRoot(root);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double resolutionX = 1600;
        double resolutionY = 900;
        Scale scale = new Scale(screenSize.width/resolutionX, screenSize.height/resolutionY);
        scale.setPivotX(0);
        scale.setPivotY(0);
        scene.getRoot().getTransforms().setAll(scale);
    }

    private void setEndScreen() {

        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File("src/main/java/it/polimi/se2018/view/gui/EndScreen.fxml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Parent root = null;
        try {
            root = loader.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.deleteObservers();
        guiControllerObservable = loader.getController();
        guiControllerObserver = loader.getController();
        this.addObserver(guiControllerObserver);

        scene.setRoot(root);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double resolutionX = 1600;
        double resolutionY = 900;
        Scale scale = new Scale(screenSize.width/resolutionX, screenSize.height/resolutionY);
        scale.setPivotX(0);
        scale.setPivotY(0);
        scene.getRoot().getTransforms().setAll(scale);
    }

    @Override
    public void update(Observable o, Object arg) {

        if(arg.getClass() == ConnectionEstablishedEvent.class){
            setNicknameChoiceScene();
            setChanged();
            notifyObservers(arg);
        }
        else if(arg.getClass() == NewObserverEvent.class){
            NewObserverEvent newObserverEvent = (NewObserverEvent) arg;
            guiControllerObservable.addObserver(newObserverEvent.getClient());
        }
        //TODO cambiare l'if
        else if(o.getClass() == ClientImplementation.class){
            MVEvent mvEvent = (MVEvent) arg;
            if(mvEvent.getId() == 40){
                setGameScene();
            }
            else if(mvEvent.getId() == 60)
                setEndScreen();
            else if(mvEvent.getId() == 56){
                setWindowPatternChoiceScene();
            }
            else if(mvEvent.getId() == 30){
                setLobbyScene();
            }
            else{
                setChanged();
                notifyObservers(mvEvent);
            }

        }
    }

}
