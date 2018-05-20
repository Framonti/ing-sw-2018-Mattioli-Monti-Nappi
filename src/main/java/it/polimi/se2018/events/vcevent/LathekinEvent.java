package it.polimi.se2018.events.vcevent;

import it.polimi.se2018.model.Position;

public class LathekinEvent extends VCEvent {
    private Position initialPosition1;
    private Position finalPosition1;
    private Position initialPosition2;
    private Position finalPosition2;

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

    public Position getInitialPosition1() {
        return initialPosition1;
    }

    public Position getFinalPosition1() {
        return finalPosition1;
    }

    public Position getInitialPosition2() {
        return initialPosition2;
    }

    public Position getFinalPosition2() {
        return finalPosition2;
    }

}
