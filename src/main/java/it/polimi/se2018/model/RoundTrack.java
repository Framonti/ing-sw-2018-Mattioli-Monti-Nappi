package it.polimi.se2018.model;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Position;

import java.util.ArrayList;


public class RoundTrack {

    private ArrayList<ArrayList<Dice>> roundTrack;

    //Constructor
    public RoundTrack() {
        roundTrack = new ArrayList<ArrayList<Dice>>();
    }

    //return array list corresponding to round position
    public ArrayList<Dice> getArrayList(int round){
        return roundTrack.get(round);
    }

    //add dice
    public void addDice( int round, Dice dice){
        roundTrack.get(round).add(dice);
    }

    //adds the remaining dices in the draft pool to the RoundTrack
    public void addDices(ArrayList<Dice> dices) {
        roundTrack.add(dices);
    }

    //prints the dices on the RoundTrack of the selected round
    public void showArrayList(int round) {
        for(Dice dice : roundTrack.get(round - 1)) {
            System.out.printf("%s%d\t", dice.getColour().getAbbreviation(), dice.getValue());
        }
        System.out.print("\n");
    }

    public String roundToString(int round) {
        if(roundTrack.size() < round) {
            return "";
        }
        String tmp = "";
        for(Dice dice: roundTrack.get(round - 1))
            tmp = tmp.concat(dice.toString() + "\t");
        return tmp;
    }

    public void showRoundTrack() {}   //graphic only method

    //compares the current round with the one chosen and returns if there are any dices on that cell
    public boolean isEmpty(int round, int position) { return round <= position; }

    //returns the sum of every dice on the RoundTrack (single player only)
    public int sumDiceValue() {
        int sum = 0;
        for(ArrayList<Dice> dices : roundTrack){
            for(Dice dice : dices) {
                sum += dice.getValue();
            }
        }
        return sum;
    }

    //returns the dice on the roundIndex cell in the arrayListIndex position
    public Dice getDice(int roundIndex, int arrayListIndex) { return roundTrack.get(roundIndex).get(arrayListIndex); }


}
