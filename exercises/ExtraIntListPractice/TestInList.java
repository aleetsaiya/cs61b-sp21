public class TestInList {
    @org.junit.Test
    public void testSize() {
        IntList L = new IntList(15, null);
        L = new IntList(10, L);
        L = new IntList(5, L);

        int expected = 3;
        int actual = L.size();

        org.junit.Assert.assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGet() {
        IntList L = new IntList(4, null);
        L = new IntList(3, L);
        L = new IntList(2, L);
        L = new IntList(1, L);

        int expected1 = 2;
        int actual1 = L.get(1);

        int expected2 = 3;
        int actual2 = L.get(2);

        org.junit.Assert.assertEquals(expected1, actual1);
        org.junit.Assert.assertEquals(expected2, actual2);
    }
}
