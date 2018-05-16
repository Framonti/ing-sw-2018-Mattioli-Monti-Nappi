package it.polimi.se2018.events;

public class GlazingHammerEvent {

    int toolCard;

    //mettere un if per vedere se il giocatore può tirare di nuovo?
    public GlazingHammerEvent(String userInput) {
        try {
            toolCard = Integer.parseInt(userInput);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }
    }
}
