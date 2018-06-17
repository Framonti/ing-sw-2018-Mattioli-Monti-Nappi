package it.polimi.se2018.events.networkevent;

import it.polimi.se2018.events.mvevent.MVEvent;
import java.util.List;

public class ClientAlreadyConnectedEvent extends MVEvent {

    private List<String> clientConnected;

    public ClientAlreadyConnectedEvent(List<String> clientConnected){
        super(70);
        this.clientConnected = clientConnected;
    }

    public List<String> getClientConnected() {
        return clientConnected;
    }
}
