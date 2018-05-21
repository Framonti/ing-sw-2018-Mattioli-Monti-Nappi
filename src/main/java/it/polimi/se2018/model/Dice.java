package it.polimi.se2018.model;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class represents a dice with a colour and gives the more basic
 * methods for its manipulation
 * @author Framonti
 */
public class Dice
{
    private int value;
    private Colour colour;

    /**
     * Creates a dice with a specific colour
     * @param colour The Colour the dice will have
     */
    public Dice(Colour colour) {

        this.colour = colour;
    }

    /**
     * Creates a dice with a specific value but no colour.
     * This constructor will be only used for the windowPatterns
     * @param value The initial value of the dice
     */
    public Dice(int value) {

        this.value = value;
    }

    /**
     * Sets a specific value for this Dice
     * @param value The value to set
     * @throws IllegalArgumentException if the value required is less than 1 or more than 6
     */
    public void setValue(int value){

        if(value > 6 || value < 1)
            throw new IllegalArgumentException("Invalid value");
        else
            this.value = value;
    }

    /**
     * Gets the dice's colour
     * @return The dice's colour
     */
    public Colour getColour() {

        return colour;
    }

    /**
     * Gets the dice's value
     * @return The dice's value
     */
    public int getValue() {

        return value;
    }

    /**
     * Decreases the value of the dice by one
     * @throws IllegalArgumentException if the dice's value is 1
     */
    public void subOne() {

        if (this.value == 1)
            throw new IllegalArgumentException("Dice's value can't be decreased");

        else this.value --;
    }

    /**
     * Increases the value of the dice by one
     * @throws IllegalArgumentException if the dice's value is 6
     */
    public void addOne() {

        if (this.value == 6)
            throw new IllegalArgumentException("Dice's value can't be increased");

        else this.value ++;
    }

    /**
     * Turns the dice on the opposite side
     * A real dice has the sum of the opposite side constant (7 for a D6)
     */
    public void turn() {

        int sumOppositeFaces = 7;
        this.value = sumOppositeFaces - this.value;
    }

    /**
     * Randomly rolls the dice
     */
    public void roll() {

        this.value = ThreadLocalRandom.current().nextInt(1, 7);
    }

    /**
     * Rolls the dice and returns the result
     * @return The value of the dice after a roll
     */
    public int rollAndGet() {

        this.roll();
        return this.getValue();
    }

    @Override
    public String toString() {

        String temp = String.valueOf(this.value);

        if(this.colour == null)
            return temp;

        else if (this.value == 0)
            return this.colour.getAbbreviation();

        else return temp.concat(this.colour.getAbbreviation());
    }

}

