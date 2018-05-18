package it.polimi.se2018.events.mvevent;

/**
 * This Event holds informations about the DraftPool
 * @author Framonti
 */
public class DraftPoolEvent extends MVEvent{


    private String draftPoolString;

    /**
     * Constructor
     * @param draftPoolString A String representing the draftPool
     */
    public DraftPoolEvent(String draftPoolString){

        super(2);
        this.draftPoolString = draftPoolString;
    }

    /**
     * Gets the draftPoolString attribute
     * @return The draftPoolString attribute
     */
    public String getDraftPoolString() {

        return draftPoolString;
    }
}
