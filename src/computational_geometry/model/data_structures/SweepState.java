package computational_geometry.model.data_structures;

import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Segment;

/**
 * The sweep state used for monotonization of an arbitrary polygon
 * @author eloi
 *
 */
public class SweepState extends BinarySearchTree<Segment> {

    private Point pEvent;

    public SweepState() {
        super();
    }

    /**
     * Find the first event found when looking left of the current event pEvent
     * @param p
     * @param root : (use getRoot() for first call)
     * @return The first segment at left of pEvent
     */
    public Segment findFirstLeftOfEvent(TreeNode<Segment> root) {
        if (root == null || pEvent == null)
            return null;

        if (root.getValue().isAtLeftOf(pEvent)) {
            if (root.getRight() == null)
                return root.getValue();
            else {
                Segment firstLeftOfRightChild = findFirstLeftOfEvent(root.getRight());
                if (firstLeftOfRightChild != null && firstLeftOfRightChild.isAtLeftOf(pEvent))
                    return firstLeftOfRightChild;
                else return root.getValue();//root.getParent() != null ? root.getParent().getValue()
                                              //       : null;
            }
        } else {
            if (root.getLeft() == null)
                return root.getParent() != null ? root.getParent().getValue()
                                                : null;
            else
                return findFirstLeftOfEvent(root.getLeft());
        }
    }

    public void setEvent(Point p) {
        this.pEvent = p;
        Segment.yComp = pEvent.y;
    }

}
