package deque;

import java.util.Iterator;

/**
 * Invariants:
 *   1. head is sentinel node with dummy value inside
 *   2. first item is head.next
 *   3. last item is head.prev
 */
public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private DoublyNode<T> head;
    private int size;

    private class DoublyNode<T> {
        private T item;
        private DoublyNode<T> prev;
        private DoublyNode<T> next;


        DoublyNode() {
            item = null;
            prev = null;
            next = null;
        }

        DoublyNode(T item, DoublyNode<T> prev, DoublyNode<T> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }

    }

    /**
     * Create an empty linked list deque.
     */
    public LinkedListDeque() {
        DoublyNode<T> sentinel = new DoublyNode<T>(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        head = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        DoublyNode<T> N = new DoublyNode<T>(item, head, head.next);
        head.next.prev = N;
        head.next = N;
        size += 1;
    };

    @Override
    public void addLast(T item) {
        DoublyNode<T> N = new DoublyNode<T>(item, head.prev, head);
        head.prev.next = N;
        head.prev = N;
        size += 1;
    };

    @Override
    public boolean isEmpty() {
        return size == 0;
    };

    @Override
    public int size() {
        return size;
    };

    @Override
    public void printDeque() {
        DoublyNode<T> ptr = head.next;
        for (int i = 0; i < size; i++) {
            System.out.print(ptr.item + " ");
            ptr = ptr.next;
        }
        System.out.println();
    };

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T item = head.next.item;
        head.next.next.prev = head;
        head.next = head.next.next;
        size -= 1;
        return item;
    };

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T item = head.prev.item;
        head.prev = head.prev.prev;
        head.prev.next = head;
        size -= 1;
        return item;
    };

    @Override
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        int i = 0;
        DoublyNode<T> ptr = head.next;
        while (i < index) {
            ptr = ptr.next;
            i += 1;
        }
        return ptr.item;
    };

    private class LinkedListDequeIterator<T> implements Iterator<T> {
        private DoublyNode<T> sentinel;
        private DoublyNode<T> current;

        public LinkedListDequeIterator(DoublyNode sentinel) {
            this.sentinel = sentinel;
            this.current = sentinel.next;
        }

        @Override
        public boolean hasNext() {
            return current != sentinel;
        }

        @Override
        public T next() {
            T res = current.item;
            current = current.next;
            return res;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator<T>(head);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        else if (!(o instanceof Deque)) {
            return false;
        }
        Deque d = (Deque) o;
        if (d.size() != this.size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!get(i).equals(d.get(i))) {
                return false;
            }
        }
        return true;
    }


    private T getRecursive(DoublyNode<T> node, int index) {
        if (index == 0) {
            return node.item;
        }
        return getRecursive(node.next, index - 1);
    }

    /**
     * Get the item which located at the given index in the linked list, but uses recursion.
     * @param index
     * @return
     */
    public T getRecursive(int index) {
        return getRecursive(head.next, index);
    }

}
