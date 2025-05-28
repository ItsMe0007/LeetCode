package main.java;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("unused")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(includeFieldNames = false)
public class Pair<K, V> {
    K key;
    V value;
}
