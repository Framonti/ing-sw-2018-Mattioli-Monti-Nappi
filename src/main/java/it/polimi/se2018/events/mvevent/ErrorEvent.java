package it.polimi.se2018.events.mvevent;

/**
 * This Event holds informations about any error
 * @author Framonti
 */
public class ErrorEvent extends MVEvent {

    private String messageToDisplay;

    /**
     * Constructor
     * @param messageToDisplay A String representing an error message
     */
    public ErrorEvent(String messageToDisplay){

        super(9);
        this.messageToDisplay = messageToDisplay;
    }

    /**
     * Gets the messageToDisplay attribute
     * @return The messageToDisplay attribute
     */
    public String getMessageToDisplay() {

        return messageToDisplay;
    }
}
