package it.polimi.se2018.events.vcevent;

public class LensCutterEvent extends VCEvent {
    private int roundIndex;  //è quello che nel controller avevo chiamato round
    private int diceIndexInRoundTrack; //è quello che nel controller avevo chiamato indexOfRoundTrack
    private int diceIndexInDraftPool; //è quello che nel controller avevo chiamato indexOfDraftPool


    // ASSUMO LA STRINGA SIA DEL TIPO: INDICE NELLA DRAFT POOL , ROUND, INDICE DELL'ARRAY PRESENTE NELLA POSIZIONE ROUND DEL ROUNDTRACK
    public LensCutterEvent(String userInput){
        super(5);
        try{
            String[] parameters = userInput.split("\\s+");
            diceIndexInDraftPool = Integer.parseInt(parameters[0]) -1;
            roundIndex = Integer.parseInt(parameters[1]) -1;
            diceIndexInRoundTrack = Integer.parseInt(parameters[2]) -1;

        }
        catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Parametri non numerici o sbagliati");
        }catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Parametri insufficienti");
        }
    }

    public int getRoundIndex() {
        return roundIndex;
    }

    public int getDiceIndexInRoundTrack() {
        return diceIndexInRoundTrack;
    }

    public int getDiceIndexInDraftPool() {
        return diceIndexInDraftPool;
    }

}
