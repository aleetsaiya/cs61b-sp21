public class TestListsExercises {
    @org.junit.Test
    public void testincrList() {
        IntList before = new IntList(3, null);
        before = new IntList(2, before);
        before = new IntList(1, before);

        int incr = 5;
        IntList after = Lists1Exercises.incrList(before, incr);

        IntList p1 = after;
        int[] expected1 = new int[] {6, 7, 8};
        int counter = 0;

        // Expected linked list value is added after invoking the function
        while (p1 != null) {
            org.junit.Assert.assertEquals(expected1[counter], p1.first);
            p1 = p1.rest;
            counter += 1;
        }
        // Expected the linked list size has not been changed
        org.junit.Assert.assertEquals(3, counter);

        // Expected the original linked list has not been modified
        p1 = before;
        int[] expected2 = new int[]{1, 2, 3};
        counter = 0;
        while (p1 != null) {
            org.junit.Assert.assertEquals(expected2[counter], p1.first);
            p1 = p1.rest;
            counter += 1;
        }
        // Expected the original linked list size has not been modified
        org.junit.Assert.assertEquals(3, counter);
    }

    @org.junit.Test
    public void testDincrList() {
        IntList L = new IntList(5, null);
        L = new IntList(4, L);
        L = new IntList(3, L);

        int incr = 5;

        int[] expected = new int[]{3 + incr, 4 + incr, 5 + incr};
        int counter = 0;
        IntList p = Lists1Exercises.dincrList(L, incr);
        while (p != null) {
            org.junit.Assert.assertEquals(expected[counter], p.first);
            counter += 1;
            p = p.rest;
        }
    }
}
