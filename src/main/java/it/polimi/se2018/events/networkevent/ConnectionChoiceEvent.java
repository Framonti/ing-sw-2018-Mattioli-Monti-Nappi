package it.polimi.se2018.events.networkevent;

/**
 * This Event holds information about the kind of connection chosen by an user (RMI or Socket) and the IP address of the server
 * @author Framonti
 */
public class ConnectionChoiceEvent extends NetworkEvent{

    private int choice;
    private String ipAddress;
    static final long serialVersionUID = 44L;

    /**
     * Constructor
     * @param choice The connection choice: 1 for RMI, 2 for Socket
     * @param ipAddress The IP address of the server
     */
    public ConnectionChoiceEvent(int choice, String ipAddress){

        super(21);
        this.choice = choice;
        this.ipAddress = ipAddress;
    }

    /**
     * Gets the connection choice
     * @return An int representing the connection choice
     */
    public int getChoice(){
        return choice;
    }

    /**
     * Gets the ip address
     * @return A String representing an IP address of the server
     */
    public String getIpAddress() {

        return ipAddress;
    }

}
