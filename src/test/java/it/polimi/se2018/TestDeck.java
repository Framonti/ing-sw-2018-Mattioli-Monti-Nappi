package it.polimi.se2018;

import it.polimi.se2018.model.Deck;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestDeck {

    private int[] arrayInt = {1,2,3,4};
    private Deck<Integer> deckToTest;

    @Before
    public void DeckBuilder()
    {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        this.deckToTest = new Deck<>(list);
    }
    @Test
    public void testMixAndDistributeSize() {

        List<Integer> list1 = deckToTest.mixAndDistribute(3);
        List<Integer> list2 = deckToTest.mixAndDistribute(2);
        assertEquals(3, list1.size());
        assertEquals(2, list2.size());
    }

    @Test
    public void testMixAndDistribuiteSizeFail()
    {
        try {
            List<Integer> list1 = deckToTest.mixAndDistribute(5);
        }
        catch (ArrayIndexOutOfBoundsException e) {

            fail("Test Passato Correttamente");
        }
    }

    @Test
    public void testMixAndDistribute()
    {

        List<Integer> list1 = deckToTest.mixAndDistribute(3);
        //Check that every element in deckToTest was present in the original List
        int i;
        for(i = 0; i<list1.size();){
            for(int j = 0; j< arrayInt.length ;)
            {
                if(list1.get(i).equals(arrayInt[j])){
                    i++;
                    j = 0;
                    if(i == list1.size())
                        break;
                }
                else j++;
            }
        }
        assertEquals(3, i);
    }
}
