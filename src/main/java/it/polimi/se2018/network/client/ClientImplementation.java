package it.polimi.se2018.network.client;

import it.polimi.se2018.events.ConnectionEstablishedEvent;
import it.polimi.se2018.events.NewObserverEvent;
import it.polimi.se2018.events.mvevent.ErrorEvent;
import it.polimi.se2018.events.mvevent.MVEvent;
import it.polimi.se2018.events.ConnectionChoiceEvent;
import it.polimi.se2018.events.vcevent.NicknameEvent;
import it.polimi.se2018.events.vcevent.VCEvent;
import it.polimi.se2018.network.server.ServerInterface;
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
    public void setClientName(String name) {
        setName(name);
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
                tryNickname(nicknameEvent);
            }
            else try {
                server.notify(vcEvent);
            } catch (RemoteException e) {
                setChanged();
                notifyObservers(new ErrorEvent(CONNECTION_ERROR + e.getMessage() + "!"));
            }

        }
    }

    private void tryNickname(NicknameEvent nicknameEvent) {
        if(connectionChoice == 1) {
            if(nicknameEvent.isFirstTime()){
                /* try (BufferedReader br = new BufferedReader(new InputStreamReader(new URL("http://checkip.amazonaws.com").openStream()))) {
                    System.setProperty("java.rmi.server.hostname", br.readLine());
                } catch (IOException e) {
                    tryNickname(nicknameEvent);
                    return;
                }*/
                try {
                    remoteReference = (ClientInterfaceRMI) UnicastRemoteObject.exportObject(this, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            try {
                server.addClient(remoteReference);
            } catch (RemoteException e) {
                System.err.println(CONNECTION_ERROR + e.getMessage() + "!");
                e.printStackTrace();
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

    public void connection(ConnectionChoiceEvent connectionChoiceEvent){

        connectionChoice = connectionChoiceEvent.getChoice();
        if(connectionChoice == 1) {
            try {
                server = (ServerInterface) Naming.lookup("//" + connectionChoiceEvent.getIpAddress() + ":" + RMI_PORT + "/MyServer");
                setChanged();
                notifyObservers(new ConnectionEstablishedEvent(true));
            } catch (MalformedURLException e) {
                System.err.println("URL non trovato!");
            } catch (RemoteException e) {
                System.err.println(CONNECTION_ERROR + e.getMessage() + "!");
            } catch (NotBoundException e) {
                System.err.println("Il riferimento passato non è associato a nulla!");
            }
        }
        else if(connectionChoice == 2){
            server = new NetworkHandler(connectionChoiceEvent.getIpAddress(), SOCKET_PORT, this);
            this.setServer(server);
            setChanged();
            notifyObservers(new ConnectionEstablishedEvent(true));
        }
        else
            System.out.println("This should never happen!");
        if (isGUI) {
            setChanged();
            notifyObservers(new NewObserverEvent(this));
        }
    }

}