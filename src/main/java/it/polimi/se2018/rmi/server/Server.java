package it.polimi.se2018.rmi.server;

import it.polimi.se2018.view.VirtualViewCLI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * This class runs a new rmi server
 * @author fabio
 */
public class Server {

    /**
     * This is the default port of rmi
     */
    private static int PORT = 1099;

    /**
     * This method creates and exports a Registry instance, initializes the virtualView and the serverImplementation.
     * Then rebinds the specified name to a new remote object
     * @param args It's the standard parameter of main
     */
    public static void main(String[] args) {

        try {
            LocateRegistry.createRegistry(PORT);
        }
        catch (RemoteException e) {
            System.out.println("Registry già presente!");
        }

        try {
            VirtualViewCLI virtualViewCLI = new VirtualViewCLI();
            ServerImplementation serverImplementation = new ServerImplementation(virtualViewCLI);

            Naming.rebind("//localhost/MyServer", serverImplementation);

            System.out.println("Server is up.");
        }
        catch (MalformedURLException e) {
            System.err.println("Impossibile registrare l'oggetto indicato!");
        }
        catch (RemoteException e) {
            System.err.println("Errore di connessione: " + e.getMessage() + "!");
        }
    }

    //Il metodo che crea il gameSetupSingleton parte solo quando ci sono connessi 4 giocatori o scade il timeout

}
