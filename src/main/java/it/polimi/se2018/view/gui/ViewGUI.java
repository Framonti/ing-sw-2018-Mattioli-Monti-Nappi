package it.polimi.se2018.view.gui;

import it.polimi.se2018.events.networkevent.ConnectionEstablishedEvent;
import it.polimi.se2018.events.networkevent.NetworkEvent;
import it.polimi.se2018.events.networkevent.NewObserverEvent;
import it.polimi.se2018.events.mvevent.MVEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * This class manages the various scenes of the GUI
 * It's an Observer of the Network Handler and an Observable for the scene controller
 * @author Framonti
 */
public class ViewGUI  extends Observable implements Observer{

    private static Stage window;
    private Scene scene;
    private Observable guiControllerObservable;
    private Observer guiControllerObserver;
    private Map<Integer, Runnable> mvEventMap = new HashMap<>();
    private Map<Integer, Runnable> networkEventMap = new HashMap<>();
    private NetworkEvent networkEventForMap;


    public ViewGUI(){
        initializeMVEventMap();
        initializeNetworkEventMap();
    }

    private void initializeMVEventMap(){
        mvEventMap.put(40, this::setGameScene);
        mvEventMap.put(60, this::setEndScreen);
        mvEventMap.put(56, this::setWindowPatternChoiceScene);
        mvEventMap.put(30, this::setLobbyScene);
    }

    private void initializeNetworkEventMap(){
        networkEventMap.put(25, () -> connectionEstablishedHandler(networkEventForMap));
        networkEventMap.put(90, () -> newObserverEventHandler(networkEventForMap));
        networkEventMap.put(80, () -> passTheEventToObservers(networkEventForMap));
        networkEventMap.put(70, () -> passTheEventToObservers(networkEventForMap));
    }

    /**
     * Static method usable in all the GUI classes;
     * It gives an url given a resource path
     * @param path The path of the resource
     * @return The url linked to the path given
     */
    public static String getUrlFromPath(String path){
        File icon = new File(path);
        String url = null;
        try {
            url = icon.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Sets a Scale object based on the screen dimension of an user
     * @return A Scale object initialized with the screen resolution of an user
     */
    private Scale setScreenProportion(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double resolutionX = 1600;
        double resolutionY = 900;
        Scale scale = new Scale(screenSize.width/resolutionX, screenSize.height/resolutionY);
        scale.setPivotX(0);
        scale.setPivotY(0);
        return scale;
    }

    /**
     * Loads a fxml file into a fileInputStream
     * @param path The path of the fxml file to load
     * @return A FileInputStream associated with the fxml file
     */
    private FileInputStream getFXMLFileFromPath(String path){
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileInputStream;
    }

    /**
     * Gets the guiControllerObservable
     * @return the guiControllerObservable
     */
    public Observable getGuiControllerObservable() {
        return guiControllerObservable;
    }

    /**
     * Properly closes the program
     */
    public static void closeProgram() {

        if (ConfirmChoiceBox.confirmChoice("Conferma chiusura", "Sei sicuro di volere uscire?")) {
            window.close();
            System.exit(0);
        }
    }

    /**
     * Starts the whole GUI;
     * It creates the Stage and ask to sets the first Scene
     * @param primaryStage
     */
    public void startGUI(Stage primaryStage){

        window = primaryStage;
        window.setTitle("Sagrada");

        window.getIcons().add(new Image(getUrlFromPath("src/main/Images/Others/sagradaIcon.png")));
        window.setResizable(false);

        window.setOnCloseRequest(event -> {
            event.consume();
            closeProgram();
        });
        setConnectionChoiceScene();
    }

    /**
     * Sets the first scene
     */
    private void setConnectionChoiceScene(){

        FXMLLoader loader = new FXMLLoader();

        FileInputStream fileInputStream = getFXMLFileFromPath("src/main/java/it/polimi/se2018/view/gui/ConnectionChoice.fxml");

        Parent root = null;
        try {
            root = loader.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        guiControllerObserver = loader.getController();
        guiControllerObservable = loader.getController();
        this.addObserver(guiControllerObserver);

        scene = new Scene(root);

        scene.getRoot().getTransforms().setAll(setScreenProportion());
        scene.setFill(Paint.valueOf("#838383"));
        window.setMaximized(true);
        window.setScene(scene);
        window.show();
    }

    /**
     * Sets the second scene
     */
    private void setNicknameChoiceScene(){

        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = getFXMLFileFromPath("src/main/java/it/polimi/se2018/view/gui/nickname.fxml");
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

        scene.setFill(Paint.valueOf("#838383"));
        scene.getRoot().getTransforms().setAll(setScreenProportion());
    }

    /**
     * Sets the third scene
     */
    private void setLobbyScene(){

        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = getFXMLFileFromPath("src/main/java/it/polimi/se2018/view/gui/waitingRoom.fxml");
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

        scene.setFill(Paint.valueOf("#f4f4f4"));
        scene.getRoot().getTransforms().setAll(setScreenProportion());
    }

    /**
     * Sets the forth scene
     */
    private void setWindowPatternChoiceScene() {

        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = getFXMLFileFromPath("src/main/java/it/polimi/se2018/view/gui/windowPatternChoice.fxml");
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

        scene.getRoot().getTransforms().setAll(setScreenProportion());
    }

    /**
     * Sets the fifth and main scene
     */
    private void setGameScene(){

        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = getFXMLFileFromPath("src/main/java/it/polimi/se2018/view/gui/Prova.fxml");
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
        scene.getRoot().getTransforms().setAll(setScreenProportion());
    }

    /**
     * Sets the sixth and final scene
     */
    private void setEndScreen() {

        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = getFXMLFileFromPath("src/main/java/it/polimi/se2018/view/gui/EndScreen.fxml");

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
        scene.setFill(Paint.valueOf("#efefef"));
        scene.getRoot().getTransforms().setAll(setScreenProportion());
    }

    private void connectionEstablishedHandler(NetworkEvent networkEvent){
        ConnectionEstablishedEvent connectionEstablishedEvent = (ConnectionEstablishedEvent) networkEvent;
        if (connectionEstablishedEvent.isFirstTimeNickname()) {
            setNicknameChoiceScene();
        }
        setChanged();
        notifyObservers(networkEvent);
    }

    private void newObserverEventHandler(NetworkEvent networkEvent){

        NewObserverEvent newObserverEvent = (NewObserverEvent) networkEvent;
        guiControllerObservable.addObserver(newObserverEvent.getClient());
    }

    private void passTheEventToObservers(NetworkEvent networkEvent){
        setChanged();
        notifyObservers(networkEvent);
    }

    @Override
    public void update(Observable o, Object arg) {

        if(arg instanceof NetworkEvent) {
            networkEventForMap = (NetworkEvent) arg;
            int id = networkEventForMap.getId();
            networkEventMap.get(id).run();
        }
        else {
            MVEvent mvEvent = (MVEvent) arg;
            int id = mvEvent.getId();
            if(id == 40|| id == 60 || id == 56 || id == 30){
                mvEventMap.get(id).run();
            }
            else{
                setChanged();
                notifyObservers(mvEvent);
            }
        }
    }

}
