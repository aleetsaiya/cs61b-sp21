package HOC;

public class HOC {
    public static int doTwice(HOCInterface f, int x) {
        return f.apply(f.apply(x));
    }

    public static void main(String[] args) {
        HOCInterface f = new HOCTargetFunction();
        int res = doTwice(f, 50);
        System.out.println(res);
    }
}
