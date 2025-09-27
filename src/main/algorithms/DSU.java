package main.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class DSU {
    private final int[] parent;
    private final int[] rank;
    int size;

    public DSU(int size) {
        this.size = size;
        parent = new int[size];
        rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 1;
        }
    }

    public int findParent(int node) {
        if (node == parent[node]) {
            return node;
        }
        parent[node] = findParent(parent[node]);
        return parent[node];
    }

    public void union(int u, int v) {
        int parentOfU = findParent(u);
        int parentOfV = findParent(v);
        if (parentOfU != parentOfV) {
            if (rank[parentOfU] > rank[parentOfV]) {
                parent[parentOfV] = parentOfU;
            } else if (rank[parentOfU] < rank[parentOfV]) {
                parent[parentOfU] = parentOfV;
            } else {
                parent[parentOfV] = parentOfU;
                rank[parentOfU] += 1;
            }
        }
    }

    public boolean isConnected(int u, int v) {
        return findParent(u) == findParent(v);
    }

    public Map<Integer, List<Integer>> groups() {
        Map<Integer, List<Integer>> groupMap = new HashMap<>();
        for (int u = 0; u < size; u++) findParent(u);

        for (int u = 0; u < size; u++) {
            groupMap.computeIfAbsent(findParent(u), _ -> new ArrayList<>()).add(u);
        }
        return groupMap;
    }
}