package it.polimi.se2018.network.client;

import it.polimi.se2018.events.ConnectionEstablishedEvent;
import it.polimi.se2018.events.mvevent.ErrorEvent;
import it.polimi.se2018.events.mvevent.MVEvent;
import it.polimi.se2018.events.ConnectionChoiceEvent;
import it.polimi.se2018.events.mvevent.WindowPatternsEvent;
import it.polimi.se2018.events.vcevent.NicknameEvent;
import it.polimi.se2018.events.vcevent.VCEvent;
import it.polimi.se2018.events.vcevent.WindowPatternChoiceEvent;
import it.polimi.se2018.network.server.ServerInterface;
import it.polimi.se2018.view.gui.ConnectionChoiceController;
import it.polimi.se2018.view.gui.GUIManager;
import javafx.application.Platform;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observable;
import java.util.Observer;

/**
 * This class represents the implementation of the client
 * @author fabio
 */
public class ClientImplementation extends Observable implements ClientInterfaceRMI, ClientInterfaceSocket, Observer {

    private static final String HOST = "localhost";
    private static final int PORT = 1111;
    private ServerInterface server;
    private String name;
    private int viewChoice;

    public ClientImplementation(int viewChoice){
        this.viewChoice = viewChoice;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void notify(MVEvent mvEvent) {
        if(mvEvent.getId() == 56){
            if(viewChoice == 1){
                this.deleteObservers();
                GUIManager.setWindowPatternChoiceScene();
                GUIManager.getWindowPatternChoiceController().addObserver(this);
                this.addObserver(GUIManager.getWindowPatternChoiceController());
            }
        }
        else if(mvEvent.getId() == 40){
            if(viewChoice == 1){
                this.deleteObservers();
                GUIManager.setGameScene();
                GUIManager.getGameController().addObserver(this);
                this.addObserver(GUIManager.getGameController());
            }
        }
        else {
            setChanged();
            notifyObservers(mvEvent);
        }
    }

    /**
     * This method sets the name attribute
     * @param name It's the string representing the name that has to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setServer(ServerInterface server) {
        this.server = server;
    }

    @Override
    public void testIfConnected() {
        //This method is used only to test if the connection of a client is lost.
    }

    @Override
    public void update(Observable o, Object event) {
        if (event.getClass() == ConnectionChoiceEvent.class) {
            this.connection((ConnectionChoiceEvent) event);
        } else {
            VCEvent vcEvent = (VCEvent) event;
            if (vcEvent.getId() == 20) {
                NicknameEvent nicknameEvent = (NicknameEvent) vcEvent;
                this.name = nicknameEvent.getNickname();
                if(viewChoice == 1){
                    GUIManager.setLobbyScene();
                    GUIManager.getWaitingRoomController().addObserver(this);
                    this.addObserver(GUIManager.getWaitingRoomController());
                }
            }
            else try {
                server.notify(vcEvent);
            } catch (RemoteException e) {
                setChanged();
                notifyObservers(new ErrorEvent("Errore di connessione: " + e.getMessage() + "!"));
            }
        }
    }

    public void connection(ConnectionChoiceEvent connectionChoiceEvent){

        if(connectionChoiceEvent.getChoice() == 1) {
            try {
                server = (ServerInterface) Naming.lookup("//localhost/MyServer");
                ClientInterfaceRMI remoteReference = (ClientInterfaceRMI) UnicastRemoteObject.exportObject(this, 0);
                server.addClient(remoteReference);
                setChanged();
                notifyObservers(new ConnectionEstablishedEvent());
                if(viewChoice == 1){
                    GUIManager.setNicknameChoiceScene();
                    GUIManager.getNicknameChoiceController().addObserver(this);
                }

            } catch (MalformedURLException e) {
                System.err.println("URL non trovato!");
            } catch (RemoteException e) {
                System.err.println("Errore di connessione: " + e.getMessage() + "!");
            } catch (NotBoundException e) {
                System.err.println("Il riferimento passato non è associato a nulla!");
            }
        }
        else if(connectionChoiceEvent.getChoice() == 2){

            server = new NetworkHandler(HOST, PORT, this);
            this.setServer(server);
            setChanged();
            notifyObservers(new ConnectionEstablishedEvent());
            if(viewChoice == 1) {
                GUIManager.setNicknameChoiceScene();
                GUIManager.getNicknameChoiceController().addObserver(this);
            }
        }
        else
            System.out.println("This should never happens!");
    }

}