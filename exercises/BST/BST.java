class BST {
    private Key key;
    private BST left;
    private BST right;

    public BST (Key key, BST left, BST right){
        this.key = key;
        this.left = left;
        this.right = right;
    }

    public BST(Key key) {
        this.key = key;
    }

    public static BST find(BST T, Key sk) {
        if (T == null) {
           return null;
        }
        if (sk.equals(T.key)) {
            return T;
        }
        else if (T.key < sk) {
            return BST.find(T.right, sk);
        }
        else {
            return BST.find(T.left, sk);
        }
    }

    public static BST insert(BST T, Key ik) {
        if (T == null) {
            return new BST(ik);
        }
        if (ik > T.key) {
            T.right = insert(T.right, ik);
        }
        else if (ik < T.key) {
            T.left = insert(T.left, ik);
        }
        return T;
    }
}