package it.polimi.se2018.network.client;

import it.polimi.se2018.events.mvevent.ErrorEvent;
import it.polimi.se2018.events.mvevent.MVEvent;
import it.polimi.se2018.events.networkevent.*;
import it.polimi.se2018.events.vcevent.NicknameEvent;
import it.polimi.se2018.events.vcevent.UnsuspendEvent;
import it.polimi.se2018.events.vcevent.VCEvent;
import it.polimi.se2018.events.vcevent.WindowPatternChoiceEvent;
import it.polimi.se2018.network.server.ServerInterface;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observable;
import java.util.Observer;

/**
 * This class represents the implementation of the client
 * @author fabio
 */
public class ClientImplementation extends Observable implements ClientInterfaceRMI, ClientInterfaceSocket, Observer {

    private static final int SOCKET_PORT = 1111;
    private static final int RMI_PORT = 1099;
    private ServerInterface server;
    private String name;
    private int connectionChoice;
    private boolean isGUI;
    private ClientInterfaceRMI remoteReference = null;
    private static final String CONNECTION_ERROR = "Errore di connessione: ";

    public ClientImplementation(boolean isGUI) {
        this.isGUI = isGUI;
    }

    @Override
    public String getClientName() {
        return name;
    }

    @Override
    public String getName() {
        return getClientName();
    }

    @Override
    public void notify(MVEvent mvEvent) {
        if (!isGUI && mvEvent.getId() != 56 && mvEvent.getId() != 40 && mvEvent.getId() != 60) {
            setChanged();
            notifyObservers(mvEvent);
        }
        else if (isGUI) {
            setChanged();
            notifyObservers(mvEvent);
            if (mvEvent.getId() == 56 || mvEvent.getId() == 30 || mvEvent.getId() == 40 || mvEvent.getId() == 60) {
                setChanged();
                notifyObservers(new NewObserverEvent(this));
            }
        }
        if (mvEvent.getId() == 9) {
            ErrorEvent errorEvent = (ErrorEvent) mvEvent;
            if (errorEvent.getMessageToDisplay().equals("Nickname già in uso!")) {
                setChanged();
                notifyObservers(new ConnectionEstablishedEvent(false));
            }
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
        if (event.getClass() == ConnectionChoiceEvent.class)
            this.connection((ConnectionChoiceEvent) event);
        else if (event.getClass() == NewGameEvent.class){
            setChanged();
            notifyObservers(new ConnectionEstablishedEvent(true));
            setChanged();
            notifyObservers(new NewObserverEvent(this));
        }
        else{
            VCEvent vcEvent = (VCEvent) event;
            if (vcEvent.getId() == 20) {
                NicknameEvent nicknameEvent = (NicknameEvent) vcEvent;
                this.name = nicknameEvent.getNickname();
                tryNickname(nicknameEvent);
            }
            else {
                if (vcEvent.getId() == 15)
                    vcEvent = new UnsuspendEvent(name);
                else if (vcEvent.getId() == -1) {
                    WindowPatternChoiceEvent windowPatternChoiceEvent = (WindowPatternChoiceEvent) vcEvent;
                    vcEvent = new WindowPatternChoiceEvent(String.valueOf(windowPatternChoiceEvent.getChoice() + 1), name);
                } else if (vcEvent.getId() == 22) {
                    setChanged();
                    notifyObservers(new SinglePlayerViewEvent());
                }
                try {
                    server.notify(vcEvent);
                } catch (RemoteException e) {
                    setChanged();
                    notifyObservers(new ErrorEvent(CONNECTION_ERROR + e.getMessage() + "!"));
                }
            }
        }
    }

    /**
     * Tries to add the client with its name
     * @param nicknameEvent It's the event that contains the name of the client
     */
    private void tryNickname(NicknameEvent nicknameEvent) {
        if(connectionChoice == 1) {
            if(nicknameEvent.isFirstTime()){
                //Usare amazon checkip per rmi, settare la proprietà del sistema
                try {
                    remoteReference = (ClientInterfaceRMI) UnicastRemoteObject.exportObject(this, 0);
                } catch (ExportException ignore) {
                    //if the object is already exported then do nothing
                } catch (RemoteException e) {
                    System.err.println(CONNECTION_ERROR + e.getMessage() + "!");
                }
            }

            try {
                server.addClient(remoteReference);
            } catch (RemoteException e) {
                System.err.println(CONNECTION_ERROR + e.getMessage() + "!");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                setChanged();
                notifyObservers(new ConnectionEstablishedEvent(false));
            }
        } else {
            try {
                server.notify(nicknameEvent);
            }
            catch (RemoteException e) {
                System.out.println("This should never happen!");
            }
        }
    }

    /**
     * Tries to connect the client to the server
     * @param connectionChoiceEvent It's the event containing the IP address of the
     *                              server and the connection type selected by the client
     */
    public void connection(ConnectionChoiceEvent connectionChoiceEvent){

        connectionChoice = connectionChoiceEvent.getChoice();
        if(connectionChoice == 1) {
            try {
                server = (ServerInterface) Naming.lookup("//" + connectionChoiceEvent.getIpAddress() + ":" + RMI_PORT + "/MyServer");
                setChanged();
                notifyObservers(new ConnectionEstablishedEvent(true));
            } catch (MalformedURLException e) {
                System.err.println("URL non trovato!");
                setChanged();
                notifyObservers(new ConnectionRefusedEvent());
            } catch (RemoteException e) {
                System.err.println(CONNECTION_ERROR + e.getMessage() + "!");
                setChanged();
                notifyObservers(new ConnectionRefusedEvent());
            } catch (NotBoundException e) {
                System.err.println("Il riferimento passato non è associato a nulla!");
                setChanged();
                notifyObservers(new ConnectionRefusedEvent());
            }
        }
        else if(connectionChoice == 2){
            try{
                server = new NetworkHandler(connectionChoiceEvent.getIpAddress(), SOCKET_PORT, this);
                this.setServer(server);
                setChanged();
                notifyObservers(new ConnectionEstablishedEvent(true));
            }
            catch (IOException e){
                setChanged();
                notifyObservers(new ConnectionRefusedEvent());
            }
        }
        else
            System.out.println("This should never happen!");
        if (isGUI) {
            setChanged();
            notifyObservers(new NewObserverEvent(this));
        }
    }

}