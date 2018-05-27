package it.polimi.se2018.events.mvevent;

import java.util.List;

/**
 * This Event holds informations about the DraftPool
 * @author Framonti
 */
public class DraftPoolEvent extends MVEvent{


    private List<String> draftPoolString;

    /**
     * Constructor
     * @param draftPoolString A String representing the draftPool
     */
    public DraftPoolEvent(List<String> draftPoolString){

        super(2);
        this.draftPoolString = draftPoolString;
    }

    /**
     * Gets the draftPoolString attribute
     * @return The draftPoolString attribute
     */
    public List<String> getDraftPoolString() {

        return draftPoolString;
    }
}
