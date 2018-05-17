package it.polimi.se2018.events;

/**
 * This class represents the event of choosing the window pattern at the begin of the game
 * @author fabio
 */
public class WindowPatternChoiceEvent extends Event {

    private int choice;

    /**
     * Constructor of the class
     * @param userInput It's the string that contains the parameter
     * @throws IllegalArgumentException If the string does not contain a valid number
     */
    public WindowPatternChoiceEvent(String userInput) {
        try {
            choice = Integer.parseInt(userInput) - 1;
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }
        setId(-1);
    }

    /**
     * @return The choice attribute
     */
    public int getChoice() {
        return choice;
    }

    /**
     * @return The name of this class
     */
    @Override
    public String toString() {
        return "WindowPatternChoiceEvent";
    }
}
