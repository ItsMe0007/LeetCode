package main.java;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.*;

@SuppressWarnings("unused")
@NoArgsConstructor
@AllArgsConstructor
public class TreeNode {
    Integer val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val) {
        this.val = val;
    }

    public TreeNode(String data) {
        data = data.replaceAll("[\\[\\]]", "");
        if (!data.isEmpty()) {
            String[] tokens = data.split(",");
            Queue<TreeNode> queue = new LinkedList<>();
            this.val = Integer.parseInt(tokens[0].trim());
            queue.add(this);
            int itr = 0;
            while (!queue.isEmpty() && itr < tokens.length) {
                TreeNode current = queue.poll();
                if (++itr < tokens.length && !"null".equals(tokens[itr])) {
                    current.left = new TreeNode(Integer.parseInt(tokens[itr].trim()));
                    queue.add(current.left);
                }
                if (++itr < tokens.length && !"null".equals(tokens[itr])) {
                    current.right = new TreeNode(Integer.parseInt(tokens[itr].trim()));
                    queue.add(current.right);
                }
            }
        }
    }

    public static TreeNode generate(int maximumCapacity) {
        return GenerateBinaryTree.generate(maximumCapacity);
    }

    public String toString() {
        List<TreeNode> list = new ArrayList<>();
        list.add(this);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null) {
                list.add(list.get(i).left);
                list.add(list.get(i).right);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (TreeNode node : list) {
            if (node != null) {
                sb.append(node.val).append(",");
            } else {
                sb.append("null,");
            }
        }
        return "[" + sb.substring(0, sb.length() - 1) + "]";
    }


    private static class GenerateBinaryTree {
        private static final Random random = new Random();
        private static final int DEFAULT_INITIAL_CAPACITY = 1 << 3;
        private static int maximumCapacity;
        private static int size;

        private static TreeNode generate(int maximumCapacity) {
            GenerateBinaryTree.maximumCapacity = Math.max(maximumCapacity, DEFAULT_INITIAL_CAPACITY);
            GenerateBinaryTree.size = 0;
            return generate();
        }

        private static TreeNode generate() {
            TreeNode root = new TreeNode(++size);
            Queue<TreeNode> queue = new LinkedList<>();
            queue.add(root);
            while (!queue.isEmpty() && size < maximumCapacity) {
                TreeNode current = queue.poll();
                if (random.nextBoolean()) {
                    current.left = new TreeNode(++size);
                    queue.add(current.left);
                }
                if (random.nextBoolean()) {
                    current.right = new TreeNode(++size);
                    queue.add(current.right);
                }
            }
            return root;
        }
    }
}
