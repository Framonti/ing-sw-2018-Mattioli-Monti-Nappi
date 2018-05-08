package it.polimi.se2018.model;

public class WindowPatternCard {
    private WindowPattern windowPattern1;
    private WindowPattern windowPattern2;

    //Constructor
    public WindowPatternCard(WindowPattern windowPattern1, WindowPattern windowPattern2){
        this.windowPattern1 = windowPattern1;
        this.windowPattern2 = windowPattern2;
    }

    //Getters
    public WindowPattern getWindowPattern1() {
        return windowPattern1;
    }

    public WindowPattern getWindowPattern2() {
        return windowPattern2;
    }
}
