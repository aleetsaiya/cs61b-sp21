package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> {
    // Using ArrayDeque as the data structure behind MaxArrayDeque
    private ArrayDeque<T> ad;
    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c ) {
        this.ad = new ArrayDeque<T>();
        this.comparator = c;
    }

    /**
     * Find the max item by the default Comparator
     * @return max item
     */
    public T max() {
        return max(this.comparator);
    }

    /**
     * Find the max item by the given Comparator
     * @param c comparator to compare which item is bigger
     * @return max item
     */
    public T max(Comparator<T> c) {
        int size = ad.size();
        if (size == 0) {
            return null;
        }
        int index = 1;
        T maxItem = ad.get(0);
        while (index < size) {
            T currentItem = ad.get(index);
            if (c.compare(maxItem, currentItem) < 0) {
                maxItem = currentItem;
            }
            index += 1;
        }
        return maxItem;
    }

    public void addLast(T item) { ad.addLast(item); }

    public void addFirst(T item) { ad.addFirst(item); }

    public T removeLast() { return ad.removeLast(); };

    public T removeFirst() { return ad.removeFirst(); }

    public int size() { return ad.size(); }

    public boolean isEmpty() { return ad.isEmpty(); }

    public void printDeque() { ad.printDeque(); }

    /* Same methods as ArrayDeque */
    public T get(int index) { return ad.get(index); }

    public double memoryUsage() { return ad.memoryUsage(); }
}
