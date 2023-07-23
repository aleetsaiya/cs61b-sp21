package bstmap;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable, V> implements Map61B<K, V>{
    private BSTNode root;
    private int size = 0;

    private class BSTNode implements Comparable{
        private K key;
        private V value;
        private BSTNode left;
        private BSTNode right;

        BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }

        public K getkey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setLeftChild() {
            this.left = left;
        }

        public void setRightChild() {
            this.right = right;
        }

        @Override
        public int compareTo(Object o) {
            if (this == o) {
                return 0;
            }
            if (!(o instanceof BSTMap.BSTNode)) {
                throw new Error("Could only compare to BSTNode");
            }
            BSTNode n = (BSTNode) o;
            return key.compareTo(n.getkey());
        }

        @Override
        public String toString() {
            return String.format("{%s: %s}", key.toString(), value.toString());
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    private boolean containsKey(BSTNode root, K key) {
        if (root == null) {
            return false;
        }
        if (root.getkey().compareTo(key) == 0) {
            return true;
        }
        else if (root.getkey().compareTo(key) > 0) {
            return containsKey(root.left, key);
        }
        else {
            return containsKey(root.right, key);
        }
    }

    @Override
    public boolean containsKey(K key) {
        return containsKey(root, key);
    }

    /** Return the value for the target key, return null if the key is not inside the map */
    private V get(BSTNode root, K key) {
        if (root == null) {
            return null;
        }
        if (root.getkey().compareTo(key) == 0) {
            return root.getValue();
        }
        else if (root.getkey().compareTo(key) > 0) {
            return get(root.left, key);
        }
        else {
            return get(root.right, key);
        }
    }

    @Override
    public V get(K key) {
        return get(root, key);
    }

    @Override
    public int size() {
        return size;
    }

    private void put (BSTNode root, BSTNode target) {
        if (root.compareTo(target) > 0) {
            if (root.left == null) {
                root.left = target;
                return;
            }
            put(root.left, target);
        }
        else if (root.compareTo(target) < 0) {
            if (root.right == null) {
                root.right = target;
                return;
            }
            put(root.right, target);
        }
        else {
            throw new Error("Could not put the same item " + target);
        }
    }

    @Override
    public void put(K key, V value) {
        if (root == null) {
            root = new BSTNode(key, value);
        }
        else {
            put(root, new BSTNode(key, value));
        }
        size += 1;
    }

    /** Print out the BSTMap in increasing key order */
    private void printInOrder(BSTNode root) {
        if (root == null) {
            return;
        }
        printInOrder(root.left);
        System.out.println(root);
        printInOrder(root.right);
    }

    public void printInOrder() {
        printInOrder(root);
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("KeySet operation is unsupported");
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("Remove operation is unsupported");
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("Remove operation is unsupported");
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException("Iterator operation is unsupported");
    }
}
