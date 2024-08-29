import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

@SuppressWarnings("unused")
public class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {
    }

    TreeNode(int val) {
        this.val = val;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    public static TreeNode generate(int maximumCapacity) {
        return GenerateBinaryTree.generate(maximumCapacity);
    }


    private static class GenerateBinaryTree {
        private static final Random random = new Random();
        private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
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