package it.polimi.se2018.network.server;

import it.polimi.se2018.events.mvevent.MVEvent;
import it.polimi.se2018.events.vcevent.NicknameEvent;
import it.polimi.se2018.events.vcevent.VCEvent;
import it.polimi.se2018.network.client.ClientInterfaceSocket;

import java.io.*;
import java.net.Socket;

import static it.polimi.se2018.network.server.Server.getServerImplementation;

/**
 * This class is instantiated every time a new client connects to the server
 * @author fabio
 */
public class VirtualClient extends Thread implements ClientInterfaceSocket {

    private Socket clientSocket;

    /**
     * This constructor only sets the clientSocket attrribute
     * @param clientSocket It's the socket of the client
     */
    public VirtualClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void notify(MVEvent mvEvent) {
        ObjectOutputStream stream;

        try {
            stream = new ObjectOutputStream(clientSocket.getOutputStream());
            stream.writeObject(mvEvent);
            stream.flush();
        }
        catch (IOException e) {
            System.out.println("Connection lost with a socketClient!");
        }
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inputStream;
            boolean loop = true;
            while(loop) {
                inputStream = new ObjectInputStream(clientSocket.getInputStream());
                VCEvent vcEvent = (VCEvent) inputStream.readObject();
                if(vcEvent == null)
                    loop = false;
                else if (vcEvent.getId() == 20) {
                    NicknameEvent nicknameEvent = (NicknameEvent) vcEvent;
                    getServerImplementation().addSocketClient(this, nicknameEvent.getNickname());
                } else
                    getServerImplementation().notify(vcEvent);
            }

            clientSocket.close();
//            getServerImplementation().removeClient(this); non capisco perché dovrei toglierlo dalla lista di clients
        }
        catch (IOException | ClassNotFoundException e) {
            getServerImplementation().removeClient(this);
        }
    }

    //TODO vedere se serve davvero questo metodo --> sempre più convinto di no
    public void testIfConnected() {
        //This method is used only to test if the connection of a client is lost.
    }
}
