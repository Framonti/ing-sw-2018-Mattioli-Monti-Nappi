package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Generic class used for the Card decks */
public class Deck<T> {

    private List<T> cardDeck;

    public Deck(List<T> deck) {

        this.cardDeck = deck;
    }

    /**The card list is randomly shuffled*/
    private void mix() {

        Collections.shuffle(cardDeck);
    }

    /**Extract the first "numberToExtract" cards from a deck
     * @param numberToExtract will be the length of the list returned
     * @return a list composed by the first "numberToExtract" cards */
    private List<T> distribute(int numberToExtract) {

        List<T> listToReturn = new ArrayList<>();
        for (int i = 0; i < numberToExtract; i++) {

            listToReturn.add(cardDeck.get(i));
        }
        return listToReturn;
    }

    /** mix and then return the first "numberToExtract" cards from a deck
     * @return a List with "numberToExtract" elements
     */
    public List<T> mixAndDistribute(int numberToExtract) {

        this.mix();
        return this.distribute(numberToExtract);
    }
}


