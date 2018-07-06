package it.polimi.se2018.events.mvevent;

/**
 * This event informs a client that the nickname chosen by an user was accepted by the server
 * @author Framonti
 */
public class NickNameAcceptedEvent extends MVEvent {

    public NickNameAcceptedEvent(){
        super(30);
    }
}
