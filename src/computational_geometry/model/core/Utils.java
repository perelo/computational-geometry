package computational_geometry.model.core;

import java.util.Random;
import java.util.Vector;

import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Segment;
import computational_geometry.model.data_structures.CircularList;

/**
 * Usefull methods which I couldn't categorize...
 * @author eloi
 *
 */
public class Utils {

    /**
     * Compute the direction of a list of points
     * @param points
     * @return < 0 if counter-clockwise, > 0 otherwise
     */
    public static int getDirection(CircularList<Point> points) {
        if (points.size() < 3)
            return 0;

        Point p1, p2, p3;
        Point top1 = null, top2 = null, top3 = null;
        for (int i = 0; i < points.size(); ++i) {
            p1 = points.elementAt(i == 0 ? points.size() - 1 : i - 1);
            p2 = points.elementAt(i);
            p3 = points.elementAt((i + 1) % points.size());

            if (p1.y > p2.y && p3.y >= p2.y) {
                if (top2 == null || top2.y > p2.y) {
                    top1 = p1;
                    top2 = p2;
                    top3 = p3;
                }
            }
        }
        if (top1 == null || top2 == null || top3 == null) {
            System.err
                    .println("error while getting direction (maby cuz horizontal segments)");
            return 0;
        }

        return (int) Math.signum(crossProduct(top1, top2, top3));
    }

    /**
     * Merge two list of points using the natural ordering of Points
     * @param left
     * @param right
     * @return
     */
    public static Vector<Point> merge(Vector<Point> left, Vector<Point> right) {

        Vector<Point> points = new Vector<Point>(left.size() + right.size());
        int pos = 0;
        while (left.size() > 0 || right.size() > 0) {
            if (left.size() > 0 && right.size() > 0) {
                if (left.elementAt(0).y <= right.elementAt(0).y) {
                    left.elementAt(0).pos = pos++;
                    points.addElement(left.elementAt(0));
                    left.remove(0);
                } else {
                    right.elementAt(0).pos = pos++;
                    points.addElement(right.elementAt(0));
                    right.remove(0);
                }
            } else if (left.size() > 0) {
                left.elementAt(0).pos = pos++;
                points.addElement(left.elementAt(0));
                left.remove(0);
            } else if (right.size() > 0) {
                right.elementAt(0).pos = pos++;
                points.addElement(right.elementAt(0));
                right.remove(0);
            }
        }
        return points;
    }

    /**
     * Great random function
     * @param n
     * @return a random number between 0 and n-1
     */
    public static int rand(int n) {
        int r = new Random().nextInt();
        if (r < 0)
            r = -r;
        return r % n;
    }

    /**
     * Compute the cross product (vector product) of three points
     * @param p1
     * @param p2
     * @param p3
     * @return The cross product p2p1 ^ p2p3
     */
    public static double crossProduct(Point p1, Point p2, Point p3) {
        // p2p1 vector (p1.x - p2.x, p1.y - p2.y)
        // p2p3 vector (p3.x - p2.x, p3.y - p3.y)
        // cross product = p2p1.x*p2p3.y - p2p1.y*p2p3.x
        return (p1.x - p2.x) * (p3.y - p2.y) - (p3.x - p2.x) * (p1.y - p2.y);
    }

    public static double crossProduct(Segment a, Segment b) {
        return crossProduct(a.u, a.v, new Point(b.v.x - b.u.x + a.u.x, b.v.y - b.u.y + a.u.y));
    }

    /**
     * Compute the orientation of three given points
     * @param p
     * @param q
     * @param r
     * @return < 0 if it's a left turn,
     *         > 0 if it's a right turn,
     *         = 0 if points are aligned
     */
    public static int orientation(Point p, Point q, Point r) {
        return (int) Math.signum((p.x - r.x) * (q.y - r.y) - (p.y - r.y) * (q.x - r.x));
    }

}
