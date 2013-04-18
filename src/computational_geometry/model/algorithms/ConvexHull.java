package computational_geometry.model.algorithms;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import computational_geometry.model.beans.Diagonal;
import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Polygon;
import computational_geometry.model.beans.Segment;
import computational_geometry.model.core.PointComparatorX;
import computational_geometry.model.core.Utils;
import computational_geometry.model.data_structures.CircularList;
import computational_geometry.model.data_structures.LinkNode;
import computational_geometry.model.traces.HullResult;

public class ConvexHull {

    /**
     * Compute the convex hull of a simple polygon
     * TODO : review to result points and not segments
     * @param polygon
     * @return
     */
    public static HullResult polygonConvexHull(Polygon polygon) {
        HullResult trace = new HullResult();
        if (polygon.getNbPoints() <= 3)
            return trace;

        Queue<LinkNode<Point>> P = new ArrayDeque<LinkNode<Point>>();
        List<LinkNode<Point>> llnp = new ArrayList<LinkNode<Point>>(polygon
                .getPoints().getNodes());
        if (Utils.getDirection(polygon.getPoints()) > 0) {
            System.out.println("reverting");
            Collections.reverse(llnp);
        }
        P.addAll(llnp);

        // Q
        Stack<LinkNode<Point>> stackEnvelop = new Stack<LinkNode<Point>>();

        double maxX = 0;
        Point rightMost = null;
        for (Point p : polygon.getPoints()) {
            if (p.x >= maxX) {
                maxX = p.x;
                rightMost = p;
            }
        }
        if (rightMost == null) {
            System.err.println("cannot find rightmost point for hull, "
                    + "aborting algorithm");
            return null;
        }

        // find the first convex angle
        LinkNode<Point> lp1 = P.poll();
        LinkNode<Point> lp2 = P.poll();
        LinkNode<Point> lp3 = P.peek();
        while (Utils.crossProduct(lp1.getValue(), lp2.getValue(),
                lp3.getValue()) > 0) {// && dir < 0) ||
        //               (crossProduct(lp1.getValue(), lp2.getValue(), lp3.getValue()) < 0 && dir > 0) {
            P.poll();
            P.add(lp1);
            lp1 = lp2;
            lp2 = lp3;
            lp3 = P.peek();
        }

        //        stackEnvelop.push(lp1);
        stackEnvelop.push(lp2);
        //        stackEnvelop.push(lp3);
        P.add(lp1);
        P.add(lp2);
        lp1 = lp2 = lp3 = null;

        while (!P.isEmpty()) {
            LinkNode<Point> vNode = P.poll();
            if (stackEnvelop.size() < 2) {
                stackEnvelop.push(vNode);
                continue;
            }

            LinkNode<Point> qiNode = stackEnvelop.pop();
            Point qi = qiNode.getValue();
            Point qim = stackEnvelop.peek().getValue();
            Point u = qiNode.getPrev().getValue();
            stackEnvelop.push(qiNode);

            if (Utils.crossProduct(qim, qi, vNode.getValue()) < 0) { // 1, 3, 4
                if (Utils.crossProduct(u, qi, vNode.getValue()) <= 0) { // 3, 4
                    if (Utils.crossProduct(rightMost, qi, vNode.getValue()) <= 0) { // 3
                        stackEnvelop.push(vNode);
                    } else {
                        while (!P.isEmpty()
                                && Utils.crossProduct(rightMost, qi, P.peek()
                                        .getValue()) > 0) { // 4
                            P.poll();
                        }
                    }
                } else { // 1
                    while (!P.isEmpty()
                            && Utils.crossProduct(qi, qim, P.peek().getValue()) > 0) {
                        P.poll();
                    }
                }
            } else { // 2
                while (Utils.crossProduct(qim, qi, vNode.getValue()) > 0) {
                    //                    if (stackEnvelop.size() >= 2) {
                    stackEnvelop.pop();
                    if (stackEnvelop.size() <= 2)
                        break;
                    qiNode = stackEnvelop.pop();
                    qi = qiNode.getValue();
                    qim = stackEnvelop.peek().getValue();
                    stackEnvelop.push(qiNode);
                    //                    }
                    //                    else {
                    //                        stackEnvelop.push(vNode);
                    //                        P.add(P.poll());
                    //                        stackEnvelop.push(P.peek());
                    //                        P.add(P.poll());
                    //
                    //                        P.add(qiNode);
                    //                        P.add(vNode);
                    //                    }
                }

                stackEnvelop.push(vNode);
            }
        }

        //        Point headOfStack = stackEnvelop.peek().getValue();
        while (stackEnvelop.size() >= 2) {
            trace.addSegment(new Diagonal(stackEnvelop.pop().getValue(),
                    stackEnvelop.peek().getValue()));
            //            polygon.addDiagonal(new Diagonal(stackEnvelop.pop().getValue(),
            //                                             stackEnvelop.peek().getValue()));
        }
        //        polygon.addDiagonal(new Diagonal(headOfStack, stackEnvelop.pop().getValue()));

        return trace;
    }

