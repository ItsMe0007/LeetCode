package main.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Prime {
    private static final int DEFAULT_MAX_N = 1_000_000;

    public final int[] spf;
    public final int[] primes;

    public Prime() {
        this(DEFAULT_MAX_N);
    }

    public Prime(int inclusiveLimit) {
        spf = new int[inclusiveLimit + 1];
        int[] tempPrimes = new int[Math.min((inclusiveLimit + 1) >> 1, 78_500)];
        int size = 0;
        spf[0] = spf[1] = -1;

        for (int num = 2; num <= inclusiveLimit; num++) {
            if (spf[num] == 0) {
                spf[num] = num;
                tempPrimes[size++] = num;
            }

            for (int i = 0; i < size; i++) {
                int prime = tempPrimes[i];
                if (prime > spf[num] || (long) prime * num > inclusiveLimit) break;
                spf[prime * num] = prime;
            }
        }
        primes = Arrays.copyOf(tempPrimes, size);
    }

    public boolean isPrime(int num) {
        return spf[num] == num;
    }

    private static final int[] SMALL = {3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37};

    public static boolean checkIfPrime(int num) {
        if (num < 2) return false;
        if ((num & 1) == 0) return num == 2;

        for (int prime : SMALL) {
            if (num == prime) return true;
            if (num % prime == 0) return false;
        }

        int factor = 41;

        while (factor <= num / factor) {
            if (num % factor == 0 || num % (factor + 2) == 0) return false;
            factor += 6;
        }
        return true;
    }

    public List<Integer> getPrimeFactors(int num) {
        List<Integer> factors = new ArrayList<>();
        while (num > 1) {
            int prime = spf[num];
            factors.add(prime);
            num /= prime;
        }
        return factors;
    }

    public List<Integer> getDistinctPrimeFactors(int num) {
        List<Integer> factors = new ArrayList<>();
        while (num > 1) {
            int prime = spf[num];
            factors.add(prime);
            do {
                num /= prime;
            } while (num > 1 && spf[num] == prime);
        }
        return factors;
    }
}
