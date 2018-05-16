package it.polimi.se2018.events;

/**
 * @author fabio
 */
public class GlazingHammerEvent {

    private int toolCard;

    //mettere un if per vedere se il giocatore può tirare di nuovo?
    public GlazingHammerEvent(String userInput) {
        try {
            toolCard = Integer.parseInt(userInput) - 1;
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }
    }

    public int getToolCard() {
        return toolCard;
    }

    @Override
    public String toString() {
        return "GlazingHammerEvent";
    }
}
