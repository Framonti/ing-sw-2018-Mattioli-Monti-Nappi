package it.polimi.se2018.model;

import it.polimi.se2018.events.mvevent.RoundTrackEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents the round track
 * @author fabio
 */

public class RoundTrack {

    private List<List<Dice>> roundList;

    //Constructor
    public RoundTrack() {
        roundList = new ArrayList<>();
    }

    /**
     * @param round Represents the number of the chosen round
     * @return A list of the dices placed on the round track on the selected round
     * @throws IndexOutOfBoundsException If the requested round is bigger than the current one
     */
    public List<Dice> getList(int round) {
        if(isEmpty(round))
            throw new IndexOutOfBoundsException("The selected round has no dice");
        return roundList.get(round);
    }

    /**
     * @param roundIndex Represents the number of the chosen round
     * @param listIndex Represents the number of the dice in the chosen round
     * @return The chosen dice
     * @throws IndexOutOfBoundsException If listIndex is bigger than the size of the list
     */
    public Dice getDice(int roundIndex, int listIndex) {
        if (getList(roundIndex).size() <= listIndex)
            throw new IndexOutOfBoundsException("There is no dice in the selected position");
        return getList(roundIndex).get(listIndex);
    }

    /**
     * To be used only while using a tool card.
     * @param round Represents the number of the chosen round
     * @param dice Represents the dice that has to be placed on the round track
     */
    public void addDice(int round, Dice dice){
        roundList.get(round).add(dice);
    }

    /**
     * Adds the remaining dices of the draft pool to the round track
     * @param dices The list of dices remained in the draft pool at the end of the round
     */
    public void addDices(List<Dice> dices) {
        List<Dice> tmp = new ArrayList<>(dices);
        roundList.add(tmp);
        GameSingleton.getInstance().myNotify(new RoundTrackEvent(toString(), toStringPath()));
    }

    /**
     * @param round Represents the number of the chosen round
     * @return A string that represents the dices on the round track on the chosen round
     */
    public String roundToString(int round) {
        if(isEmpty(round))
            return "";
        String tmp = "";
        for(Dice dice: roundList.get(round))
            tmp = tmp.concat(dice.toString() + "\t");
        return tmp;
    }

    /**
     * @param requestedRound Represents the number of the round requested by the player
     * @return True if the size of roundTrack is smaller than the requested round
     */
    private boolean isEmpty(int requestedRound) { return roundList.size() <= requestedRound; }

    /**
     * This method will be used only in single player mode.
     * @return The sum of all the dices' value on the round track
     */
    public int sumDiceValue() {
        int sum = 0;
        for(List<Dice> dices : roundList){
            for(Dice dice : dices) {
                sum += dice.getValue();
            }
        }
        return sum;
    }

    @Override
    public String toString() {
        String roundTrack = "\t\t\t1\t2\t3\t4\t5\t6\t7\t8\t9\n";
        int round;
        for(round = 0; round < 10; round++)
            roundTrack = roundTrack.concat("Round " + (round+1) + ":\t" + roundToString(round) + "\n");
        return roundTrack;
    }

    /**
     * Gets a List of path for the GUI representation of a round of the RoundTrack.
     * @param round the round to be represented
     * @return A List of path for the GUI representation of a round of the RoundTrack.
     */
    private List<String> roundToStringPath(int round){
        ArrayList<String> list = new ArrayList<>();
        for (Dice dice: roundList.get(round))
            list.add(dice.toStringPath());
        while (list.size() < 9)
            list.add(" ");
        return list;
    }

    /**
     * Gets a List of path for the GUI representation the whole RoundTrack.
     * @return A List of path for the GUI representation of the whole RoundTrack.
     */
    public List<String> toStringPath(){
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0 ; i < 10; i++){
            if(isEmpty(i)){
                for(int j = 0; j < 9 ; j++)
                    list.add(" ");
            }
            else
                list.addAll(roundToStringPath(i));
        }
        return list;
    }

    /**
     * Removes everything from the roundTrack
     */
    public void removeAll(){
        roundList.clear();
    }
}
