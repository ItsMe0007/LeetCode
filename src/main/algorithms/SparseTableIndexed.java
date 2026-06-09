package main.algorithms;

@SuppressWarnings({"unused", "DuplicatedCode"})
public class SparseTableIndexed {
    private final int[] arr;
    private final int n;
    private final int maxLog;
    private final int[][] table;
    private final int[] log2;

    public SparseTableIndexed(int[] arr) {
        this.arr = arr;
        this.n = arr.length;
        this.log2 = new int[n + 1];
        populateLog2(n);
        this.maxLog = 1 + log2[n];
        this.table = new int[maxLog][n];
        buildTable();
    }

    private void populateLog2(int n) {
        log2[1] = 0;
        for (int i = 2; i <= n; i++) {
            log2[i] = log2[i >> 1] + 1;
        }
    }

    private void buildTable() {
        for (int i = 0; i < n; i++) table[0][i] = i;

        for (int k = 1; k < maxLog; k++) {
            int blockLen = 1 << k;
            int halfLen = blockLen >> 1;
            for (int i = 0; i + blockLen - 1 < n; ++i) {
                table[k][i] = merge(table[k - 1][i], table[k - 1][i + halfLen]);
            }
        }
    }

    public int query(int startIdx, int endIdx) {
        int length = endIdx - startIdx + 1;
        int k = log2[length];
        int leftBlock = table[k][startIdx];
        int rightBlock = table[k][endIdx - (1 << k) + 1];
        return merge(leftBlock, rightBlock);
    }

    public int merge(int left, int right) {
        return arr[left] <= arr[right] ? left : right;
    }
}