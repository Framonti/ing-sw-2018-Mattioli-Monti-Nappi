package it.polimi.se2018.events.vcevent;

/**
 * This class represents the event of choosing the window pattern at the begin of the game
 * @author fabio
 */
public class WindowPatternChoiceEvent extends VCEvent {

    private int choice;
    private String name;

    /**
     * Constructor of the class
     * @param userInput It's the string that contains the parameter
     * @throws IllegalArgumentException If the string does not contain a valid number
     */
    public WindowPatternChoiceEvent(String userInput, String playerName) {
        super(-1);
        try {
            name = playerName;
            choice = Integer.parseInt(userInput) - 1;
            if(choice < 0 || choice > 3)
                throw new IllegalArgumentException("Parametro inesistente");
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }
    }

    /**
     * @return The choice attribute
     */
    public int getChoice() {
        return choice;
    }

    /**
     * Gets the name
     * @return The name attribute
     */
    public String getName() {
        return name;
    }
}
