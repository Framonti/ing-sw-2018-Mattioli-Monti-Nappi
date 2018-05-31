package it.polimi.se2018.view.gui;

 import it.polimi.se2018.events.mvevent.WindowPatternsEvent;
 import it.polimi.se2018.events.vcevent.ConnectionChoiceEvent;
 import javafx.application.Application;
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

public class GUIManager{

    private static Stage window;
    private static NicknameChoiceController nicknameChoiceController;
    private static ConnectionChoiceController connectionChoiceController;
    private static WaitingRoomController waitingRoomController;
    private static WindowPatternChoiceController windowPatternChoiceController;
    private static EndScreenController endScreenController;

    public static NicknameChoiceController getNicknameChoiceController() {
        return nicknameChoiceController;
    }

    public static ConnectionChoiceController getConnectionChoiceController() {
        return connectionChoiceController;
    }

    public static WaitingRoomController getWaitingRoomController() {
        return waitingRoomController;
    }

    public static WindowPatternChoiceController getWindowPatternChoiceController() {
        return windowPatternChoiceController;
    }

    public static EndScreenController getEndScreenController() {
        return endScreenController;
    }

    public static Stage getWindow(){
        return window;
    }

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
        //GUIManager.setLobbyScene();
        //GUIManager.setWindowPatternChoiceScene();
      //  try {
        //    GUIManager.setEndScreen();
        //} catch (IOException e) {
       // }
    }


    public static void closeProgram() {

        if (ConfirmChoiceBox.confirmChoice("Conferma chiusura", "Sei sicuro di volere uscire?"))
            window.close();
    }

    private static void setNicknameChoiceScene() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = new FileInputStream(new File("src/main/java/it/polimi/se2018/view/gui/nickname.fxml"));
        Parent root = loader.load(fileInputStream);
        nicknameChoiceController = loader.getController();
        window.setScene(new Scene(root));
        window.show();
    }


    public static void setConnectionChoiceScene() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = new FileInputStream(new File("src/main/java/it/polimi/se2018/view/gui/ConnectionChoice.fxml"));
        Parent root = loader.load(fileInputStream);
        connectionChoiceController = loader.getController();
        window.setScene(new Scene(root));
        window.show();
    }


    public static void setLobbyScene() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = new FileInputStream(new File("src/main/java/it/polimi/se2018/view/gui/waitingRoom.fxml"));
        Parent root = loader.load(fileInputStream);
        waitingRoomController = loader.getController();
        window.setScene(new Scene(root));
        window.show();
    }

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