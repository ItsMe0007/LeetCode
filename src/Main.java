import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        Solution solution = new Solution();
        Method method = solution.getClass().getDeclaredMethods()[0];
        try (BufferedReader br = new BufferedReader(new FileReader(".\\src\\TestCase.txt"))) {
            while (br.ready()) {
                Object[] params = new Object[method.getParameterCount()];
                for (int i = 0; i < params.length; i++) {
                    Class<?> clazz = method.getParameterTypes()[i];
                    params[i] = TypeCaster.castTo(parse(br.readLine(), clazz), clazz);
                }
                System.out.println(method.invoke(solution, params));
            }
        }
    }

    private static Object parse(String s, Class<?> clazz) {
        if (clazz.isArray()) {
            List<Object> list = new ArrayList<>();
            for (String pair : s.substring(1, s.length() - 1).split(",")) {
                list.add(parse(pair, clazz.getComponentType()));
            }
            return list.toArray();
//            return Arrays.stream(s.substring(1, s.length() - 1).split(",")).map(x -> parse(x, clazz.getComponentType())).toArray();
        } else {
            if (Objects.equals(clazz, int.class)) {
                return Integer.parseInt(s);
            } else if (Objects.equals(clazz, long.class)) {
                return Long.parseLong(s);
            } else if (Objects.equals(clazz, char.class)) {
                return s.charAt(0);
            } else if (Objects.equals(clazz, float.class)) {
                return Float.parseFloat(s);
            } else if (Objects.equals(clazz, boolean.class)) {
                return Boolean.parseBoolean(s);
            } else if (Objects.equals(clazz, double.class)) {
                return Double.parseDouble(s);
            }
            return null;
        }
    }


    public static class TypeCaster {

        public static Object castTo(Object o, Class<?> clazz) {
            if (clazz.isArray()) {
                return castArray(o, clazz);
            } else {
                return clazz.cast(o);
            }
        }

        private static Object castArray(Object o, Class<?> clazz) {
            Class<?> componentType = clazz.getComponentType();
            if (componentType.isArray()) {
                // Handle multi-dimensional arrays
                return castMultiDimensionalArray(o, componentType);
            } else {
                // Handle single-dimensional arrays
                Object[] array = (Object[]) o;
                Object castArray = Array.newInstance(componentType, array.length);
                for (int i = 0; i < array.length; i++) {
                    Array.set(castArray, i, componentType.cast(array[i]));
                }
                return castArray;
            }
        }

        private static Object castMultiDimensionalArray(Object o, Class<?> componentType) {
            Class<?> componentComponentType = componentType.getComponentType();
            if (componentComponentType.isArray()) {
                // Recursively handle arrays
                Object[] array = (Object[]) o;
                Object castArray = Array.newInstance(componentType, array.length);
                for (int i = 0; i < array.length; i++) {
                    Array.set(castArray, i, castMultiDimensionalArray(array[i], componentComponentType));
                }
                return castArray;
            } else {
                // Handle last dimension
                return castArray(o, componentType);
            }
        }

    }

}