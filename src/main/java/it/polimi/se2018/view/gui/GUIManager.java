package it.polimi.se2018.view.gui;

 import it.polimi.se2018.events.mvevent.WindowPatternsEvent;
 import javafx.fxml.FXMLLoader;
 import javafx.scene.Parent;
 import javafx.scene.Scene;
 import javafx.scene.transform.Scale;
 import javafx.stage.Stage;
 import javafx.scene.image.Image;
 import java.awt.*;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.IOException;
 import java.net.MalformedURLException;
 import java.util.*;
 import java.util.List;

/**
 * This class sets the Stage for the GUI and manages all the various Scenes
 */
public class GUIManager{

    private static Stage window;
    private static NicknameChoiceController nicknameChoiceController;
    private static ConnectionChoiceController connectionChoiceController;
    private static WaitingRoomController waitingRoomController;
    private static WindowPatternChoiceController windowPatternChoiceController;
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

        try {
            GUIManager.setNicknameChoiceScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     * @throws IOException
     */
    private static void setNicknameChoiceScene() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = new FileInputStream(new File("src/main/java/it/polimi/se2018/view/gui/nickname.fxml"));
        Parent root = loader.load(fileInputStream);
        nicknameChoiceController = loader.getController();
        window.setScene(new Scene(root));
        window.show();
    }

    /**
     * Sets the second Scene
     * @throws IOException
     */
    public static void setConnectionChoiceScene() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = new FileInputStream(new File("src/main/java/it/polimi/se2018/view/gui/ConnectionChoice.fxml"));
        Parent root = loader.load(fileInputStream);
        connectionChoiceController = loader.getController();
        window.setScene(new Scene(root));
        window.show();
    }

    /**
     * Sets the third Scene
     * @throws IOException
     */
    public static void setLobbyScene() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = new FileInputStream(new File("src/main/java/it/polimi/se2018/view/gui/waitingRoom.fxml"));
        Parent root = loader.load(fileInputStream);
        waitingRoomController = loader.getController();
        window.setScene(new Scene(root));
        window.show();
    }

    /**
     * Sets the forth Scene
     * @throws IOException
     */
    public static void setWindowPatternChoiceScene() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = new FileInputStream(new File("src/main/java/it/polimi/se2018/view/gui/windowPatternChoice.fxml"));

        Parent root = loader.load(fileInputStream);
        Scene windowPatternChoiceScene = new Scene(root);

        windowPatternChoiceController = loader.getController();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double resolutionX = 1600;
        double resolutionY = 900;
        Scale scale = new Scale(screenSize.width/resolutionX, screenSize.height/resolutionY);
        scale.setPivotX(0);
        scale.setPivotY(0);
        windowPatternChoiceScene.getRoot().getTransforms().setAll(scale);
        window.setScene(windowPatternChoiceScene);

        List<String> list = new ArrayList<>();
        list.add("src/main/Images/WindowPattern/AuroraeMagnificus.png");
        List<String> list1 = new ArrayList<>();
        list1.add("src/main/Images/WindowPattern/AuroraeMagnificus.png");
        list1.add("src/main/Images/WindowPattern/AuroraSagradis.png");
        list1.add("src/main/Images/WindowPattern/Comitas.png");
        list1.add("src/main/Images/WindowPattern/LuxMundi.png");
        WindowPatternsEvent windowPatternsEvent = new WindowPatternsEvent(list, list1);
        windowPatternChoiceController.setWindowPatterns(windowPatternsEvent);

        window.setMaximized(true);
        window.show();
    }

    /**
     * Sets the sixth Scene
     * @throws IOException
     */
    public static void setEndScreen() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = new FileInputStream(new File("src/main/java/it/polimi/se2018/view/gui/EndScreen.fxml"));
        Parent root = loader.load(fileInputStream);
        endScreenController = loader.getController();
        Scene endScreen = new Scene(root);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double resolutionX = 1600;
        double resolutionY = 900;
        Scale scale = new Scale(screenSize.width/resolutionX, screenSize.height/resolutionY);
        scale.setPivotX(0);
        scale.setPivotY(0);
        endScreen.getRoot().getTransforms().setAll(scale);
        window.setScene(endScreen);
        window.setMaximized(true);
        window.show();
    }

}