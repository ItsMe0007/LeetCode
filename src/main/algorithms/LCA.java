package main.algorithms;

import java.util.*;

@SuppressWarnings("unused")
public class LCA {
    private final int n;
    private final int maxLog;
    private final int[][] parent;
    private final int[] depth;
    private final List<List<Integer>> graph;

    public LCA(int n, int[][] edges, boolean directed) {
        this.n = n;
        this.maxLog = 32 - Integer.numberOfLeadingZeros(n);
        this.parent = new int[n + 1][maxLog];
        this.depth = new int[n + 1];
        this.graph = new ArrayList<>();

        for (int i = 0; i <= n; i++)
            graph.add(new ArrayList<>());

        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            if (!directed) graph.get(edge[1]).add(edge[0]);
        }

        iterativeDFS();

        for (int j = 1; j < maxLog; j++) {
            for (int node = 1; node <= n; node++) {
                parent[node][j] = parent[parent[node][j - 1]][j - 1];
            }
        }
    }

    private void iterativeDFS() {
        ArrayDeque<Integer> queue = new ArrayDeque<>(n + 1);
        boolean[] visited = new boolean[n + 1];
        int root = 1;
        queue.addLast(root);
        visited[root] = true;

        while (!queue.isEmpty()) {
            int node = queue.pollFirst();
            for (int child : graph.get(node)) {
                if (visited[child]) continue;
                visited[child] = true;
                depth[child] = depth[node] + 1;
                parent[child][0] = node;
                queue.addLast(child);
            }
        }
    }

    public int getLCA(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }

        u = lift(u, depth[u] - depth[v]);
        if (u == v) return u;

        for (int j = maxLog - 1; j >= 0; j--) {
            if (parent[u][j] != parent[v][j]) {
                u = parent[u][j];
                v = parent[v][j];
            }
        }
        return parent[u][0];
    }

    public int getDistance(int u, int v) {
        return depth[u] + depth[v] - 2 * depth[getLCA(u, v)];
    }

    public int getDepth(int node) {
        return depth[node];
    }

    public int getKthAncestor(int node, int k) {
        if (k > depth[node]) return 0;
        return lift(node, k);
    }

    private int lift(int node, int k) {
        for (int bitPos = 0; bitPos < maxLog; bitPos++) {
            if ((k & (1 << bitPos)) != 0) {
                node = parent[node][bitPos];
            }
        }
        return node;
    }
}