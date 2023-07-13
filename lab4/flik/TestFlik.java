package flik;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.Math;
public class TestFlik {
    @Test
    public void testSameNumber() {
        assertTrue(Flik.isSameNumber(128, 128));
    }

    @Test
    public void testMultipleSameNumbers() {
        int j = 0;
        for (int i = 0; i < 500; i++, j++) {
            assertTrue(Flik.isSameNumber(i , j));
        }
    }

    @Test
    public void randomTestSameNumbers() {
        int m = 0;
        int n = 0;
        for (int i = 0; i < 100000; i++) {
            int option = (int) (Math.random() * 4);
            if (option == 0) {
                m -= 1;
                n -= 1;
                assertTrue(Flik.isSameNumber(m, n));
            }
            else if (option == 1) {
                m += 1;
                n += 1;
                assertTrue(Flik.isSameNumber(m, n));
            }
            else if (option == 2) {
                m *= 2;
                n *= 2;
                assertTrue(Flik.isSameNumber(m, n));
            }
            else if (option == 3) {
                m /= 2;
                n /= 2;
                assertTrue(Flik.isSameNumber(m, n));
            }
        }
    }
}
