public class SLList {
    public class IntNode {
        public int item;
        public IntNode next;
        public IntNode(int i, IntNode n) {
            item = i;
            next = n;
        }
    }

    private IntNode first; 

    public SLList(int x) {
        first = new IntNode(x, null);
    }

    /** Adds an item to the front of the list. */
    public void addFirst(int x) {
        first = new IntNode(x, first);
    }    

    /** Retrieves the front item from the list. */
    public int getFirst() {
        return first.item;
    }

    /** Adds an item to the end of the list. */
    public void addLast(int x) {
        /* Your Code Here! */
        IntNode p = first;
        while (p.next != null) {
          p = p.next;
        }
        p.next = new IntNode(x, null);
    }

    private static int size(IntNode n) {
      if (n.next == null) {
        return 1;
      }
      return 1 + size(n.next);
    }

    /** Returns the number of items in the list using recursion. */
    public int size() {
        /* Your Code Here! */
        return size(first);
    }

    public static void main (String[] args) {
      SLList L = new SLList(3);
      L.addFirst(2);
      L.addFirst(1);
      L.addLast(4);
     

      IntNode p = L.first;
      while (p != null) {
        System.out.println("Sequence: " + p.item);
        p = p.next;
      }

       System.out.println("Size: " + L.size());
    }
}