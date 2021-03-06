package it.polimi.se2018.network.client;

import it.polimi.se2018.events.mvevent.MVEvent;
import it.polimi.se2018.events.vcevent.VCEvent;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.network.server.ServerInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class is used if the chosen connection is Socket.
 * It manages the input and the output streams between the client and the server
 * @author fabio
 */
public class NetworkHandler extends Thread implements ServerInterface {

    private Socket socket;
    private ClientInterfaceSocket client;
    private static final String EXCEPTION = "This method should not be used here!";

    /**
     * This constructor initializes the class and starts the run method.
     * @param host It's the string representing the host path
     * @param port It's the number of the port that must be used for the connection
     * @param client It's the interface of the client
     */
    NetworkHandler(String host, int port, ClientInterfaceSocket client) throws IOException {

        this.socket = new Socket(host, port);
        this.client = client;

        this.start();
    }

    /**
     * This method initializes a new output stream and writes the event in it
     * @param vcEvent It's the event that has to be sent
     */
    public synchronized void notify(VCEvent vcEvent) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(vcEvent);
            outputStream.flush();
        }
        catch (IOException e) {
            System.out.println("Failed creation of new outputStream!");
        }
    }

    @Override
    public void run() {
        ObjectInputStream inputStream;

        while(!socket.isClosed()) {
            try {
                inputStream = new ObjectInputStream(this.socket.getInputStream());
            }
            catch (IOException e) {
                System.out.println("Il server non è più online. Riprova più tardi.");
                return;
            }
            try {
                Object obj = inputStream.readObject();
                if (obj.getClass() != String.class) {
                    MVEvent mvEvent = (MVEvent) obj;
                    client.notify(mvEvent);
                }
            }
            catch (IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
                System.out.println("inputStream error!");
            }
        }
    }

    public void addClient(ClientInterfaceRMI clientInterfaceRMI) {
        throw new UnsupportedOperationException(EXCEPTION);
    }


    public void send(MVEvent mvEvent) {
        throw new UnsupportedOperationException(EXCEPTION);
    }


    public void sendTo(MVEvent mvEvent, Player currentPlayer) {
        throw new UnsupportedOperationException(EXCEPTION);
    }
}
