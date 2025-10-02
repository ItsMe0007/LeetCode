package main.algorithms;

@SuppressWarnings("unused")
public abstract class PrefixSum2D {
    protected final long[][] ps;
    public static final long DEFAULT_MOD = 1_000_000_007L;

    private PrefixSum2D(long[][] ps) {
        this.ps = ps;
    }

    public static PrefixSum2D of(int[][] arr) {
        return new PlainPrefixSum2D(preparePS(arr));
    }

    public static PrefixSum2D ofMod(int[][] arr) {
        return ofMod(arr, DEFAULT_MOD);
    }

    public static PrefixSum2D ofMod(int[][] arr, long mod) {
        return new ModPrefixSum2D(preparePS(arr), mod);
    }

    public abstract long rangeSumInclusive(int i1, int j1, int i2, int j2);

    private static long[][] preparePS(int[][] arr) {
        int m = arr.length;
        int n = arr[0].length;
        long[][] ps = new long[m + 1][n + 1];
        for (int i = 0; i < m; i++) {
            long rowSum = 0;
            for (int j = 0; j < n; j++) {
                rowSum += arr[i][j];
                ps[i + 1][j + 1] = ps[i][j + 1] + rowSum;
            }
        }
        return ps;
    }

    private static final class PlainPrefixSum2D extends PrefixSum2D {
        private PlainPrefixSum2D(long[][] ps) {
            super(ps);
        }

        @Override
        public long rangeSumInclusive(int i1, int j1, int i2, int j2) {
            return ps[i2 + 1][j2 + 1] - ps[i1][j2 + 1] - ps[i2 + 1][j1] + ps[i1][j1];
        }
    }

    private static final class ModPrefixSum2D extends PrefixSum2D {
        private final long mod;

        private ModPrefixSum2D(long[][] ps, long mod) {
            super(ps);
            this.mod = mod;
        }

        @Override
        public long rangeSumInclusive(int i1, int j1, int i2, int j2) {
            long res = (ps[i2 + 1][j2 + 1] - ps[i1][j2 + 1] - ps[i2 + 1][j1] + ps[i1][j1]) % mod;
            if (res < 0) res += mod;
            return res;
        }
    }
}
