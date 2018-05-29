package it.polimi.se2018.network.client;

import it.polimi.se2018.network.server.ServerInterface;
import it.polimi.se2018.view.ViewCLI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * This class represents the client
 * @author fabio
 */
public class Client {

    private static final String HOST = "localhost";
    private static final int PORT = 1111;

    /**
     * This method initializes a new socket or network client, based on client preferences.
     * Asks to the player his name and then adds the client reference to the server.
     * @param args Standard main parameter
     */
    public static void main(String[] args) {
        String choice = networkChoice();
        switch (choice) {
            case "1":
                try {
                    ServerInterface server = (ServerInterface) Naming.lookup("//localhost/MyServer");

                    ClientImplementation client = new ClientImplementation();
                    client.setServer(server);

                    ClientInterfaceRMI remoteReference = (ClientInterfaceRMI) UnicastRemoteObject.exportObject(client, 0);

                    ViewCLI viewCLI = new ViewCLI();

                    client.addObserver(viewCLI);
                    viewCLI.addObserver(client);

                    client.setName(viewCLI.askName());

                    server.addClient(remoteReference);

                } catch (MalformedURLException e) {
                    System.err.println("URL non trovato!");
                } catch (RemoteException e) {
                    System.err.println("Errore di connessione: " + e.getMessage() + "!");
                } catch (NotBoundException e) {
                    System.err.println("Il riferimento passato non è associato a nulla!");
                }
                break;

            case "2":

                ViewCLI viewCLI = new ViewCLI();
                ClientImplementation client = new ClientImplementation();
                client.setName(viewCLI.askName());

                ServerInterface server = new NetworkHandler(HOST, PORT, client);
                client.setServer(server);

                client.addObserver(viewCLI);
                viewCLI.addObserver(client);

                break;

            default:
                System.out.println("This should never happen!\n");
                main(args);
                break;
        }
    }

    /**
     * This is a support method for main, it asks which type of connection is preferred
     * @return The string that contains the answer
     */
    private static String networkChoice() {
        Scanner network = new Scanner(System.in);
        String choice = "";
        while(!choice.equals("1") && !choice.equals("2")) {
            System.out.println("Scegli il tipo di connessione che preferisci\n1)\tRMI\n2)\tSocket");
            choice = network.nextLine();
        }
        return choice;
    }

}
