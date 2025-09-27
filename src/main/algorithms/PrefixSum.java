package main.algorithms;

@SuppressWarnings("unused")
public class PrefixSum {

    private final long[] preSum;

    public PrefixSum(int[] a) {
        int n = a.length;
        preSum = new long[n + 1];
        for (int i = 0; i < n; i++) preSum[i + 1] = preSum[i] + a[i];
    }

    public long rangeSumInclusive(int l, int r) {
        return preSum[r + 1] - preSum[l];
    }

    public static class PrefixSum2D {
        private final long[][] preSum2D;

        public PrefixSum2D(int[][] a) {
            int m = a.length;
            int n = a[0].length;
            preSum2D = new long[m + 1][n + 1];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    preSum2D[i + 1][j + 1] = preSum2D[i + 1][j] + preSum2D[i][j + 1] - preSum2D[i][j] + a[i][j];
                }
            }
        }

        public long rangeSumInclusive(int i1, int j1, int i2, int j2) {
            return preSum2D[i2 + 1][j2 + 1] - preSum2D[i1][j2 + 1] - preSum2D[i2 + 1][j1] + preSum2D[i1][j1];
        }

    }
}