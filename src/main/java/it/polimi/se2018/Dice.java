package it.polimi.se2018;
import java.util.concurrent.ThreadLocalRandom;

public class Dice
{
    private int value;
    private final Colour colour;


    //Constructor
    public Dice(Colour colour)
    {
        this.colour = colour;
    }


    //getter and setter
    public void setValue(int value)
    {
        this.value = value;
    }

    public Colour getColour()
    {
        return colour;
    }

    public int getValue()
    {
        return value;
    }

    //decrease the value of the dice by one
    //@return false when this.value is equal to 1 (can't be decreased)
    public boolean subOne()
    {
        if (this.value == 1)
            return false;
        else
        {
            this.value --;
            return true;
        }
    }

    //increase the value of the dice by one
    //@return false when this.value is equal to 6 (can't be increased)
    public boolean addOne()
    {
        if (this.value == 6)
            return false;
        else
        {
            this.value ++;
            return true;
        }
    }

    //Turn the dice to the opposite side
    //A real dice has the sum of the opposite side constant (7 for a D6)
    public void turn()
    {
        switch (this.value)
        {
            case 1: setValue(6);
                break;
            case 2: setValue(5);
                break;
            case 3: setValue(4);
                break;
            case 4: setValue(3);
                break;
            case 5: setValue(2);
                break;
            case 6: setValue(1);
                break;
            default: setValue(getValue());
                break;
        }
    }

    //Roll the dice
    public void roll()
    {
        this.value = ThreadLocalRandom.current().nextInt(1, 7);
    }

    //Roll the dice and @return the result
    public int rollAndGet()
    {
        this.roll();
        return this.getValue();
    }
}

