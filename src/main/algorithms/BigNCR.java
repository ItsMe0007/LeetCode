package main.algorithms;

@SuppressWarnings("unused")
public class BigNCR {
    private static final long DEFAULT_MOD = 1_000_000_007L;
    private static final int DEFAULT_MAX_N = 1_000_000;

    public final long[] fact;
    public final long[] invFact;
    private final long mod;

    public BigNCR() {
        this(DEFAULT_MAX_N, DEFAULT_MOD);
    }

    public BigNCR(int maxN) {
        this(maxN, DEFAULT_MOD);
    }

    public BigNCR(int maxN, long mod) {
        this.mod = mod;
        this.fact = new long[maxN + 1];
        this.invFact = new long[maxN + 1];

        fact[0] = invFact[0] = 1;
        for (int i = 1; i <= maxN; i++) {
            fact[i] = (fact[i - 1] * i) % mod;
        }

        // Fermat: Inv(a) = a^(p-2) mod p
        invFact[maxN] = power(fact[maxN], mod - 2, mod);
        for (int i = maxN; i >= 1; i--) {
            invFact[i - 1] = (invFact[i] * i) % mod;
        }
    }

    public static long power(long base, long exp, long mod) {
        long result = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) result = (result * base) % mod;
            base = (base * base) % mod;
            exp >>= 1;
        }
        return result;
    }

    public long nCr(int n, int r) {
        if (r < 0 || r > n) return 0;
        return (((fact[n] * invFact[r]) % mod) * invFact[n - r]) % mod;
    }

    public long nPr(int n, int r) {
        if (r < 0 || r > n) return 0;
        return (fact[n] * invFact[n - r]) % mod;
    }
}