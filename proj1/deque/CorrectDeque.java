package deque;

public class CorrectDeque<T> implements Deque<T> {
    private T[] items;
    private int size;
    private final int CAPACITY = 100000;

    public CorrectDeque() {
        items = (T[]) new Object[CAPACITY];
    }

    public void addFirst(T item) {
        for (int i = size - 1; i >= 0; i--) {
            items[i + 1] = items[i];
        }
        items[0] = item;
        size += 1;
    }

    public void addLast(T item) {
        items[size] = item;
        size += 1;
    }


    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(items + " ");
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T res = items[0];
        for (int i = 1; i < size; i++) {
            items[i - 1] = items[i];
        }
        size -= 1;
        return res;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T res = items[size - 1];
        items[size - 1] = null;
        size -= 1;
        return res;
    }

    public T get(int index) {
        return items[index];
    }
}
