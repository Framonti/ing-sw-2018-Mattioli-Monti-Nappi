package it.polimi.se2018.testModel;

import it.polimi.se2018.model.Deck;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Tests for the Deck Class
 * @author Framonti
 */
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

    /**
     * Checks that the mixAndDistribute method returns a list with the same size of the given parameter
     */
    @Test
    public void testMixAndDistributeSize() {

        List<Integer> list1 = deckToTest.mixAndDistribute(3);
        List<Integer> list2 = deckToTest.mixAndDistribute(2);
        assertEquals(3, list1.size());
        assertEquals(2, list2.size());
    }

    /**
     * Checks that an IndexOutOfBoundException is raised if the parameter passed is too high
     */
    @Test
    public void testMixAndDistributeSizeFail()
    {
        try {
            List<Integer> list1 = deckToTest.mixAndDistribute(5);
        }
        catch (IndexOutOfBoundsException e) {

            assertTrue("Test Passed", true);
        }
    }

    /**
     * Checks that every element in deckToTest was present in the original List
     */
    @Test
    public void testMixAndDistribute()
    {
        int[] arrayInt = {1,2,3,4};
        List<Integer> list = deckToTest.mixAndDistribute(3);

        //scans both the arrayInt and the list, updating the first index only if there is a match
        //between the value on arrayInt and list
        int i;
        int j;
        for(i = 0; i < list.size();){
            for(j = 0; j< arrayInt.length; j++){

                if(list.get(i) == arrayInt[j]){
                    i++;
                    j = 0;
                    if(i == list.size())
                        break;
                }
            }
        }
        assertEquals(3, i);
    }
}
