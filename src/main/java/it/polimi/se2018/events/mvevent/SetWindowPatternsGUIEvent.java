package it.polimi.se2018.events.mvevent;

import java.util.List;

public class SetWindowPatternsGUIEvent extends MVEvent {

    private List<String> windowPatternsGUI;
    private List<String> favorTokensNumber;

    public SetWindowPatternsGUIEvent(List<String> windowPatternsGUI, List<String> favorTokensNumber){
        super(15);
        this.windowPatternsGUI = windowPatternsGUI;
        this.favorTokensNumber = favorTokensNumber;
    }

    public List<String> getWindowPatternsGUI() {
        return windowPatternsGUI;
    }

    public List<String> getFavorTokensNumber() {
        return favorTokensNumber;
    }
}
