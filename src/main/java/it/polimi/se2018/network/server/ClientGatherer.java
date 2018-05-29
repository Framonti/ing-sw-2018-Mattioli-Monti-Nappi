package it.polimi.se2018.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class is generated when the socket server is initialized.
 * Its main purpose is to connect new clients
 * @author fabio
 */
public class ClientGatherer extends Thread {

    private final Server server;
    private ServerSocket serverSocket;

    /**
     * This constructor creates a new ServerSocket with the port parameter
     * @param server It's the reference to the server
     * @param port It's the port number of the ServerSocket
     */
    ClientGatherer(Server server, int port) {
        this.server = server;
        try {
            this.serverSocket = new ServerSocket(port);
        }
        catch (IOException e) {
            System.out.println("Creation of new ServerSocket failed!");
        }
    }

    @Override
    public void run() {
        while(true) {
            Socket newClient;
            try {
                newClient = serverSocket.accept();
                server.addClient(newClient);
            }
            catch (IOException e) {
                System.out.println("Adding client failed!");
            }
        }
    }
}
