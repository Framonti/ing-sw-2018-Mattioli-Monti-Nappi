package it.polimi.se2018.events.networkevent;

/**
 * This Event informs the GUI that the game is SinglePlayer, loading the correct fxml and its controller
 * @author  fabio
 */
public class SinglePlayerViewEvent extends NetworkEvent {

    public SinglePlayerViewEvent() {
        super(1);
    }

}
