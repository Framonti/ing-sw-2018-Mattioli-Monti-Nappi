package it.polimi.se2018;

import it.polimi.se2018.model.Deck;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TestDeck {

    private Deck<Integer> deckToTest;

    @Before
    public void setUp()
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
    public void testMixAndDistributeSizeFail()
    {
        try {
            List<Integer> list1 = deckToTest.mixAndDistribute(5);
        }
        catch (ArrayIndexOutOfBoundsException e) {

            fail("Test passed");
        }
    }

    @Test //TODO è sbagliato
    public void testMixAndDistribute()
    {
        int[] arrayInt = {1,2,3,4};
        List<Integer> list = deckToTest.mixAndDistribute(3);
        //Check that every element in deckToTest was present in the original List

        System.out.println(Arrays.asList((arrayInt)).containsAll(list));
        assertTrue(Arrays.asList(arrayInt).containsAll(list));
    }
}
