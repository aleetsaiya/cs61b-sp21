/** Invariant:
 * 1. parent == -1 represents the item is the root item (do not have the parent item)
 * 2. parent != -1 represents the item has the parent item
 * */
public class QuickUnionDS implements DisjointSets{
    private int[] parent;

    public QuickUnionDS(int N) {
        // Each item do not connect to any others items at first
        parent = new int[N];
        for (int i = 0; i < N; i++) {
            parent[i] = -1;
        }
    }

    /** Find the root item by the given item */
    private int find (int p) {
        // If the item have parent, keep finding the parent item until the item is the root item
        if (parent[p] == -1) {
            return p;
        }
        return find(parent[p]);
    }

    @Override
    public void connect(int p, int q) {
        int pRoot = find(p);
        int qRoot = find(q);
        parent[pRoot] = qRoot;
    }

    @Override
    public boolean isConnected(int p, int q) {
        // find p and q root item then checking if they have the same root item
        int pRoot = find(p);
        int qRoot = find(q);

        if (pRoot == qRoot) {
            if (pRoot == -1) {
                return false;
            }
            return true;
        }
        else {
            return false;
        }
    }

}