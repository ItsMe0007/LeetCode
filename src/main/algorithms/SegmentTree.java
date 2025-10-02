package main.algorithms;

@SuppressWarnings("unused")
public class SegmentTree {

    public static class SegmentNode {
        int start;
        int end;
        int minIdx;
        int maxIdx;
        long sum;

        public SegmentNode(int start, int end, int minIdx, int maxIdx, long sum) {
            this.start = start;
            this.end = end;
            this.minIdx = minIdx;
            this.maxIdx = maxIdx;
            this.sum = sum;
        }

        public SegmentNode(int idx, long sum) {
            this.start = idx;
            this.end = idx;
            this.minIdx = idx;
            this.maxIdx = idx;
            this.sum = sum;
        }

        public void update(int idx, int val) {
            this.start = idx;
            this.end = idx;
            this.minIdx = idx;
            this.maxIdx = idx;
            this.sum = val;
        }

        @Override
        public String toString() {
            return String.format("range=[%d,%d], minIdx=%d, maxIdx=%d, sum=%d",
                    start, end, minIdx, maxIdx, sum);
        }
    }

    final int[] arr;
    final int arrSize;
    final int treeSize;
    final int leafSize;
    final int nonLeafSize;
    final SegmentNode[] tree;
    final SegmentNode emptyNode = new SegmentNode(-1, -1, -1, -1, 0);

    public SegmentTree(int[] arr) {
        this.arr = arr;
        arrSize = arr.length;
        leafSize = (arrSize == 1) ? 1 : Integer.highestOneBit(arrSize - 1) << 1;
        nonLeafSize = leafSize - 1;
        treeSize = leafSize + nonLeafSize;
        tree = new SegmentNode[treeSize];

        for (int leafIdx = nonLeafSize, arrIdx = 0; leafIdx < treeSize; ++leafIdx, ++arrIdx) {
            if (arrIdx < arrSize) tree[leafIdx] = new SegmentNode(arrIdx, arr[arrIdx]);
            else tree[leafIdx] = emptyNode;
        }

        for (int nonLeafIdx = nonLeafSize - 1; nonLeafIdx >= 0; --nonLeafIdx) {
            computeAndUpdateParent(nonLeafIdx);
        }
    }

    private void computeAndUpdateParent(int parent) {
        SegmentNode left = tree[leftChildIndex(parent)];
        SegmentNode right = tree[rightChildIndex(parent)];
        tree[parent] = merge(left, right);
    }

    private SegmentNode merge(SegmentNode left, SegmentNode right) {
        if (left == emptyNode) return right;
        if (right == emptyNode) return left;
        return new SegmentNode(
                left.start,
                right.end,
                arr[left.minIdx] <= arr[right.minIdx] ? left.minIdx : right.minIdx,
                arr[left.maxIdx] >= arr[right.maxIdx] ? left.maxIdx : right.maxIdx,
                left.sum + right.sum
        );
    }

    private int leftChildIndex(int parent) {
        return (parent << 1) + 1;
    }

    private int rightChildIndex(int parent) {
        return (parent << 1) + 2;
    }

    private int parentIndex(int leaf) {
        return (leaf - 1) >> 1;
    }

    private boolean isLeftChild(int node) {
        return (node & 1) == 1;
    }

    private boolean isRightChild(int node) {
        return (node & 1) == 0;
    }

    public SegmentNode query(int startIdx, int endIdx) {
        return query(0, startIdx, endIdx);
    }

    private SegmentNode query(int nodeIdx, int startIdx, int endIdx) {
        SegmentNode node = tree[nodeIdx];
        if (node == emptyNode || startIdx > node.end || endIdx < node.start) return emptyNode;
        if (startIdx <= node.start && node.end <= endIdx) return node;

        return merge(
                query(leftChildIndex(nodeIdx), startIdx, endIdx),
                query(rightChildIndex(nodeIdx), startIdx, endIdx)
        );
    }

    public void update(int idx, int val) {
        int parent = idx + nonLeafSize;
        arr[idx] = val;
        tree[parent].update(idx, val);
        while (parent != 0) {
            parent = parentIndex(parent);
            computeAndUpdateParent(parent);
        }
    }
}
