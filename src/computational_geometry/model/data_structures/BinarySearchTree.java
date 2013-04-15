package computational_geometry.model.data_structures;

/**
 * Elementary implementation of a binary search tree
 * @author eloi
 *
 * @param <T> : The type of the nodes
 */
public class BinarySearchTree<T extends Comparable<T>> {

    protected TreeNode<T> root;

    public BinarySearchTree() {
        super();
        root = null;
    }

    protected TreeNode<T> insertImpl(TreeNode<T> root, TreeNode<T> newNode) {
        if (root == null) {
            root = newNode;
        } else if (newNode.getValue().compareTo(root.getValue()) < 0) {
            newNode.setParent(root);
            root.setLeft(insertImpl(root.getLeft(), newNode));
        } else {
            newNode.setParent(root);
            root.setRight(insertImpl(root.getRight(), newNode));
        }
        return root;
    }

    public void insert(T obj) {
        if (obj == null) return;
        root = insertImpl(root, new TreeNode<T>(obj));
    }

    protected void deleteImpl(TreeNode<T> root, T obj) {
        TreeNode<T> node = searchImpl(root, obj);
        if (node == null) return;

        if (node.isLeaf()) {

            TreeNode<T> parent = node.getParent();
            if (parent == null) {       // node is the root
                this.root = null;
            } else {
                if (parent.getLeft() != null &&
                    parent.getLeft().getValue().equals(node.getValue())) {
                    parent.setLeft(null);
                } else {
                    parent.setRight(null);
                }
            }
            return; //  del

        } else if (node.getLeft() != null) {
            TreeNode<T> higherLow = node.getLeft();

            while (higherLow.getRight() != null) {
                higherLow = higherLow.getRight();
            }
            T tmp = node.getValue();
            node.setValue(higherLow.getValue());
            higherLow.setValue(tmp);

            deleteImpl(higherLow, tmp);
        } else {
            TreeNode<T> lowerHigh = node.getRight();

            while (lowerHigh.getLeft() != null) {
                lowerHigh = lowerHigh.getLeft();
            }
            T tmp = node.getValue();
            node.setValue(lowerHigh.getValue());
            lowerHigh.setValue(tmp);

            deleteImpl(lowerHigh, tmp);
        }
    }

    public void delete(T obj) {
        deleteImpl(root, obj);
    }

    protected TreeNode<T> searchImpl(TreeNode<T> root, T obj) {
        if (root == null)
            return null;
        int comp = obj.compareTo(root.getValue());
        if (comp == 0) {
            return root;
        }
        else if (comp < 0) {
            return searchImpl(root.getLeft(), obj);
        }
        else {
            return searchImpl(root.getRight(), obj);

        }
    }

    public T search(T obj) {
        TreeNode<T> node = searchImpl(root, obj);
        return (node == null) ? null : node.getValue();
    }

    protected void displayImpl(TreeNode<T> root) {
        if (root == null) return;

        displayImpl(root.getLeft());
        System.out.println(root.getValue().toString());
        displayImpl(root.getRight());
    }

    public void display() {
        displayImpl(root);
    }

    public TreeNode<T> getRoot() {
        return root;
    }
}
