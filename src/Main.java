import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        Solution solution = new Solution();
        Method method = solution.getClass().getDeclaredMethods()[0];
        System.out.println(method.invoke(solution,
                5, parse2D("[[4,1,-1],[2,0,-1],[0,3,-1],[4,3,-1]]"), 0, 1, 5
        ));
    }

    public static int[][] parse2D(String s) {
        List<int[]> list = new ArrayList<>();
        for (String pair : s.substring(2, s.length() - 2).split("],\\[")) {
            String[] nums = pair.split(",");
            int[] arr = new int[nums.length];
            for (int i = 0; i < nums.length; i++) {
                arr[i] = Integer.parseInt(nums[i].trim());
            }
            list.add(arr);
        }
        return list.toArray(new int[list.size()][]);
    }

    public static int[] parse1D(String s) {
        List<Integer> list = new ArrayList<>();
        for (String num : s.substring(1, s.length() - 1).split(",")) {
            list.add(Integer.parseInt(num.trim()));
        }
        return list.stream().mapToInt(i -> i).toArray();
    }


}