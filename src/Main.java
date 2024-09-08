import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Solution solution = new Solution();
        Optional<Method> method = Arrays.stream(solution.getClass().getDeclaredMethods()).filter(x -> Modifier.isPublic(x.getModifiers())).findFirst();
        try (BufferedReader br = new BufferedReader(new FileReader(".\\src\\TestCase.txt"))) {
            while (br.ready() && method.isPresent()) {
                Object[] params = new Object[method.get().getParameterCount()];
                for (int i = 0; i < params.length; i++) {
                    Class<?> clazz = method.get().getParameterTypes()[i];
                    params[i] = parse(br.readLine(), clazz);
                }
                System.out.println(toString(method.get().invoke(solution, params), method.get().getReturnType()));
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
                    } else if (Objects.equals(clazz, ListNode.class)) {
                        return new ListNode(s);
                    } else if (Objects.equals(clazz, TreeNode.class)) {
                        return new TreeNode(s);
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
            } else if (Objects.equals(clazz, ListNode.class)) {
                return new ListNode(s);
            } else if (Objects.equals(clazz, TreeNode.class)) {
                return new TreeNode(s);
            }
        }
        return null;
    }

    private static List<String> splitAtComma(String s) {
        if (s.length() == 2) return Collections.emptyList();
        else if (s.length() > 2 && s.charAt(1) != '[') {
            return Arrays.asList(s.substring(1, s.length() - 1).split(","));
        }
        List<String> list = new ArrayList<>();
        int start = 1;
        int end = 0;
        int count = 0;
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

    private static String toString(Object object, Class<?> clazz) {
        if (clazz.isArray()) {
            StringBuilder sb = new StringBuilder("[");
            int length = Array.getLength(object);
            for (int i = 0; i < length; i++) {
                Object element = Array.get(object, i);
                sb.append(toString(element, clazz.getComponentType())).append(",");
            }
            sb.setCharAt(sb.length() - 1, ']');
            return sb.toString();
        }
        return Optional.ofNullable(object).orElse("null").toString();
    }


}
