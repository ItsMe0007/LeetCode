package main.algorithms.utils;

@SuppressWarnings("unused")
public final class ModularArithmetic {

    private ModularArithmetic() {
    }

    public static final long DEFAULT_MOD = 1_000_000_007L;

    public static long multiply(long x, long y) {
        return multiply(x, y, DEFAULT_MOD);
    }

    public static long multiply(long x, long y, long mod) {
        long result = 0;
        x %= mod;
        y %= mod;
        while (y > 0) {
            if ((y & 1) == 1) result = (result + x) % mod;
            x = (x << 1) % mod;
            y >>= 1;
        }
        return result;
    }

    public static long power(long base, long exp) {
        return power(base, exp, DEFAULT_MOD);
    }

    public static long power(long base, long exp, long mod) {
        long result = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) result = multiply(result, base, mod);
            base = multiply(base, base, mod);
            exp >>= 1;
        }
        return result;
    }

    public static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static long lcm(long x, long y) {
        return (x / gcd(x, y)) * y;
    }
}
