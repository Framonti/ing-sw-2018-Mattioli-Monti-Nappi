package it.polimi.se2018.events.mvevent;

import java.util.List;

//evento per GameController: ToolCard1, ToolCard2, ToolCard3, PublicObjectiveCard1 ,ecc...
public class SetImageViewEvent extends MVEvent{

    private List<String> paths;

    public SetImageViewEvent(List<String> paths ){
        super(44);
        this.paths = paths;
    }

    public List<String> getPaths() {
        return paths;
    }

}
