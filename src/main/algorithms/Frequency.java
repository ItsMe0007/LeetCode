package main.algorithms;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Frequency {
    private static final int ARRAY_BASED_SPAN_LIMIT = 1_000_000;
    private static final int SPAN_PER_ELEM_FACTOR = 16;

    public final int min;
    public final int max;
    private final boolean isArrayBased;
    private int[] freq;
    private Map<Integer, Integer> freqMap;

    public Frequency(int[] arr) {
        if (arr == null || arr.length == 0) {
            min = 0;
            max = -1;
            isArrayBased = false;
            freq = null;
            freqMap = HashMap.newHashMap(0);
            return;
        }

        int tempMin = Integer.MAX_VALUE;
        int tempMax = Integer.MIN_VALUE;

        for (int num : arr) {
            tempMin = Math.min(tempMin, num);
            tempMax = Math.max(tempMax, num);
        }
        this.min = tempMin;
        this.max = tempMax;

        long span = (long) max - (long) min;
        isArrayBased = span >= 0 && span <= ARRAY_BASED_SPAN_LIMIT && span <= (long) SPAN_PER_ELEM_FACTOR * arr.length;

        if (isArrayBased) {
            buildArrayBasedFreq(arr);
        } else {
            buildMapBasedFreq(arr);
        }
    }

    private void buildArrayBasedFreq(int[] arr) {
        freq = new int[max - min + 1];
        for (int num : arr) {
            ++freq[num - min];
        }
    }

    private void buildMapBasedFreq(int[] arr) {
        freqMap = HashMap.newHashMap(arr.length);
        for (int num : arr) {
            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
        }
    }

    public int getFrequency(int num) {
        if (num < min || num > max) return 0;
        if (isArrayBased) {
            return freq[num - min];
        } else {
            return freqMap.getOrDefault(num, 0);
        }
    }
}
