package it.polimi.se2018.events.networkevent;

/**
 * @author Framonti
 */
public class ConnectionChoiceEvent extends NetworkEvent{

    private int choice;
    private String ipAddress;
    static final long serialVersionUID = 44L;

    public ConnectionChoiceEvent(int choice, String ipAddress){

        super(21);
        this.choice = choice;
        this.ipAddress = ipAddress;
    }

    public int getChoice(){
        return choice;
    }

    public String getIpAddress() {

        return ipAddress;
    }

}
