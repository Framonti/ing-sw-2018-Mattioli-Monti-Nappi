package it.polimi.se2018.events.vcevent;

/**
 * This event is generated when a player uses the Lens Cutter ToolCard
 * @author Daniele Mattioli
 */
public class LensCutterEvent extends VCEvent {

    private int roundIndex;
    private int diceIndexRoundTrack;
    private int diceIndexDraftPool;

    /**
     * The Constructor analyzes the userInput and saves it into the class's attributes
     * @param userInput A String representing an user input
     * @throws IllegalArgumentException If the string does not contain valid parameters
     */
    public LensCutterEvent(String userInput){
        super(5);
        try{
            //String is interpreted as: draftPool index, round, roundTrack index
            String[] parameters = userInput.split("\\s+");
            diceIndexDraftPool = Integer.parseInt(parameters[0]) -1;
            roundIndex = Integer.parseInt(parameters[1]) -1;
            diceIndexRoundTrack = Integer.parseInt(parameters[2]) -1;

        }
        catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Parametri insufficienti");
        }
    }

    /**
     * Gets the roundIndex
     * @return The roundIndex
     */
    public int getRoundIndex() {
        return roundIndex;
    }

    /**
     * Gets the diceIndexInRoundTrack
     * @return the diceIndexInRoundTrack
     */
    public int getDiceIndexInRoundTrack() {
        return diceIndexRoundTrack;
    }

    /**
     * Gets the diceIndexInDraftPool
     * @return the diceIndexInDraftPool
     */
    public int getDiceIndexInDraftPool() {
        return diceIndexDraftPool;
    }

}
