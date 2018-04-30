package it.polimi.se2018;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//This generic class will be used for the Card decks
public class Deck<T>
{

    private List<T> cardDeck;
    private final int numberToExtract;


    public Deck(int numberToExtract, List<T> deck)
    {
        this.numberToExtract = numberToExtract;
        this.cardDeck = deck;
    }

    //The list of card is randomly shuffled
    private void mix()
    {
        Collections.shuffle(this.cardDeck);
    }

    //Extract the first n card from a deck
    private List<T> distribute()
    {
        List<T> listToReturn = new ArrayList<>();
        for (int i = 0; i < this.numberToExtract; i++)
        {
            listToReturn.add(this.cardDeck.get(i));
        }
        return listToReturn;
    }

    public List<T> mixAndDistribute()
    {
        this.mix();
        return this.distribute();
    }
}


