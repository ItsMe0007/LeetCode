package main.java;

@SuppressWarnings("unused")
public record Pair<K, V>(K key, V value) {
    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
