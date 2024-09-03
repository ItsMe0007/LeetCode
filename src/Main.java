import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Solution solution = new Solution();
        Method method = solution.getClass().getDeclaredMethods()[0];
        try (BufferedReader br = new BufferedReader(new FileReader(".\\src\\TestCase.txt"))) {
            while (br.ready()) {
                Object[] params = new Object[method.getParameterCount()];
                for (int i = 0; i < params.length; i++) {
                    Class<?> clazz = method.getParameterTypes()[i];
                    params[i] = parse(br.readLine(), clazz);
                }
                System.out.println(method.invoke(solution, params));
            }
        }
    }

    private static Object parse(String s, Class<?> clazz) {
        if (clazz.isArray()) {
            Class<?> componentClazz = clazz.getComponentType();
            List<String> parts = splitAtComma(s);
            Object array = Array.newInstance(clazz.getComponentType(), parts.size());
            for (int i = 0; i < parts.size(); i++) {
                if (componentClazz.isArray()) {
                    Array.set(array, i, parse(parts.get(i), clazz.getComponentType()));
                } else {
                    String part = parts.get(i);
                    if (Objects.equals(componentClazz, int.class)) {
                        Array.set(array, i, Integer.parseInt(part));
                    } else if (Objects.equals(componentClazz, long.class)) {
                        Array.set(array, i, Long.parseLong(part));
                    } else if (Objects.equals(componentClazz, char.class)) {
                        Array.set(array, i, part.charAt(1));
                    } else if (Objects.equals(componentClazz, float.class)) {
                        Array.set(array, i, Float.parseFloat(part));
                    } else if (Objects.equals(componentClazz, boolean.class)) {
                        Array.set(array, i, Boolean.parseBoolean(part));
                    } else if (Objects.equals(componentClazz, double.class)) {
                        Array.set(array, i, Double.parseDouble(part));
                    } else if (Objects.equals(componentClazz, String.class)) {
                        Array.set(array, i, part.substring(1, part.length() - 1));
                    }
                }
            }
            return array;
        } else {
            if (Objects.equals(clazz, int.class)) {
                return Integer.parseInt(s);
            } else if (Objects.equals(clazz, long.class)) {
                return Long.parseLong(s);
            } else if (Objects.equals(clazz, char.class)) {
                return s.charAt(1);
            } else if (Objects.equals(clazz, float.class)) {
                return Float.parseFloat(s);
            } else if (Objects.equals(clazz, boolean.class)) {
                return Boolean.parseBoolean(s);
            } else if (Objects.equals(clazz, double.class)) {
                return Double.parseDouble(s);
            } else if (Objects.equals(clazz, String.class)) {
                return s.substring(1, s.length() - 1);
            }
        }
        return null;
    }

    private static List<String> splitAtComma(String s) {
        if (s.charAt(1) != '[') return Arrays.asList(s.substring(1, s.length() - 1).split(","));

        List<String> list = new ArrayList<>();
        int start = 1, end = 0, count = 0;
        while (++end < s.length()) {
            if (s.charAt(end) == '[') {
                count++;
            } else if (s.charAt(end) == ']') {
                count--;
            }
            if (count == 0) {
                list.add(s.substring(start, end + 1));
                start = ++end + 1;
            }
        }
        return list;
    }
}
