package it.polimi.se2018.network.client;

import it.polimi.se2018.view.ViewCLI;
import it.polimi.se2018.view.gui.ViewGUI;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.Scanner;

/**
 * This class represents the client
 * @author fabio
 */
public class Client extends Application{

    public static void main(String[] args) {

        String viewString;
        System.out.println("Come vorresti giocare?\n1)GUI\n2)CLI");
        Scanner scanner = new Scanner(System.in);
        viewString = scanner.nextLine();

        if(viewString.equals("1")){
            launch(args);
        }
        else if(viewString.equals("2"))
            cliGame();
        else{
            System.out.println("Non è stata inserita un'opzione corretta");
            main(args);
        }
    }

    @Override
    public void start(Stage primaryStage) {

        ViewGUI viewGUI = new ViewGUI();
        viewGUI.startGUI(primaryStage);
        ClientImplementation client = new ClientImplementation(true);
        client.addObserver(viewGUI);
        viewGUI.getGuiControllerObservable().addObserver(client);

    }

    /**
     * This method initializes a new socket or network client, based on client preferences.
     * Asks to the player his name and then adds the client reference to the server.
     */
    private static void cliGame() {

        ClientImplementation client = new ClientImplementation(false);
        ViewCLI viewCLI = new ViewCLI();
        client.addObserver(viewCLI);
        viewCLI.addObserver(client);
        viewCLI.askConnection();
    }
}
