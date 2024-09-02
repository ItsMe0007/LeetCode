import java.util.*;

class Solution {

    public int chalkReplacer(int[] chalk, int k) {
        long[] ps = new long[chalk.length];
        ps[0] = chalk[0];
        for (int i = 1; i < ps.length; i++) {
            ps[i] = ps[i - 1] + chalk[i];
        }

        k = (int) (k % ps[ps.length - 1]);

        int left = 0, right = ps.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (ps[mid] == k) {
                return mid + 1;
            }
            if (ps[mid] < k) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return right;
    }
}