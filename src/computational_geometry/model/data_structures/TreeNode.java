package computational_geometry.model.data_structures;

/**
 * Nodes of a binary tree
 * @author eloi
 *
 * @param <T> : The type of the value inside the node
 */
public class TreeNode<T> {

    private T value;
    private TreeNode<T> parent;
    private TreeNode<T> left;
    private TreeNode<T> right;

    public TreeNode(T value, TreeNode<T> parent, TreeNode<T> left,
            TreeNode<T> right) {
        this.value = value;
        this.parent = parent;
        this.left = left;
        this.right = right;
    }

    public TreeNode(TreeNode<T> node) {
        this(node.getValue(), node.getParent(), node.getLeft(), node.getRight());
    }

    public TreeNode(T obj) {
        this(obj, null, null, null);
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public TreeNode<T> getLeft() {
        return left;
    }

    public void setLeft(TreeNode<T> left) {
        this.left = left;
    }

    public TreeNode<T> getRight() {
        return right;
    }

    public void setRight(TreeNode<T> right) {
        this.right = right;
    }

}
