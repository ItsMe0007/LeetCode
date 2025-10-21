package main.algorithms;

@SuppressWarnings("unused")
public class RangeDiff {

    public final int min;
    private final int size;
    private final int[] diff;

    public RangeDiff(int min, int max) {
        this.min = min;
        this.size = max - min + 1;
        this.diff = new int[size + 1];
    }

    public void addRangeInclusive(int start, int endInclusive, int delta) {
        diff[start - min] += delta;
        diff[(endInclusive - min) + 1] -= delta;
    }

    public int[] toArray() {
        int[] res = new int[size];
        int runningSum = 0;

        for (int i = 0; i < size; ++i) {
            runningSum += diff[i];
            res[i] = runningSum;
        }
        return res;
    }
}
