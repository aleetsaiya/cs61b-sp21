package deque;

/**
 * Invariants:
 *   1. First item is located at nextFirst + 1
 *   2. Last item is located at nextLast - 1
 *   3. Items will be continuous from the first item to the last item (no empty block between them)
 */
public class ArrayDeque<T> implements Deque<T>{
    private int size;
    private T[] items;
    private int nextFirst;
    private int nextLast;


    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }

    /* Add some extra spaces between first item and last item */
    private void resize(int capacity) {
        T[] nitems = (T[]) new Object[capacity];
        // Find the first item
        int firstItemIdx = nextFirst + 1;
        if (firstItemIdx == items.length) {
            firstItemIdx = 0;
        }
        /* Copy items from items to nitems
           Because items will be continuous from first to last. When we copy "size" number of items
           from the first item, it will copy all the items from first to last item
        */
        int counter = 0;
        int L = items.length;
        while (counter < size) {
            nitems[counter] = items[(firstItemIdx + counter) % L];
            counter += 1;
        }
        // Update items array to the new items
        items = nitems;
        // Update index information
        nextFirst = nitems.length - 1;
        nextLast = size;
    }

    private boolean isFulled() {
        return size == items.length;
    }

    @Override
    public void addFirst(T item){
        items[nextFirst] = item;
        size += 1;
        // Update nextFirst
        nextFirst -= 1;
        if (nextFirst < 0) {
            nextFirst = items.length - 1;
        }
        // Check if the items is fulled
        if (isFulled()) {
            resize(2 * items.length);
        }
    };

    @Override
    public void addLast(T item){
        items[nextLast] = item;
        size += 1;
        // Update nextLast
        nextLast += 1;
        if (nextLast >= items.length) {
            nextLast = 0;
        }
        // Check if the items is fulled
        if (isFulled()) {
            resize(2 * items.length);
        }
    };

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size(){
        return size;
    };

    @Override
    public void printDeque(){
        int firstItemIndex = nextFirst + 1;
        if (firstItemIndex == items.length) {
            firstItemIndex = 0;
        }
        int counter = 0;
        int L = items.length;
        while (counter < size) {
            System.out.print(items[(firstItemIndex + counter) % L] + " ");
            counter += 1;
        }
        System.out.println();
    };

    @Override
    public T removeFirst(){
        if (size == 0) {
            return null;
        }
        int index = (nextFirst + 1) % items.length;
        T target = items[index];
        items[index] = null;
        nextFirst = index;
        size -= 1;

        if (items.length / 4 >= size) {
            resize(items.length / 4);
        }
        return target;
    };

    @Override
    public T removeLast(){
        if (size == 0) {
            return null;
        }
        int index = nextLast - 1;
        if (index < 0) {
            index = items.length - 1;
        }
        T target = items[index];
        items[index] = null;
        nextLast = index;
        size -= 1;

        if (items.length / 4 >= size) {
            resize(items.length / 4);
        }
        return target;
    };

    /**
     *
     * @param index index 0 equals to the first item
     * @return
     */
    @Override
    public T get(int index){
        if (index >= size) {
            return null;
        }
        int L = items.length;
        int itemsCountToEnd = L - (nextFirst + 1);
        if (index < itemsCountToEnd) {
            return items[nextFirst + 1 + index];
        }
        else {
            return items[index - itemsCountToEnd];
        }
    };

    public double memoryUsage() {
        return size / (double)items.length;
    }
}
