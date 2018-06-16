package it.polimi.se2018.network.server;

import it.polimi.se2018.view.VirtualViewCLI;

import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * This class creates a new RMI and socket server
 * @author fabio
 */
public class Server {

    private static ServerImplementation serverImplementation;

    /**
     * This constructor initialize the socket server and creates a new ClientGatherer.
     * 1111 it's the standard port
     */
    private Server() {
        int socketPORT = 1111;
        new ClientGatherer(this, socketPORT).start();

        System.out.println("[Socket]\tServer is up.");
    }

    /**
     * This method adds a new socket client to the game
     * @param newClient It's the socket of the new client
     */
    public void addClient(Socket newClient) {
        VirtualClient virtualClient = new VirtualClient(newClient);
        virtualClient.start();
    }

    /**
     * @return The reference of the ServerImplementation
     */
    public static ServerImplementation getServerImplementation() {
        return serverImplementation;
    }

    /**
     * This method creates and exports a Registry instance, 1099 it's the standard RMI port.
     * Initializes the virtualView and the serverImplementation.
     * Rebinds the specified name to a new remote object.
     * Constructs a new Server
     * @param args It's the standard parameter of main
     */
    public static void main(String[] args) {

        try {
            int rmiPORT = 1099;
            LocateRegistry.createRegistry(rmiPORT);
        }
        catch (RemoteException e) {
            System.out.println("Registry already exists!");
        }

        try {
            VirtualViewCLI virtualViewCLI = new VirtualViewCLI();
            serverImplementation = new ServerImplementation(virtualViewCLI);
            virtualViewCLI.setServer(serverImplementation);

            Naming.rebind("//localhost/MyServer", serverImplementation);

            System.out.println("[RMI]\t\tServer is up.");
        }
        catch (MalformedURLException e) {
            System.err.println("The name is not an appropriately formatted URL!");
        }
        catch (RemoteException e) {
            System.err.println("Connection error: " + e.getMessage() + "!");
        }

        new Server();
    }

}
