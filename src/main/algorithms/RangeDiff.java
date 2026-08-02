package main.algorithms;

@SuppressWarnings("unused")
public class RangeDiff {

    private final int min;
    private final int size;
    private final long[] diff;

    public RangeDiff(int min, int max) {
        this.min = min;
        this.size = max - min + 1;
        this.diff = new long[size + 1];
    }

    public RangeDiff(int size) {
        this(0, size - 1);
    }

    public void addRangeInclusive(int start, int endInclusive, int delta) {
        diff[start - min] += delta;
        diff[(endInclusive - min) + 1] -= delta;
    }

    public long[] toArray() {
        long[] res = new long[size];
        long runningSum = 0;

        for (int i = 0; i < size; ++i) {
            runningSum += diff[i];
            res[i] = runningSum;
        }
        return res;
    }
}
