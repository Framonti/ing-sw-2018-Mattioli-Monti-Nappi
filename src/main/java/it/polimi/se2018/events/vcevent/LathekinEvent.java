package it.polimi.se2018.events.vcevent;

import it.polimi.se2018.model.Position;

/**
 * This event is generated when a player uses the Lathekin ToolCard
 */
public class LathekinEvent extends VCEvent {

    private Position initialPosition1;
    private Position finalPosition1;
    private Position initialPosition2;
    private Position finalPosition2;

    /**
     * The Constructor analyzes the userInput and saves it into the class's attributes
     * @param userInput A String representing an user input
     * @throws IllegalArgumentException If the string does not contain valid parameters
     */
    public LathekinEvent(String userInput){
        super(4);
        try{
            String[] parameters = userInput.split("\\s+");
            initialPosition1 = new Position(Integer.parseInt(parameters[0])-1, Integer.parseInt(parameters[1])-1);
            finalPosition1 = new Position (Integer.parseInt(parameters[2])-1, Integer.parseInt(parameters[3])-1);
            initialPosition2 = new Position ( Integer.parseInt(parameters[4])-1, Integer.parseInt(parameters[5])-1);
            finalPosition2 = new Position ( Integer.parseInt(parameters[6])-1, Integer.parseInt(parameters[7])-1);
        }
        catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Parametri insufficienti");
        }
    }

    /**
     * Gets the initialPosition1
     * @return The InitialPosition1
     */
    public Position getInitialPosition1() {
        return initialPosition1;
    }

    /**
     * Gets the finalPosition2
     * @return The finalPosition2
     */
    public Position getFinalPosition1() {
        return finalPosition1;
    }

    /**
     * Gets the initialPosition2
     * @return The InitialPosition2
     */
    public Position getInitialPosition2() {
        return initialPosition2;
    }

    /**
     * Gets the finalPosition2
     * @return The finalPosition2
     */
    public Position getFinalPosition2() {
        return finalPosition2;
    }

}