    /**
     * Compute the convex hull of a given set of point
     * using the divide and conquer paradigm
     * @param points
     * @return The set of points that are in the convex hull - in counter-clockwise order
     */
    public static HullResult ConvexHullDivideAndConquer(List<Point> points) {
        List<Point> sortedList = new ArrayList<Point>(points);
        Collections.sort(sortedList, new PointComparatorX());
        return hull(sortedList);
    }

    /**
     * Compute the convex hull of a given set of points
     * we assume that the points are sorted by their x coordinate
     * @param points
     * @return The points belonging to the hull ordered counter-clockwise
     */
    public static HullResult hull(List<Point> points) {
        HullResult res = new HullResult();
        if (points.size() < 3) {
            CircularList<Point> list = new CircularList<Point>();
            if (points.size() >= 1) {
                list.addFirst(points.get(0));
                res.setHull(list.getNode(0));
            }
            if (points.size() >= 2) {
                list.addFirst(points.get(1));
            }
        } else if (points.size() == 3) {
            CircularList<Point> list = new CircularList<Point>();
            Point p, q, r;
            p = points.get(0);
            q = points.get(1);
            r = points.get(2);
            if (Utils.orientation(p, q, r) < 0) {
                list.addFirst(r);
                list.addFirst(q);
                list.addFirst(p);
            } else {
                list.addFirst(p);
                list.addFirst(q);
                list.addFirst(r);
            }
            res.setHull(list.getNode(0));
        } else {
            int splitIndex = points.size() / 2;
            LinkNode<Point> hull1 = hull(points.subList(0, splitIndex)).getHull();
            LinkNode<Point> hull2 = hull(points.subList(splitIndex, points.size())).getHull();
            res = unionHull(hull1, hull2);
        }
        return res;
    }

    public static HullResult unionHull(LinkNode<Point> hull1, LinkNode<Point> hull2) {
        LinkNode<Point> tmp, u, v, uh, vh, ul, vl;
        tmp = u = hull1;
        do {
            if (tmp.getValue().x > u.getValue().x)
                u = tmp;
            tmp = tmp.getNext();
        } while (!tmp.getValue().equals(hull1.getValue()));
        tmp = v = hull2;
        do {
            if (tmp.getValue().x < v.getValue().x)
                v = tmp;
            tmp = tmp.getNext();
        } while (!tmp.getValue().equals(hull2.getValue()));

        uh = u;
        vh = v;
        ul = u;
        vl = v;
        boolean b;
        while ((b = Utils.orientation(vh.getPrev().getValue(), vh.getValue(), uh.getValue()) > 0) ||
               Utils.orientation(uh.getValue(), uh.getNext().getValue(), vh.getValue()) > 0) {
            if (b) {
                vh = vh.getPrev();
            } else {
                uh = uh.getNext();
            }
        }
        while ((b = Utils.orientation(vl.getValue(), vl.getNext().getValue(), ul.getValue()) > 0) ||
               Utils.orientation(ul.getValue(), ul.getPrev().getValue(), vl.getValue()) < 0) {
            if (b) {
                vl = vl.getNext();
            } else {
                ul = ul.getPrev();
            }
        }
        uh.setPrev(vh);
        vh.setNext(uh);
        ul.setNext(vl);
        vl.setPrev(ul);
        HullResult res = new HullResult();
        res.setHull(uh);
        res.setLowerTangent(new Segment(ul.getValue(), vl.getValue()));
        res.setUpperTangent(new Segment(uh.getValue(), vh.getValue()));
        return res;
    }

}
