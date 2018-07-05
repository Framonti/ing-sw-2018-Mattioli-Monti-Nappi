package it.polimi.se2018.view;

import it.polimi.se2018.events.mvevent.*;
import it.polimi.se2018.events.vcevent.VCEvent;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.network.server.ServerInterface;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;

/**
 * This class is the fake view that is observed by the controller
 * @author fabio
 */
public class VirtualView extends Observable implements Observer, ViewCLIInterface{

    private ServerInterface server;
    private Player currentPlayer;
    private static final String CONNECTION_EXCEPTION = "Errore di connessione: ";

    /**
     * This method sets the currentPlayer attribute
     * @param currentPlayer It's the current player
     */
    public void setCurrentPlayer(Player currentPlayer) { this.currentPlayer = currentPlayer; }

    /**
     * This method sets the server attribute
     * @param server It's the serverImplementation that has to be set
     */
    public void setServer(ServerInterface server) {
        this.server = server;
    }



    public void fluxBrushChoice(MVEvent fluxBrushPlaceDice) {
        try {
            server.sendTo(fluxBrushPlaceDice, currentPlayer);
        }
        catch (RemoteException e) {
            System.out.println(CONNECTION_EXCEPTION + e.getMessage());
        }
    }


    public void fluxRemoverChoice(MVEvent fluxRemoverPlaceDice) {
        try {
            server.sendTo(fluxRemoverPlaceDice, currentPlayer);
        }
        catch (RemoteException e) {
            System.out.println(CONNECTION_EXCEPTION + e.getMessage());
        }
    }

    /**
     * This method forwards the VCEvents coming from the view to the controller
     * @param vcEvent It's the event that must be forwarded
     */
    public void forwardVCEvent(VCEvent vcEvent) {
        setChanged();
        notifyObservers(vcEvent);
    }

    public void endGame() {
        setChanged();
        notifyObservers();
    }


    public void playerSuspended() {
        try {
            server.sendTo(new SuspendedEvent(), currentPlayer);
        } catch (RemoteException e) {
            System.out.println(CONNECTION_EXCEPTION + e.getMessage());
        }
    }


    public void getInput() {
        try {
            server.sendTo(new GetInputEvent(), currentPlayer);
        }
        catch (RemoteException e) {
            System.out.println(CONNECTION_EXCEPTION + e.getMessage());
        }
    }


    public void showActionMenu(MVEvent showActionMenu) {
        try {
            server.sendTo(showActionMenu, currentPlayer);
        }
        catch (RemoteException e) {
            System.out.println(CONNECTION_EXCEPTION + e.getMessage());
        }
    }


    public void showError(MVEvent errorEvent) {
        try {
            server.sendTo(errorEvent, currentPlayer);
        }
        catch (RemoteException e) {
            System.out.println(CONNECTION_EXCEPTION + e.getMessage());
        }
    }


    public void showWindowPatterns(MVEvent windowPatternsEvent) {
        try {
            server.sendTo(windowPatternsEvent, currentPlayer);
        }
        catch (RemoteException e) {
            System.out.println(CONNECTION_EXCEPTION + e.getMessage());
        }
    }


    public void showAll(MVEvent showAllEvent) {
        try {
            server.sendTo(showAllEvent, currentPlayer);
        }
        catch (RemoteException e) {
            System.out.println(CONNECTION_EXCEPTION + e.getMessage());
        }
    }


    public void showEndTurn(MVEvent endTurnEvent) {
        try {
            server.sendTo(endTurnEvent, currentPlayer);
        }
        catch (RemoteException e) {
            System.out.println(CONNECTION_EXCEPTION + e.getMessage());
        }
    }


    public void update(Observable o, Object arg) {
        MVEvent mvEvent = (MVEvent) arg;
        try {
            server.send(mvEvent);
        }
        catch (RemoteException e) {
            System.out.println(CONNECTION_EXCEPTION + e.getMessage());
        }
    }

}
