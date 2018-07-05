package it.polimi.se2018.events.vcevent;

import it.polimi.se2018.model.Position;

/**
 * This event is generated when a player uses the Eglomise Brush ToolCard
 * @author  Daniele Mattioli
 */
public class EglomiseBrushEvent extends VCEvent {

    private Position initialPosition;
    private Position finalPosition;

    /**
     * The Constructor analyzes the userInput and saves it into the class's attributes
     * @param userInput A String representing an user input
     * @throws IllegalArgumentException If the string does not contain valid parameters
     */
    public EglomiseBrushEvent(String userInput){
        super(2);
        try{
            String[] parameters = userInput.split("\\s+");
            initialPosition = new Position(Integer.parseInt(parameters[0])-1, Integer.parseInt(parameters[1])-1);
            finalPosition = new Position (Integer.parseInt(parameters[2]) -1, Integer.parseInt(parameters[3])-1);
        }
        catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Parametri insufficienti");
        }
    }

    /**
     * Gets the initialPosition
     * @return The InitialPosition
     */
    public Position getInitialPosition() {
        return initialPosition;
    }

    /**
     * Gets the finalPosition
     * @return The finalPosition
     */
    public Position getFinalPosition() {

        return finalPosition;
    }
}
