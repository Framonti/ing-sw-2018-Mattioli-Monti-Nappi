package it.polimi.se2018.events;

import java.io.Serializable;

public class ConnectionChoiceEvent implements Serializable{

    private int id = 21;
    private int choice;
    private String ipAddress;

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
