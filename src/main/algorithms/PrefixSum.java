package main.algorithms;

@SuppressWarnings("unused")
public abstract class PrefixSum {
    protected final long[] ps;
    public static final long DEFAULT_MOD = 1_000_000_007L;

    private PrefixSum(long[] ps) {
        this.ps = ps;
    }

    public static PrefixSum of(int[] arr) {
        return new PlainPrefixSum(preparePS(arr));
    }

    public static PrefixSum ofMod(int[] arr) {
        return ofMod(arr, DEFAULT_MOD);
    }

    public static PrefixSum ofMod(int[] arr, long mod) {
        return new ModPrefixSum(preparePS(arr), mod);
    }

    public abstract long rangeSumInclusive(int left, int right);

    private static long[] preparePS(int[] arr) {
        int n = arr.length;
        long[] ps = new long[n + 1];
        for (int i = 0; i < n; i++) {
            ps[i + 1] = ps[i] + arr[i];
        }
        return ps;
    }

    private static final class PlainPrefixSum extends PrefixSum {
        private PlainPrefixSum(long[] ps) {
            super(ps);
        }

        @Override
        public long rangeSumInclusive(int left, int right) {
            return ps[right + 1] - ps[left];
        }
    }

    private static final class ModPrefixSum extends PrefixSum {
        private final long mod;

        private ModPrefixSum(long[] ps, long mod) {
            super(ps);
            this.mod = mod;
        }

        @Override
        public long rangeSumInclusive(int left, int right) {
            long res = (ps[right + 1] - ps[left]) % mod;
            if (res < 0) res += mod;
            return res;
        }
    }
}