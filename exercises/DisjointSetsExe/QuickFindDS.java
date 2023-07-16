public class QuickFindDS implements DisjointSets{
    private int[] id;

    public QuickFindDS(int N) {
        // Each item do not connect to any others items at first. All of them are independent.
        id = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;
        }
    }

    @Override
    public void connect(int p, int q) {
        // make every item which id is p's id to q's id
        int pid = id[p];
        int qid = id[q];
        for (int i = 0; i < id.length; i++) {
            if (id[i] == pid) {
                id[i] = qid;
            }
        }
    }

    @Override
    public boolean isConnected(int p, int q) {
       // check whether p's id and q's id are same
       return id[p] == id[q];
    }
}
