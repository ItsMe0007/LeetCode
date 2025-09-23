package main.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Prime {
    private static final int DEFAULT_MAX_N = 1_000_000;

    public final boolean[] isPrime;
    public final List<Integer> primes;

    public Prime() {
        this(DEFAULT_MAX_N);
    }

    public Prime(int inclusiveLimit) {
        isPrime = new boolean[inclusiveLimit + 1];
        primes = new ArrayList<>();
        Arrays.fill(isPrime, true);
        isPrime[0] = isPrime[1] = false;

        for (int m = 4; m <= inclusiveLimit; m += 2) isPrime[m] = false;

        for (int p = 3; p * p <= inclusiveLimit; p += 2) {
            if (isPrime[p]) {
                for (int m = p * p; m <= inclusiveLimit; m += (p << 1)) {
                    isPrime[m] = false;
                }
            }
        }
        if (inclusiveLimit >= 2) primes.add(2);
        for (int p = 3; p <= inclusiveLimit; p += 2) {
            if (isPrime[p]) primes.add(p);
        }
    }

    private static final int[] SMALL = {3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37};

    public static boolean checkIfPrime(int num) {
        if (num < 2) return false;
        if ((num & 1L) == 0) return num == 2;

        for (int p : SMALL) {
            if (num == p) return true;
            if (num % p == 0) return false;
        }

        int factor = 11;
        while (factor <= num / factor) {
            if (num % factor == 0 || num % (factor + 2) == 0) return false;
            factor += 6;
        }
        return true;
    }

    public List<Long> getPrimeFactors(long n) {
        List<Long> factors = new ArrayList<>();
        if (n <= 1) return factors;

        for (Integer p : primes) {
            if ((long) p * p > n) break;
            while (n % p == 0) {
                factors.add((long) p);
                n /= p;
            }
        }
        if (n > 1) factors.add(n);
        return factors;
    }

}
