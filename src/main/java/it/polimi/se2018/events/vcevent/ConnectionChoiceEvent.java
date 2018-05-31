package it.polimi.se2018.events.vcevent;

public class ConnectionChoiceEvent extends VCEvent{

    private int choice;

    public ConnectionChoiceEvent(int choice){

        super(21);
        this.choice = choice;
    }

    public int getChoice(){
        return choice;
    }
}
