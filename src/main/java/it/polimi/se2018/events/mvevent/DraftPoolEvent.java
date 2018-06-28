package it.polimi.se2018.events.mvevent;

import java.util.List;

/**
 * This Event holds information about the DraftPool
 * @author Framonti
 */
public class DraftPoolEvent extends MVEvent{

    private List<String> draftPoolStringCLI;
    private List<String> draftPoolGUI;

    /**
     * Creates a DraftPoolEvent
     * @param draftPoolStringCLI A List containing the abbreviations of the dice currently in the DraftPool
     * @param draftPoolGUI A List containing path, each representing a dice to be loaded in the GUI
     */
    public DraftPoolEvent(List<String> draftPoolStringCLI ,List<String> draftPoolGUI){

        super(2);
        this.draftPoolStringCLI = draftPoolStringCLI;
        this.draftPoolGUI = draftPoolGUI;
    }

    /**
     * Gets the draftPoolString attribute
     * @return The draftPoolString attribute
     */
    public List<String> getDraftPoolString() {

        return draftPoolStringCLI;
    }

    /**
     * Gets the draftPoolGUI
     * @return The draftPoolGUI, representing path that the GUI will load
     */
    public List<String> getDraftPoolGUI() {
        return draftPoolGUI;
    }
}
