package it.polimi.se2018.events;

import java.io.Serializable;

/**
 * @author Framonti
 */
public class ConnectionChoiceEvent implements Serializable{

    private int id = 21;
    private int choice;
    private String ipAddress;
    static final long serialVersionUID = 44L;

    public ConnectionChoiceEvent(int choice, String ipAddress){

        this.choice = choice;
        this.ipAddress = ipAddress;
    }

    public int getChoice(){
        return choice;
    }

    public String getIpAddress() {

        return ipAddress;
    }

    public int getId() {
        return id;
    }
}
