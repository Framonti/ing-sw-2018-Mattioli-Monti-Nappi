package it.polimi.se2018.events.mvevent;

import java.util.List;

/**
 * This Event holds informations about the DraftPool
 * @author Framonti
 */
public class DraftPoolEvent extends MVEvent{


    private List<String> draftPoolStringCLI;
    private List<String> draftPoolStringGUI;

    /**
     *
     * @param draftPoolStringCLI
    // * @param draftPoolStringGUI
     */
    public DraftPoolEvent(List<String> draftPoolStringCLI /*,List<String> draftPoolStringGUI*/){

        super(2);
        this.draftPoolStringCLI = draftPoolStringCLI;
        //this.draftPoolStringGUI = draftPoolStringGUI;
    }

    /**
     * Gets the draftPoolString attribute
     * @return The draftPoolString attribute
     */
    public List<String> getDraftPoolString() {

        return draftPoolStringCLI;
    }

    public List<String> getDraftPoolStringGUI() {
        return draftPoolStringGUI;
    }
}
