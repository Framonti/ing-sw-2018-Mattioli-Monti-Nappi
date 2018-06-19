package it.polimi.se2018.events.networkevent;

/**
 * This event is crated when an user wants to play again, after the conclusion of the previous game
 * @author Framonti
 */
public class NewGameEvent extends NetworkEvent {

    public NewGameEvent(){
        super(70);
    }
}
