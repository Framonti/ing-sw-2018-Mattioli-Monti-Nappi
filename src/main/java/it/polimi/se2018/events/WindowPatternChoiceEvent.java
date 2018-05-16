package it.polimi.se2018.events;

/**
 * @author fabio
 */
public class WindowPatternChoiceEvent {

    private int choice;

    public WindowPatternChoiceEvent(String userInput) {
        try {
            choice = Integer.parseInt(userInput) - 1;
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parametri insufficienti");
        }
    }

    public int getChoice() {
        return choice;
    }

    @Override
    public String toString() {
        return "WindowPatternChoiceEvent";
    }
}
