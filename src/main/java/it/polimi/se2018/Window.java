package it.polimi.se2018;

public class Window {

    //Attributes (the ones commented don't have a class already)
    private WindowFrame windowFrame;
    private WindowPattern windowPattern;
    //private DicePattern dicePattern;

    //Constructor
    public Window(WindowFrame windowFrame, WindowPattern windowPattern /*, DicePattern dicePattern*/) {
        this.windowFrame = windowFrame;
        this.windowPattern = windowPattern;
        //this.dicePattern = dicePattern;
    }

    public void show() {
        //graphic only method
    }
}
