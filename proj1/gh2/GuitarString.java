package gh2;

 import deque.Deque;
 import deque.LinkedListDeque;
// TODO: maybe more imports

//Note: This file will not compile until you complete the Deque implementations
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday. */
    // Sampling Rat: How many snapshots per second from waveform to digital sound
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private Deque<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    // Frequency: Different sound have different frequency, the higher sound have higher frequency
    // like the A above middle C on a piano is commonly tuned to 440Hz, meaning that it vibrate
    // at a frequency of 440 cycles per second
    public GuitarString(double frequency) {
        buffer = new LinkedListDeque<Double>();
        int capacity = (int)Math.round(SR / frequency);
        for (int i = 0; i < capacity; i++) {
            buffer.addLast(0.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        int size = buffer.size();
        int counter = 0;
        while (counter < size) {
            buffer.removeFirst();
            counter += 1;
        }
        counter = 0;
        while (counter < size) {
            buffer.addLast(Math.random() - 0.5);
            counter += 1;
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        double dequed = buffer.removeFirst();
        double first = buffer.get(0);
        buffer.addLast(((dequed + first) / 2) * DECAY); ;
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.get(0);
    }
}
