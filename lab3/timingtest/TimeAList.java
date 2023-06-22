package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        /**
         * Process:
         * 1. Instantiating a lot of ALists, each different ALists store different amount of elements
         * 2. Calculate the spending time for each AList (the initial maximum size within AList is 100)
         * 3. Using the same data structure (AList) to store the spending time information
         */
        int[] SIZES = new int[]{1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000};
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList();

        for (int SIZE : SIZES) {
            Ns.addLast(SIZE);
            AList<Integer> L = new AList<>();
            Stopwatch sw = new Stopwatch();
            for (int i = 0; i < SIZE; i++) {
                L.addLast(i);
            }
            times.addLast(sw.elapsedTime());
        }

        printTimingTable(Ns, times, Ns);
    }
}
