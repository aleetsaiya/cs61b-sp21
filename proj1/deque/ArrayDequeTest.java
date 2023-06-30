package deque;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Random;

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
    /* Test if get method is correct */
    public void getTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<Integer>();
        ad.addLast(10);
        ad.addLast(20);
        ad.addFirst(5);
        ad.addLast(30);
        ad.addFirst(2);

        assertEquals(2, (int)ad.get(0));
        assertEquals(10, (int)ad.get(2));
        assertEquals(30, (int)ad.get(4));
        assertNull(ad.get(5));
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
        System.out.println(memoryUsage);
    }
}
