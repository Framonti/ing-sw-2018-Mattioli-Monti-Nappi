package it.polimi.se2018.view.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class is used in order to create a simply and customizable confirmation box
 * The box blocks any interaction with the scene that calls it until the user gives an answer
 */
public class ConfirmChoiceBox {

    private static boolean choice;

    /**
     * Create a box with a simple message, a title and two buttons (yes/no).
     * @param title The title of the confirmation box
     * @param message The question the user will answer with a yes/no choice
     * @return True if the "Sì" button is pressed, false if the "No" button is pressed
     */
    public static boolean confirmChoice(String title, String message){

        Stage window = new Stage();
        //Blocks interaction with the caller Scene
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(400);
        window.setMinHeight(225);
        window.setResizable(false);

        VBox vBox = new VBox(5);
        HBox hBox = new HBox(5);

        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(30);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);

        //Sets the message and the button
        Label label = new Label(message);
        Button yesButton = new Button("Sì");
        Button noButton = new Button("No");

        //Adds everything to the layouts
        vBox.getChildren().addAll(label, hBox);
        hBox.getChildren().addAll(yesButton, noButton);

        yesButton.setOnAction(event -> {choice = true; window.close();});
        noButton.setOnAction(event -> {choice = false; window.close();});

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();

        return choice;
    }

}
