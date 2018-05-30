package it.polimi.se2018.view;

import it.polimi.se2018.events.mvevent.*;
import it.polimi.se2018.events.vcevent.SkipTurnEvent;
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
public class VirtualViewCLI extends Observable implements Observer, ViewCLIInterface{

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

    //TODO tutti (o quasi) i blocchi try catch sono evitabili: send gestisce già l'eccezione Remote

    /**
     * This method forwards the VCEvents coming from the view to the controller
     * @param vcEvent It's the event that must be forwarded
     */
    public void forwardVCEvent(VCEvent vcEvent) {
        setChanged();
        notifyObservers(vcEvent);
    }

    @Override
    public void getInput() {
        try {
            server.sendTo(new GetInputEvent(), currentPlayer);
        }
        catch (RemoteException | IndexOutOfBoundsException e) {
            System.out.println(CONNECTION_EXCEPTION + e.getMessage());
        }
    }

    @Override
    public void showActionMenu(MVEvent showActionMenu) {
        try {
            server.sendTo(showActionMenu, currentPlayer);
        }
        catch (RemoteException e) {
            System.out.println(CONNECTION_EXCEPTION + e.getMessage());
        }
    }

    @Override
    public void showError(MVEvent errorEvent) {
        try {
            server.sendTo(errorEvent, currentPlayer);
        }
        catch (RemoteException e) {
            System.out.println(CONNECTION_EXCEPTION + e.getMessage());
        }
    }

    @Override
    public void showWindowPatterns(MVEvent windowPatternsEvent) {
        try {
            server.sendTo(windowPatternsEvent, currentPlayer);
        }
        catch (RemoteException e) {
            System.out.println(CONNECTION_EXCEPTION + e.getMessage());
        }
    }

    @Override
    public void showAll(MVEvent showAllEvent) {
        try {
            server.sendTo(showAllEvent, currentPlayer);
        }
        catch (RemoteException e) {
            System.out.println(CONNECTION_EXCEPTION + e.getMessage());
        }
    }

    @Override
    public void showEndTurn(MVEvent endTurnEvent) {
        try {
            server.sendTo(endTurnEvent, currentPlayer);
        }
        catch (RemoteException e) {
            System.out.println(CONNECTION_EXCEPTION + e.getMessage());
        }
    }

    @Override
    public void fluxBrushChoice() {
        try {
            server.sendTo(new FluxBrushChoiceEvent(), currentPlayer);
        }
        catch (RemoteException e) {
            System.out.println(CONNECTION_EXCEPTION + e.getMessage());
        }
    }

    //Qui arrivano solo eventi che modificano il model, quindi devono essere mostrati a tutti
    @Override
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
