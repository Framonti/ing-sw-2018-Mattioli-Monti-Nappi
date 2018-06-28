package it.polimi.se2018.events.vcevent;

/**
 * This class represents the event related to the toolCard Flux Remover
 * @author fabio
 */
public class FluxRemoverChooseDiceEvent extends VCEvent {

    private int diceIndex;

    /**
     * Constructor of the class
     * @param userInput It's the string that contains the parameters
     * @throws IllegalArgumentException If the string does not contain valid parameters
     */
    public FluxRemoverChooseDiceEvent(String userInput) {
        super(11);
        try {
            String[] input = userInput.split("\\s+");
            diceIndex = Integer.parseInt(input[0]) - 1;
        }
        catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Paramentri insufficienti");
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }
    }

    /**
     * @return The diceIndex attribute
     */
    public int getDiceIndex() {
        return diceIndex;
    }

}
