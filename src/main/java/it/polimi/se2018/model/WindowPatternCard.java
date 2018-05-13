package it.polimi.se2018.model;

/** This class represents an ingame WindowPatterCard, with a WindowPattern on its front and another one on its back
 *  @author Daniele Mattioli
 * */

public class WindowPatternCard {
    private WindowPattern windowPattern1;
    private WindowPattern windowPattern2;

    /**
     * Constructor of the class.
     * @param windowPattern1 represents the front of the window pattern card
     * @param windowPattern2 represents the back of the window pattern card
     */
    public WindowPatternCard(WindowPattern windowPattern1, WindowPattern windowPattern2){
        this.windowPattern1 = windowPattern1;
        this.windowPattern2 = windowPattern2;
    }

    /**
     * Gets the front of the window pattern card
     * @return the front of the window pattern card
     */
    public WindowPattern getWindowPattern1() {
        return windowPattern1;
    }

    /**
     * Gets the back of the window pattern card
     * @return the back of the window pattern card
     */
    public WindowPattern getWindowPattern2() {
        return windowPattern2;
    }
}
