package main.java;

import java.io.BufferedReader;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    private static final Path TEST_CASE_FILE_PATH = Paths.get("src", "main", "test", "TestCase.txt");

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        Solution solution = new Solution();
        Optional<Method> methodOpt = Arrays.stream(solution.getClass().getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .findFirst();

        if (methodOpt.isEmpty()) return;
        Method method = methodOpt.get();

        Type[] genericParams = method.getGenericParameterTypes();
        Type genericReturn = method.getGenericReturnType();

        try (BufferedReader br = Files.newBufferedReader(TEST_CASE_FILE_PATH)) {
            while (br.ready()) {
                Object[] params = new Object[genericParams.length];
                for (int i = 0; i < params.length; i++) {
                    String line = br.readLine();
                    params[i] = parse(line.trim().replace("©leetcode", ""), genericParams[i]);
                }
                Object response = method.invoke(solution, params);
                System.out.println(toString(response, genericReturn));
            }
        }
        System.out.println("Time elapsed: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private static Object parse(String s, Type type) {
        if (type instanceof Class<?> clazz) {
            if (clazz.isArray()) return parseArray(s, clazz);
            return directParse(s, clazz);
        }
        return parseGenericType(s, type);
    }

    private static Object parseArray(String s, Class<?> clazz) {
        Class<?> component = clazz.getComponentType();
        List<String> parts = splitAtComma(s);
        Object array = Array.newInstance(component, parts.size());
        for (int i = 0; i < parts.size(); i++) {
            Object elem = parse(parts.get(i).trim(), component);
            Array.set(array, i, elem);
        }
        return array;
    }

    private static Object directParse(String s, Class<?> clazz) {
        if (clazz == int.class || clazz == Integer.class) return Integer.parseInt(s);
        if (clazz == long.class || clazz == Long.class) return Long.parseLong(s);
        if (clazz == char.class || clazz == Character.class) return s.charAt(1);
        if (clazz == float.class || clazz == Float.class) return Float.parseFloat(s);
        if (clazz == double.class || clazz == Double.class) return Double.parseDouble(s);
        if (clazz == boolean.class || clazz == Boolean.class) return Boolean.parseBoolean(s);
        if (clazz == String.class) return s.substring(1, s.length() - 1);
        if (clazz == ListNode.class) return new ListNode(s);
        if (clazz == TreeNode.class) return new TreeNode(s);
        return null;
    }

    private static List<Object> parseGenericType(String s, Type type) {
        if (type instanceof ParameterizedType pType) {
            Type raw = pType.getRawType();
            if (raw instanceof Class && List.class.isAssignableFrom((Class<?>) raw)) {
                Type elemType = pType.getActualTypeArguments()[0];
                List<String> parts = splitAtComma(s);
                List<Object> list = new ArrayList<>(parts.size());
                for (String part : parts) {
                    list.add(parse(part.trim(), elemType));
                }
                return list;
            }
        }
        return Collections.emptyList();
    }

    private static String toString(Object object, Type type) {
        if (object == null) return "null";
        if (type instanceof Class<?> clazz) {
            if (clazz.isArray()) return toStringArray(object, clazz);
            return object.toString();
        }
        return toStringGenericType(object, type);
    }

    private static String toStringGenericType(Object object, Type type) {
        if (type instanceof ParameterizedType pType) {
            Type raw = pType.getRawType();
            if (raw instanceof Class && List.class.isAssignableFrom((Class<?>) raw)) {
                List<?> list = (List<?>) object;
                if (list.isEmpty()) return "[]";
                Type elemType = pType.getActualTypeArguments()[0];
                StringBuilder sb = new StringBuilder("[");
                for (Object elem : list) {
                    sb.append(toString(elem, elemType)).append(",");
                }
                sb.setCharAt(sb.length() - 1, ']');
                return sb.toString();
            }
        }
        return object.toString();
    }

    private static String toStringArray(Object object, Class<?> clazz) {
        int len = Array.getLength(object);
        if (len == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < len; i++) {
            Object elem = Array.get(object, i);
            sb.append(toString(elem, clazz.getComponentType())).append(",");
        }
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }

    private static List<String> splitAtComma(String s) {
        if (s == null) return Collections.emptyList();
        s = s.trim();
        if (s.length() < 2 || s.charAt(0) != '[' || s.charAt(s.length() - 1) != ']') {
            return Collections.singletonList(s);
        }
        if (s.length() == 2) return new ArrayList<>();
        if (s.charAt(1) != '[') {
            String inside = s.substring(1, s.length() - 1);
            String[] raw = inside.split(",");
            List<String> out = new ArrayList<>(raw.length);
            for (String part : raw) out.add(part.trim());
            return out;
        }

        List<String> list = new ArrayList<>();
        int start = 1;
        int end = 0;
        int count = 0;
        while (++end < s.length() - 1) {
            char c = s.charAt(end);
            if (c == '[') count++;
            else if (c == ']') count--;
            else if (c == ',' && count == 0) {
                list.add(s.substring(start, end));
                start = end + 1;
            }
        }
        list.add(s.substring(start, s.length() - 1));
        list.replaceAll(String::trim);
        return list;
    }
}
