package it.polimi.se2018.rmi.client;

import it.polimi.se2018.rmi.server.ServerInterface;
import it.polimi.se2018.view.ViewCLI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class runs a new rmi client
 * @author fabio
 */
public class Client {

    /**
     * This method looks for the server, then initializes a new clientImplementation and sets its server attribute.
     * Asks to the player his name and then adds the client reference to the server
     * @param args Standard main parameter
     */
    public static void main(String[] args) {
        ServerInterface server;

        try {
            server = (ServerInterface) Naming.lookup("//localhost/MyServer");

            ClientImplementation client = new ClientImplementation();
            client.setServer(server);

            ClientInterface remoteReference = (ClientInterface) UnicastRemoteObject.exportObject(client, 0);

            ViewCLI viewCLI = new ViewCLI();

            client.addObserver(viewCLI);
            viewCLI.addObserver(client);

            client.setName(viewCLI.askName());

            server.addClient(remoteReference);

        }
        catch (MalformedURLException e) {
            System.err.println("URL non trovato!");
        }
        catch (RemoteException e) {
            System.err.println("Errore di connessione: " + e.getMessage() + "!");
        }
        catch (NotBoundException e) {
            System.err.println("Il riferimento passato non è associato a nulla!");
        }
    }

}
