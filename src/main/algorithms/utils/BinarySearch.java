package main.algorithms.utils;

import java.util.Comparator;
import java.util.List;

@SuppressWarnings("unused")
public final class BinarySearch {

    private BinarySearch() {
    }

    public static int lowerBound(int[] nums, int target) {
        return lowerBound(nums, 0, nums.length - 1, target);
    }

    public static int upperBound(int[] nums, int target) {
        return upperBound(nums, 0, nums.length - 1, target);
    }

    public static int lowerBound(int[] nums, int left, int right, int target) {
        if (nums[left] >= target) return left;
        if (nums[right] < target) return right + 1;
        while (left + 1 < right) {
            int mid = (left + right) >> 1;
            if (nums[mid] >= target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return (nums[left] >= target) ? left : right;
    }

    public static int upperBound(int[] nums, int left, int right, int target) {
        if (nums[left] > target) return left;
        if (nums[right] <= target) return right + 1;
        while (left + 1 < right) {
            int mid = (left + right) >> 1;
            if (nums[mid] > target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return (nums[left] > target) ? left : right;
    }

    public static <T> int lowerBound(List<T> list, T target, Comparator<? super T> comparator) {
        return lowerBound(list, 0, list.size() - 1, target, comparator);
    }

    public static <T> int upperBound(List<T> list, T target, Comparator<? super T> comparator) {
        return upperBound(list, 0, list.size() - 1, target, comparator);
    }

    public static <T> int lowerBound(List<T> list, int left, int right, T target, Comparator<? super T> comparator) {
        if (comparator.compare(list.get(left), target) >= 0) return left;
        if (comparator.compare(list.get(right), target) < 0) return right + 1;
        while (left + 1 < right) {
            int mid = (left + right) >> 1;
            if (comparator.compare(list.get(mid), target) >= 0) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return (comparator.compare(list.get(left), target) >= 0) ? left : right;
    }

    public static <T> int upperBound(List<T> list, int left, int right, T target, Comparator<? super T> comparator) {
        if (comparator.compare(list.get(left), target) > 0) return left;
        if (comparator.compare(list.get(right), target) <= 0) return right + 1;
        while (left + 1 < right) {
            int mid = (left + right) >> 1;
            if (comparator.compare(list.get(mid), target) > 0) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return (comparator.compare(list.get(left), target) > 0) ? left : right;
    }
}
