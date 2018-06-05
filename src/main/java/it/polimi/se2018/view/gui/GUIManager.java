package it.polimi.se2018.view.gui;

 import it.polimi.se2018.controller.GameController;
 import javafx.fxml.FXMLLoader;
 import javafx.scene.Parent;
 import javafx.scene.Scene;
 import javafx.scene.transform.Scale;
 import javafx.stage.Stage;
 import javafx.scene.image.Image;
 import java.awt.*;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileNotFoundException;
 import java.io.IOException;
 import java.net.MalformedURLException;

/**
 * This class sets the Stage for the GUI and manages all the various Scenes
 */
public class GUIManager{

    private static Stage window;
    private static Scene scene;
    private static NicknameChoiceController nicknameChoiceController;
    private static ConnectionChoiceController connectionChoiceController;
    private static WaitingRoomController waitingRoomController;
    private static WindowPatternChoiceController windowPatternChoiceController;
    private static GameController gameController;
    private static EndScreenController endScreenController;



    /**
     * Gets the controller associated to the nickname.fxml file
     * @return The controller for the nickname.fxml file
     */
    public static NicknameChoiceController getNicknameChoiceController() {
        return nicknameChoiceController;
    }

    /**
     * Gets the controller associated to the ConnectionChoice.fxml file
     * @return The controller for the ConnectionChoice.fxml file
     */
    public static ConnectionChoiceController getConnectionChoiceController() {
        return connectionChoiceController;
    }

    /**
     * Gets the controller associated to the waitingRoom.fxml file
     * @return The controller for the waitingRoom.fxml file
     */
    public static WaitingRoomController getWaitingRoomController() {
        return waitingRoomController;
    }

    /**
     * Gets the controller associated to the windowPatternChoice.fxml file
     * @return The controller for the windowPatternChoice.fxml file
     */
    public static WindowPatternChoiceController getWindowPatternChoiceController() {
        return windowPatternChoiceController;
    }

    public static GameController getGameController(){
        return gameController;
    }

    /**
     * Gets the controller associated to the EndScreen.fxml file
     * @return The controller for the EndScreen.fxml file
     */
    public static EndScreenController getEndScreenController() {
        return endScreenController;
    }

    /**
     * Gets the Stage of the GUI
     * @return The Stage of the GUI
     */
    public static Stage getWindow(){
        return window;
    }

    /**
     * Create the Stage of the GUI and sets its parameters
     * @param primaryStage
     */
    public static void startGUI(Stage primaryStage){

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

        GUIManager.setConnectionChoiceScene();
    }


    /**
     * Closes the program, after a confirmation from the user
     */
    public static void closeProgram() {

        if (ConfirmChoiceBox.confirmChoice("Conferma chiusura", "Sei sicuro di volere uscire?"))
            window.close();
    }

    /**
     * Sets the first Scene
     */
    public static void setNicknameChoiceScene(){

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
        nicknameChoiceController = loader.getController();
        scene.setRoot(root);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double resolutionX = 1600;
        double resolutionY = 900;
        Scale scale = new Scale(screenSize.width/resolutionX, screenSize.height/resolutionY);
        scale.setPivotX(0);
        scale.setPivotY(0);
        scene.getRoot().getTransforms().setAll(scale);
    }

    /**
     * Sets the second Scene
     */
    public static void setConnectionChoiceScene(){

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

        connectionChoiceController = loader.getController();
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

    /**
     * Sets the third Scene
     */
    public static void setLobbyScene(){

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
        waitingRoomController = loader.getController();
        scene.setRoot(root);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double resolutionX = 1600;
        double resolutionY = 900;
        Scale scale = new Scale(screenSize.width/resolutionX, screenSize.height/resolutionY);
        scale.setPivotX(0);
        scale.setPivotY(0);
        scene.getRoot().getTransforms().setAll(scale);
    }

    /**
     * Sets the forth Scene
     */
    public static void setWindowPatternChoiceScene() {

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

        windowPatternChoiceController = loader.getController();
        scene.setRoot(root);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double resolutionX = 1600;
        double resolutionY = 900;
        Scale scale = new Scale(screenSize.width/resolutionX, screenSize.height/resolutionY);
        scale.setPivotX(0);
        scale.setPivotY(0);
        scene.getRoot().getTransforms().setAll(scale);
    }

    public static void setGameScene(){

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

        gameController = loader.getController();

        scene.setRoot(root);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double resolutionX = 1600;
        double resolutionY = 900;
        Scale scale = new Scale(screenSize.width/resolutionX, screenSize.height/resolutionY);
        scale.setPivotX(0);
        scale.setPivotY(0);
        scene.getRoot().getTransforms().setAll(scale);
    }

    /**
     * Sets the sixth Scene
     */
    public static void setEndScreen() {

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

        endScreenController = loader.getController();

        scene.setRoot(root);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double resolutionX = 1600;
        double resolutionY = 900;
        Scale scale = new Scale(screenSize.width/resolutionX, screenSize.height/resolutionY);
        scale.setPivotX(0);
        scale.setPivotY(0);
        scene.getRoot().getTransforms().setAll(scale);
    }

}