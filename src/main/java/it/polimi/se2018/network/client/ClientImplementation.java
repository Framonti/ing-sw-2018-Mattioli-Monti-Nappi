package it.polimi.se2018.network.client;

import it.polimi.se2018.events.ConnectionEstablishedEvent;
import it.polimi.se2018.events.NewObserverEvent;
import it.polimi.se2018.events.mvevent.ErrorEvent;
import it.polimi.se2018.events.mvevent.MVEvent;
import it.polimi.se2018.events.ConnectionChoiceEvent;
import it.polimi.se2018.events.vcevent.NicknameEvent;
import it.polimi.se2018.events.vcevent.VCEvent;
import it.polimi.se2018.network.server.ServerInterface;
import it.polimi.se2018.view.gui.GUIManager;

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

    private static final int SOCKET_PORT = 1111;
    private static final int RMI_PORT = 1099;
    private ServerInterface server;
    private String name;
    private final Object nameLock = new Object();
    private int connectionChoice;
    private boolean isGUI;

    public ClientImplementation(boolean isGUI) {
        this.isGUI = isGUI;
    }

    @Override
    public String getName() {
        return name;
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

        if (mvEvent.getId() == 30) {
            synchronized (nameLock) {
                nameLock.notifyAll();
            }
        } else if (mvEvent.getId() == 9) {
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
        else {
            VCEvent vcEvent = (VCEvent) event;
            if (vcEvent.getId() == 20) {
                NicknameEvent nicknameEvent = (NicknameEvent) vcEvent;
                this.name = nicknameEvent.getNickname();
                askName(nicknameEvent);
            }
            else try {
                server.notify(vcEvent);
            } catch (RemoteException e) {
                setChanged();
                notifyObservers(new ErrorEvent("Errore di connessione: " + e.getMessage() + "!"));
            }

        }
    }

    private void askName(NicknameEvent nicknameEvent) {
        if(connectionChoice == 1) {
            ClientInterfaceRMI remoteReference = null;
            try {
                remoteReference = (ClientInterfaceRMI) UnicastRemoteObject.exportObject(this, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            try {
                server.addClient(remoteReference);
            } catch (RemoteException e) {
                System.err.println("Errore di connessione: " + e.getMessage() + "!");
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
            synchronized (nameLock) {
                try{
                    nameLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

            }
        }
    }

    public void connection(ConnectionChoiceEvent connectionChoiceEvent){

        connectionChoice = connectionChoiceEvent.getChoice();
        if(connectionChoice == 1) {
            try {
                server = (ServerInterface) Naming.lookup("//" + connectionChoiceEvent.getIpAddress() + ":" + RMI_PORT + "/MyServer");
                setChanged();
                notifyObservers(new ConnectionEstablishedEvent(true));
                setChanged();
                notifyObservers(new NewObserverEvent(this));
            } catch (MalformedURLException e) {
                System.err.println("URL non trovato!");
            } catch (RemoteException e) {
                System.err.println("Errore di connessione: " + e.getMessage() + "!");
            } catch (NotBoundException e) {
                System.err.println("Il riferimento passato non è associato a nulla!");
            }
        }
        else if(connectionChoice/*Event.getChoice()*/ == 2){

            server = new NetworkHandler(connectionChoiceEvent.getIpAddress(), SOCKET_PORT, this);
            this.setServer(server);
            setChanged();
            notifyObservers(new ConnectionEstablishedEvent(true));
            setChanged();
            notifyObservers(new NewObserverEvent(this));
        }
        else
            System.out.println("This should never happen!");
    }

}