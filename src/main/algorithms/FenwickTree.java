package main.algorithms;

@SuppressWarnings("unused")
public class FenwickTree {
    private final int treeSize;
    private final long[] tree;

    public FenwickTree(int n) {
        this.treeSize = n + 1;
        this.tree = new long[n + 1];
    }

    public FenwickTree(int[] values) {
        this(values.length);
        for (int i = 0; i < values.length; i++) {
            tree[i + 1] = values[i];
        }

        for (int i = 1; i < treeSize; i++) {
            int p = i + (i & -i);
            if (p < treeSize) tree[p] += tree[i];
        }
    }

    private void add(int idx, long delta) {
        while (idx < treeSize) {
            tree[idx] += delta;
            idx += idx & -idx;
        }
    }

    private long prefixSum(int idx) {
        long sum = 0;
        while (idx > 0) {
            sum += tree[idx];
            idx -= idx & -idx;
        }
        return sum;
    }

    public long rangeSum(int start, int endInclusive) {
        return prefixSum(endInclusive + 1) - prefixSum(start);
    }

    public long get(int idx) {
        return prefixSum(idx + 1) - prefixSum(idx);
    }

    public void update(int idx, long val) {
        addDelta(idx, val - get(idx));
    }

    public void addDelta(int idx, long delta) {
        add(idx + 1, delta);
    }
}