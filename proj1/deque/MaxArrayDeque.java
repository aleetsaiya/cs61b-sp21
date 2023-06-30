package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    // Using ArrayDeque as the data structure behind MaxArrayDeque
    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        super();
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
        int size = size();
        if (size == 0) {
            return null;
        }
        int index = 1;
        T maxItem = get(0);
        while (index < size) {
            T currentItem = get(index);
            if (c.compare(maxItem, currentItem) < 0) {
                maxItem = currentItem;
            }
            index += 1;
        }
        return maxItem;
    }
}
