package computational_geometry.model.core;

import java.util.Vector;

import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Polygon;
import computational_geometry.model.beans.Point.Side;
import computational_geometry.model.beans.Segment;
import computational_geometry.model.data_structures.CircularList;
import computational_geometry.model.data_structures.LinkNode;

/**
 * Methods to manipulate polygons and part of them
 * Used is most of polygon's algorithms
 * @author eloi
 *
 */
public class Polygons {

    /**
     * add to the polygon segments the ones that are connecting the polygon points
     * @param polygon
     */
    public static void addSegmentsFromPoints(Polygon polygon) {
        CircularList<Point> points = polygon.getPoints();

        // Ajout d'un segment entre chaque paire de points consecutifs
        for (int n = 0; n < points.size() - 1; n++) {
            Point p1 = points.elementAt(n);
            Point p2 = points.elementAt(n + 1);
            polygon.addSegment(new Segment(p1, p2));
        }
        // Ajout du segment qui "ferme" le polygone
        if (points.size() > 2)
            polygon.addSegment(new Segment(points.lastElement(), points
                    .firstElement()));
    }

    /**
     * Compute the convexness of the polygon,
     * returns true if all angles have the same orientation.
     * The polygon points must ordered in the list
     * @param polygon
     * @return
     */
    public static boolean isConvex(Polygon polygon) {
        if (polygon.getNbPoints() < 3) {
            return true;
        }
        Point p1, p2, p3;
        CircularList<Point> points = polygon.getPoints();
        int firstOrientation = Utils.orientation(points.get(0), points.get(1), points.get(2));
        for (int i = 1; i < points.size(); ++i) {
            p1 = points.elementAt(i - 1);
            p2 = points.elementAt(i);
            p3 = points.elementAt((i + 1) % points.size());
            if (Utils.orientation(p1, p2, p3) != firstOrientation)
                return false;
        }
        return true;
    }

    public static boolean isMonotonous(Polygon polygon) {
        CircularList<Point> points = polygon.getPoints();

        Point p1, p2, p3;
        boolean bottomFound = false, topFound = false;
        for (int i = 0; i < points.size(); ++i) {
            p1 = points.elementAt(i == 0 ? points.size() - 1 : i - 1);
            p2 = points.elementAt(i);
            p3 = points.elementAt((i + 1) % points.size());

            if (p2.y < p1.y && p2.y < p3.y) { // p2 is the top
                if (topFound)
                    return false;
                topFound = true;
            } else if (p2.y > p1.y && p2.y > p3.y) { // p2 is the bottom
                if (bottomFound)
                    return false;
                bottomFound = true;
            }
        }
        return true;
    }

    public static void markPointsWithType(CircularList<Point> points, int dir) {
        Point p1, p2, p3;
        Point.Type type = null;
        for (int i = 0; i < points.size(); ++i) {
            p1 = points.elementAt(i == 0 ? points.size() - 1 : i - 1);
            p2 = points.elementAt(i);
            p3 = points.elementAt((i + 1) % points.size());
            double prod = Utils.crossProduct(p1, p2, p3);

            if (p1.y > p2.y && p3.y > p2.y) { // start or split
                // now check if the polygon is above or below
                if ((prod < 0 && dir < 0) || (prod > 0 && dir > 0)) { // below
                    type = Point.Type.START;
                } else { // above
                    type = Point.Type.SPLIT;
                }
            } else if (p1.y > p2.y && p3.y == p2.y) {
                if (prod > 0 && dir < 0) {
                    type = Point.Type.REGULARR;
                } else if (prod < 0 && dir < 0) {
                    type = Point.Type.START;
                } else if (prod > 0 && dir > 0) {
                    type = Point.Type.REGULARL;
                } else if (prod < 0 && dir > 0) {
                    type = Point.Type.SPLIT;
                }
            } else if (p1.y == p2.y && p3.y > p2.y) {
                if (prod > 0 && dir < 0) {
                    type = Point.Type.SPLIT;
                } else if (prod < 0 && dir < 0) {
                    type = Point.Type.REGULARL;
                } else if (prod > 0 && dir > 0) {
                    type = Point.Type.START;
                } else if (prod < 0 && dir > 0) {
                    type = Point.Type.REGULARR;
                }
            } else if (p1.y < p2.y && p3.y < p2.y) { // end or merge
                // now check if the polygon is above or below
                if ((prod < 0 && dir < 0) || (prod > 0 && dir > 0)) { // below
                    type = Point.Type.END;
                } else { // above
                    type = Point.Type.MERGE;
                }
            } else if (p1.y < p2.y && p3.y == p2.y) {
                if (prod > 0 && dir < 0) {
                    type = Point.Type.REGULARL;
                } else if (prod < 0 && dir < 0) {
                    type = Point.Type.END;
                } else if (prod > 0 && dir > 0) {
                    type = Point.Type.REGULARR;
                } else if (prod < 0 && dir > 0) {
                    type = Point.Type.MERGE;
                }
            } else if (p1.y == p2.y && p3.y < p2.y) {
                if (prod > 0 && dir < 0) {
                    type = Point.Type.MERGE;
                } else if (prod < 0 && dir < 0) {
                    type = Point.Type.REGULARR;
                } else if (prod > 0 && dir > 0) {
                    type = Point.Type.END;
                } else if (prod < 0 && dir > 0) {
                    type = Point.Type.REGULARL;
                }
            } else if (p1.y == p2.y && p3.y == p2.y) {
                System.err.println("/!\\ 3 points aligned horizontaly");
            } else { // regular
                // now check if the polygon is at the right or the left
                if ((p1.y >= p2.y && dir < 0) || p1.y < p2.y && dir > 0) {
                    type = Point.Type.REGULARR;
                } else {
                    type = Point.Type.REGULARL;
                }
            }
            p2.type = type;
        }

    }

