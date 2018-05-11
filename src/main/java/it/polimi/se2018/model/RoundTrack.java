package it.polimi.se2018.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents the round track
 * @author fabio
 */

public class RoundTrack {

    private ArrayList<ArrayList<Dice>> roundTrack;

    //Constructor
    public RoundTrack() {
        roundTrack = new ArrayList<ArrayList<Dice>>();
    }

    /**
     * @param round Represents the number of the chosen round
     * @return A list of the dices placed on the round track on the selected round
     */
    public List<Dice> getArrayList(int round){
        return roundTrack.get(round);
    }

    /**
     * To be used only while using a tool card.
     * @param round Represents the number of the chosen round
     * @param dice Represents the dice that has to be placed on the round track
     */
    public void addDice(int round, Dice dice){
        roundTrack.get(round).add(dice);
    }

    /**
     * Adds the remaining dices of the draft pool to the round track
     * @param dices The list of dices remained in the draft pool at the end of the round
     */
    public void addDices(ArrayList<Dice> dices) {
        roundTrack.add(dices);
    }

    /**
     * @param round Represents the number of the chosen round
     * @return A string that represents the dices on the round track on the chosen round
     */
    public String roundToString(int round) {
        if(roundTrack.size() < round) {
            return "";
        }
        String tmp = "";
        for(Dice dice: roundTrack.get(round - 1))
            tmp = tmp.concat(dice.toString() + "\t");
        return tmp;
    }

    //graphic only method
    public void showRoundTrack() {}

    /**
     * @param currentRound Represents the number of the current round
     * @param requestedRound Represents the number of the round requested by the player
     * @return True if the current round is smaller (or equal) than the requested round
     */
    public boolean isEmpty(int currentRound, int requestedRound) { return currentRound <= requestedRound; }

    /**
     * This method will be used only in single player mode.
     * @return The sum of all the dices' value on the round track
     */
    public int sumDiceValue() {
        int sum = 0;
        for(ArrayList<Dice> dices : roundTrack){
            for(Dice dice : dices) {
                sum += dice.getValue();
            }
        }
        return sum;
    }

    /**
     * @param roundIndex Represents the number of the chosen round
     * @param arrayListIndex Represents the number of the dice in the chosen round
     * @return The chosen dice
     */
    public Dice getDice(int roundIndex, int arrayListIndex) { return roundTrack.get(roundIndex).get(arrayListIndex); }

}
