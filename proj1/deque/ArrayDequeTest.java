package deque;
import edu.princeton.cs.algs4.StdRandom;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    /* Add some items, check addFirst and addLast are correct */
    public void addTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();

        ad.addFirst(10);
        ad.addLast(20);
        ad.addFirst(0);
        ad.addLast(30);

        assertEquals(ad.size(), 4);
    }

    @Test
    public void removeTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<Integer>();

        ad.addFirst(10);
        ad.addFirst(5);
        ad.addLast(20);
        ad.addFirst(3);
        ad.addLast(30);

        assertEquals(3, (int)ad.removeFirst());
        assertEquals(5, (int)ad.removeFirst());
        assertEquals(30, (int)ad.removeLast());
        assertEquals(20, (int)ad.removeLast());
    }

    @Test
    /* Test remove from an empty deque */
    public void removeEmptyTest() {
        ArrayDeque<String> ad = new ArrayDeque<String>();

        String s1 = ad.removeLast();
        assertNull(s1);

        String s2 = ad.removeFirst();
        assertNull(s2);

        ad.addLast("Hello");
        ad.addLast("World");
        ad.addFirst("YO!");

        ad.removeLast();
        ad.removeFirst();
        ad.removeLast();
        ad.removeFirst();
        assertNull(ad.removeLast());
        assertNull(ad.removeFirst());

        assertEquals(0, ad.size());
    }

    @Test
    /* Test if you can create ArrayDeque with different types */
    public void multiParamTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        ArrayDeque<String> ad2 = new ArrayDeque<String>();
        ArrayDeque<Double> ad3 = new ArrayDeque<Double>();

        ad1.addFirst(1);
        ad2.addFirst("Hello World");
        ad3.addFirst(18.5);

        ad1.removeFirst();
        ad2.removeFirst();
        ad3.removeFirst();
    }

    @Test
    /* Test if addFirst can work correctly with removeLast, and addLast can work correctly with removeFirst */
    public void addRemoveTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        ArrayDeque<Integer> ad2 = new ArrayDeque<Integer>();

        ad1.addFirst(10);
        ad2.addLast(10);

        assertEquals(10, (int)ad1.removeLast());
        assertEquals(0, ad1.size());

        assertEquals(10, (int)ad2.removeFirst());
        assertEquals(0, ad2.size());
    }

    @Test
    /* Add large number of elements into ArrayDeque. Check the order is correct */
    public void bigADTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<Integer>();
        for (int i = 99999; i >= 0; i--) {
            ad.addFirst(i);
        }
        for (int i = 100000; i < 200000; i++) {
            ad.addLast(i);
        }
        for (int i = 0; i < 200000; i++) {
            assertEquals(i, (int)ad.removeFirst());
        }
    }

    @Test
    public void memoryUsageTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<Integer>();
        for (int i = 0; i < 10000; i++) {
            ad.addLast(i);
        }
        int itemsLeft = 9;
        int removeAmount = 10000 - itemsLeft;
        for (int i = 0; i < removeAmount; i++) {
            ad.removeLast();
        }
        double memoryUsage = ad.memoryUsage();
        assertTrue(memoryUsage >= 0.25);
    }

    @Test
    public void addFirstRemoveLastIsEmptyTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<Integer>();
        CorrectDeque<Integer> cd = new CorrectDeque<Integer>();
        for (int i = 0; i < 100000; i++) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                ad.addFirst(i);
                cd.addFirst(i);
            }
            else if (operationNumber == 1) {
                Object cd1 = cd.removeLast();
                Object ad1 = ad.removeLast();
                assertEquals(cd1, ad1);
            }
            else if (operationNumber == 2) {
                assertEquals(cd.size(), ad.size());
            }
            else if (operationNumber == 3) {
                assertEquals(cd.isEmpty(), ad.isEmpty());
            }
        }
    }

    @Test
    public void addFirstRemoveFirstIsEmptyTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<Integer>();
        CorrectDeque<Integer> cd = new CorrectDeque<Integer>();

        for (int i = 0; i < 100000; i++) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                ad.addFirst(i);
                cd.addFirst(i);
            }
            else if (operationNumber == 1) {
                assertEquals(cd.size(), ad.size());
            }
            else if (operationNumber == 2) {
                assertEquals(cd.removeFirst(), ad.removeFirst());
            }
            else if (operationNumber == 3) {
                assertEquals(cd.isEmpty(), ad.isEmpty());
            }
        }
    }

    @Test
    public void fillUpEmptyFillUpAgain() {
        ArrayDeque<Integer> ad = new ArrayDeque<Integer>();
        CorrectDeque<Integer> cd = new CorrectDeque<Integer>();
        for (int i = 0; i < 100; i++) {
            ad.addLast(i);
            cd.addLast(i);
        }
        for (int i = 0; i < 100; i++) {
            assertEquals(cd.removeLast(), ad.removeLast());
        }
        assertEquals(cd.isEmpty(), ad.isEmpty());
        for (int i = 0; i < 100; i++) {
            ad.addFirst(i);
            cd.addFirst(i);
        }
        for (int i = 0; i < 100; i++) {
            assertEquals(cd.get(i), ad.get(i));
        }
        assertEquals(cd.isEmpty(), ad.isEmpty());
    }

    @Test
    /* Test if get method is correct */
    public void getTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<Integer>();
        CorrectDeque<Integer> cd = new CorrectDeque<Integer>();
        for (int i = 0; i < 100000; i++) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {
                ad.addLast(i);
                cd.addLast(i);
            }
            else if (operationNumber == 1) {
                assertEquals(cd.removeLast(), ad.removeLast());
            }
            else if (operationNumber == 2) {
                assertEquals(cd.size(), ad.size());
                if (cd.size() == 0) {
                    cd.addFirst(i);
                    ad.addFirst(i);
                }
                int randomIdx = StdRandom.uniform(0, cd.size());
                assertEquals(cd.get(randomIdx), ad.get(randomIdx));
            }
        }
    }

    @Test
    public void iterableTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<Integer>();
        for (int i = 0; i < 5; i++) {
            ad.addLast(i);
        }
        int[] res = new int[5];
        for (int i : ad) {
            res[i] = i;
        }
        int[] expected = new int[]{0, 1, 2, 3, 4};
        assertArrayEquals(expected, res);
    }
}