    /** mark all given points */
    public static void markPointsWithChains(CircularList<Point> points) {
        if (points.size() < 3)
            return;

        Side side = Side.UNKNOWN;

        LinkNode<Point> topNode = null;
        Point p1 = null, p2 = null, p3 = null, top = null, bottom = null;
        for (int i = 0; i < points.size(); ++i) {
            p1 = points.elementAt(i == 0 ? points.size() - 1 : i - 1);
            p2 = points.elementAt(i);
            p3 = points.elementAt((i + 1) % points.size());

            if (p2.y <= p1.y && p2.y <= p3.y) { // p2 is the top
                if (top == null || top.y > p2.y) {
                    top = p2;
                    topNode = points.getNode(i);
                }
            } else if (p2.y >= p1.y && p2.y >= p3.y) { // p2 is the bottom
                if (bottom == null || bottom.y < p2.y) {
                    bottom = p2;
                }
            }
        }
        if (p1 == null || p2 == null || p3 == null || top == null
                || bottom == null) {
            System.err.println("error marking points : should never happen");
            return; // to avoid null pointer exc
        }

        side = Utils.getDirection(points) < 0 ? Side.RIGHT : Side.LEFT;
        topNode.getValue().side = side;
        for (int i = 0; i < points.size() - 1; ++i) {
            if (topNode.getNext().getValue().equals(bottom)) {
                side = (side == Point.Side.RIGHT) ? Point.Side.LEFT
                        : Point.Side.RIGHT;
            }
            topNode.getNext().getValue().side = side;
            topNode = topNode.getNext();
        }
    }

    public static Vector<Point> mergeChains(CircularList<Point> points, int dir) {
        if (points.size() <= 1)
            return null;

        int posR = -1, posL = -1;

        Vector<Point> left = new Vector<Point>();
        Vector<Point> right = new Vector<Point>();
        for (Point p : points) {
            if (p.side == Point.Side.RIGHT) {
                if (!left.isEmpty()) {
                    if (posR == -1) {
                        if (dir > 0) {
                            posR = right.size();
                        } else {
                            posR = 0;
                        }
                    }
                    right.add(posR, p);
                    if (dir < 0) {
                        posR++;
                    }
                } else {
                    if (dir > 0)
                        right.add(0, p);
                    else
                        right.add(p);
                }
            } else if (p.side == Point.Side.LEFT) {
                if (!right.isEmpty()) {
                    if (posL == -1) {
                        if (dir > 0) {
                            posL = 0;
                        } else {
                            posL = left.size();
                        }
                    }
                    left.add(posL, p);
                    if (dir > 0) {
                        ++posL;
                    }
                } else {
                    if (dir > 0)
                        left.add(p);
                    else
                        left.add(0, p);
                }
            }
        }
        return Utils.merge(left, right);
    }

}
