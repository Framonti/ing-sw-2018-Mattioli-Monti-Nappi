package it.polimi.se2018;
import java.util.concurrent.ThreadLocalRandom;

public class Dice
{
    private int value;
    private Colour colour;

    //Constructors
    public Dice(Colour colour) {

        this.colour = colour;
    }

    //This constructor will be only used for the windowPatterns
    public Dice(int value) {

        this.value = value;
    }

    //getters and setter
    public void setValue(int value) {

        this.value = value;
    }

    public Colour getColour() {

        return colour;
    }

    public int getValue() {

        return value;
    }

    //decrease the value of the dice by one
    //@return false when this.value is equal to 1 (can't be decreased)
    public boolean subOne() {

        if (this.value == 1)
            return false;

        else {
            this.value --;
            return true;
        }
    }

    //increase the value of the dice by one
    //@return false when this.value is equal to 6 (can't be increased)
    public boolean addOne() {

        if (this.value == 6)
            return false;
        else {
            this.value ++;
            return true;
        }
    }

    //Turn the dice to the opposite side
    //A real dice has the sum of the opposite side constant (7 for a D6)
    public void turn() {

        int sumOppositeFaces = 7;
        this.value = sumOppositeFaces - this.value;
    }

    //Roll the dice
    public void roll() {

        this.value = ThreadLocalRandom.current().nextInt(1, 7);
    }

    //Roll the dice and @return the result
    public int rollAndGet() {

        this.roll();
        return this.getValue();
    }

    @Override
    //@return a quick representation of the dice, using its value and the initial of its colour
    public String toString() {

        String temp = String.valueOf(this.value);

        if(this.colour == null)
            return temp;

        else if (this.value == 0)
            return this.colour.getAbbreviation();

        else return temp.concat(this.colour.getAbbreviation());
    }

}

