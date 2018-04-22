package it.polimi.se2018;

public class Window {

    private WindowFrame windowFrame;
    private WindowPattern windowPattern;
    private DicePattern dicePattern;    //la classe DicePattern deve ancora essere implementata

    //Constructor
    public Window(WindowFrame windowFrame, WindowPattern windowPattern, DicePattern dicePattern) {
        this.windowFrame = windowFrame;
        this.windowPattern = windowPattern;
        this.dicePattern = dicePattern;
    }

    public void show() {
        //graphic only method
    }
}
