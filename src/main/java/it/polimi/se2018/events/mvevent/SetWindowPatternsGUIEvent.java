package it.polimi.se2018.events.mvevent;

import java.util.List;

public class SetWindowPatternsGUIEvent extends MVEvent {

    private List<String> windowPatternsGUI;

    public SetWindowPatternsGUIEvent(List<String> windowPatternsGUI){
        super(15);
        this.windowPatternsGUI = windowPatternsGUI;
    }

    public List<String> getWindowPatternsGUI() {
        return windowPatternsGUI;
    }
}
