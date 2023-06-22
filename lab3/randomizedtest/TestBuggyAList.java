package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    private static void assertListEqual(AListNoResizing expected, BuggyAList actual) {
        assertEquals(expected.size(), actual.size());

        int size = expected.size();
        for (int i = 0; i < size; i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing al = new AListNoResizing();
        BuggyAList bal = new BuggyAList();

        al.addLast(4);
        bal.addLast(4);

        al.addLast(5);
        bal.addLast(5);

        al.addLast(6);
        bal.addLast(6);

        al.removeLast();
        bal.removeLast();
        assertListEqual(al, bal);

        al.removeLast();
        bal.removeLast();
        assertListEqual(al, bal);

        al.removeLast();
        bal.removeLast();
        assertListEqual(al, bal);
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList BL = new BuggyAList();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            // Get a random number to decide which operation should implement
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                BL.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                assertEquals(L.size(), BL.size());
            }
            else if (L.size() > 0 && operationNumber == 2) {
                // getLast
                assertEquals(L.getLast(), BL.getLast());
            }
            else if (L.size() > 0 && operationNumber == 3) {
                // removeLast
                assertEquals(L.removeLast(), BL.removeLast());
            }
        }
    }
}
