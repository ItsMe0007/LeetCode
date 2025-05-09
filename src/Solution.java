class Solution {

    private int n;
    private int[] origFreq = new int[10];
    private int[] currFreq = new int[10];
    private int halfSum;
    private int halfN;
    private final long MOD = 1000000007;

    private long[] factorials = new long[41];
    private long[] invFactorials = new long[41];
    private long[][][] memo = new long[81][41][361];

    /*
     * if totalSum is odd, then not possible, return 0;
     *
     * let halfSum = totalSum / 2;
     * halfN = floor(n/2)
     *
     * now, find a subsequence of halfN digits that sums to halfSum.
     * also maintain freq of each used digit.
     * count += (Number of permutations of these halfN digits) * (Number of permutations of remaining n-halfN digits)
     *
     * instead of blindly choosing a number, use a frequency array and backtrack from 0 to each character's frequency.
     *
     * */
    public int countBalancedPermutations(String num) {
        this.n = num.length();
        this.halfN = n / 2;
        halfSum = 0;

        for (char c : num.toCharArray()) {
            origFreq[c - '0']++;
            halfSum += c - '0';
        }

        if ((halfSum & 1) == 1) return 0;
        else halfSum >>= 1;

        computeFactorialsAndInvFacts();

        for (int i = 0; i < memo.length; i++) {
            for (int j = 0; j < memo[0].length; j++) {
                for (int k = 0; k < memo[0][0].length; k++) {
                    memo[i][j][k] = -1;
                }
            }
        }

        return (int) backTrack(0, 0, halfN, halfSum);
    }

    private long backTrack(int start, int memoIndex, int remainingK, int remainingSum) {
        if (remainingK == 0 && remainingSum == 0) {
            return calculatePossible();
        } else if (remainingK <= 0 || remainingSum < 0 || start > 9) {
            return 0L;
        } else if (memo[memoIndex][remainingK][remainingSum] != -1) {
            return memo[memoIndex][remainingK][remainingSum];
        }
        long res = 0L;

        for (int howManyStartPicked = 0; howManyStartPicked <= origFreq[start]; howManyStartPicked++) {
            currFreq[start] += howManyStartPicked;
            res = (res + backTrack(start + 1, memoIndex + origFreq[start], remainingK - howManyStartPicked, remainingSum - (howManyStartPicked * start))) % MOD;
            currFreq[start] -= howManyStartPicked;
        }
        return memo[memoIndex][remainingK][remainingSum] = res;
    }

    private long calculatePossible() {
        long res = factorials[halfN] * factorials[n - halfSum];
        for (int i = 0; i < 10; i++) {
            res = (res * invFactorials[currFreq[i]] * invFactorials[origFreq[i] - currFreq[i]]) % MOD;
        }
        return res;
    }

    private long power(long base, long exponent) {
        long res = 1;
        while (exponent > 0) {
            if ((exponent & 1) == 1) {
                res = (res * base) % MOD;
            }
            base = (base * base) % MOD;
            exponent >>= 1;
        }
        return res;
    }

    private void computeFactorialsAndInvFacts() {
        /*
         * using Fermet's little theorem, inv(a) = a^(MOD-2)
         * */
        factorials[0] = 1;
        invFactorials[0] = 1;
        for (int i = 1; i < factorials.length; i++) {
            factorials[i] = (factorials[i - 1] * i) % MOD;
            invFactorials[i] = power(factorials[i], MOD - 2);
        }
    }

}
