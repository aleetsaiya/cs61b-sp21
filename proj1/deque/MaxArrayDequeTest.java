package deque;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;

public class MaxArrayDequeTest {
    private class MaxNumberComparator implements Comparator<Integer> {
        /**
         * Just only support Number or String
         * @param o1 the first object to be compared.
         * @param o2 the second object to be compared.
         * @return
         */
        @Override
        public int compare(Integer o1, Integer o2) {
            if (o1 > o1) {
                return 1;
            }
            else if (o1 == o2) {
                return 0;
            }
            else {
                return -1;
            }
        }
    }

    private class MinNumberComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer n1, Integer n2) {
            if (n1 > n2) {
                return -1;
            }
            else if (n1 == n2) {
                return 0;
            }
            else {
                return 1;
            }
        }
    }
    @Test
    public void maxTest() {
        MaxNumberComparator c = new MaxNumberComparator();
        MaxArrayDeque<Integer> mad1 = new MaxArrayDeque<Integer>(c);
        MaxArrayDeque<Integer> mad2 = new MaxArrayDeque<Integer>(c);

        int N = 10000;
        for (int i = 0; i < N; i++) {
            mad1.addLast(i);;
            mad2.addLast(i);
        }

        for (int i = 0; i < 300; i++) {
            mad2.removeLast();
        }

        assertEquals(N - 1, (int)mad1.max());
        assertEquals(N - 1 - 300, (int)mad2.max());
    }

    @Test
    public void givenComparatorMaxTest() {
        MaxNumberComparator c = new MaxNumberComparator();
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<Integer>(c);
        int N = 10000;
        for (int i = 0; i < N; i++) {
            mad.addLast(i);
        }

        MinNumberComparator mc = new MinNumberComparator();
        int min = mad.max(mc);
        assertEquals(0, min);
    }
}
