package computational_geometry.model.algorithms;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import computational_geometry.model.beans.Line;
import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Segment;
import computational_geometry.model.core.Lines;
import computational_geometry.model.core.PointComparatorX;
import computational_geometry.model.core.Utils;
import computational_geometry.model.data_structures.DCEL.Edge;
import computational_geometry.model.data_structures.DCEL.Face;
import computational_geometry.model.data_structures.DCEL.Vert;
import computational_geometry.model.data_structures.VoronoiDiagram;
import computational_geometry.model.data_structures.VoronoiDiagram.VorCell;
import computational_geometry.model.traces.HullResult;
import computational_geometry.model.traces.VoronoiTrace;

public class Voronoi {

    public static Rectangle bound = new Rectangle();

    /**
     * Compute voronoi diagram of a given list of points
     * using the divide and conquer method
     * @param points
     * @return
     */
    public static VoronoiTrace ComputeVoronoiDiagram(List<Point> points) {
        if (points.size() < 2) {
            return null;
        }
        List<Point> sortedPoints = new ArrayList<Point>(points);
        Collections.sort(sortedPoints, new PointComparatorX());
        return Vor(sortedPoints);
    }

    /**
     * Recursively compute the actual voronoi diagram of a given list of points
     * assuming the list is sorted
     * @param points : the sorted (by x) list of points
     * @return
     */
    private static VoronoiTrace Vor(List<Point> points) {
        VoronoiTrace trace = new VoronoiTrace();
        VoronoiDiagram vor;
        if (points.size() == 2) {
            vor = handle2PointsVor(points);
        } else if (points.size() == 3) {
            vor = handle3PointsVor(points);
        } else {
            vor = handleNPointsVor(points);
        }

        trace.vor = vor;
        return trace;
    }

    /**
     * Compute the voronoi diagram of two points
     * @param points : the sorted (by x) list of points
     * @return
     */
    private static VoronoiDiagram handle2PointsVor(List<Point> points) {
        VoronoiDiagram vor = new VoronoiDiagram();
        Point p1 = points.get(0);
        Point p2 = points.get(1);
        Line bisector = Lines.findBisector(p1, p2);

        Edge e1 = vor.new Edge();
        Edge e2 = vor.new Edge();

        VorCell c1 = vor.new VorCell(p1, e1);
        VorCell c2 = vor.new VorCell(p2, e2);

        Vert v1 = vor.new Vert(bisector.findLowerPoint(bound), e1);
        Vert v2 = vor.new Vert(bisector.findUpperPoint(bound), e2);

        e1.fill(v1, e2, c1, e1, e1);
        e2.fill(v2, e1, c2, e2, e2);

        vor.addEdge(e1);
        vor.addEdge(e2);
        vor.addFace(c1);
        vor.addFace(c2);

        return vor;
    }

    /**
     * Compute the voronoi diagram of three points
     * @param points : the sorted (by x) list of points
     * @return
     */
    private static VoronoiDiagram handle3PointsVor(List<Point> points) {
        VoronoiDiagram vor = new VoronoiDiagram();
        Point p1 = points.get(0);
        Point p2 = points.get(1);
        Point p3 = points.get(2);
        Line l1 = Lines.findLine(p1, p2);
        Line l2 = Lines.findLine(p1, p3);
        Line l3 = Lines.findLine(p2, p3);
        Line b1 = Lines.findBisector(p1, p2);
        Line b2 = Lines.findBisector(p1, p3);
        Line b3 = Lines.findBisector(p2, p3);
        Point inter = b1.findIntersection(b2);

        VorCell c1;
        VorCell c2;
        VorCell c3;
        if (inter == null) {
            // two bisectors,
            // assuming that point is sorted, it's bisector(p1, p2) and bisector(p2, p3)
            Edge e11 = vor.new Edge();
            Edge e12 = vor.new Edge();
            Edge e21 = vor.new Edge();
            Edge e22 = vor.new Edge();

            c1 = vor.new VorCell(p1, e11);
            c2 = vor.new VorCell(p2, e12);
            c3 = vor.new VorCell(p3, e22);

            Vert v1 = vor.new Vert(b1.findLowerPoint(bound), e11);
            Vert v2 = vor.new Vert(b1.findUpperPoint(bound), e12);
            Vert v3 = vor.new Vert(b3.findLowerPoint(bound), e21);
            Vert v4 = vor.new Vert(b3.findUpperPoint(bound), e22);

            e11.fill(v1, e12, c1, e11, e11);
            e12.fill(v2, e11, c2, e12, e12);
            e21.fill(v3, e22, c2, e21, e21);
            e22.fill(v4, e21, c3, e22, e22);

            vor.addEdge(e11);
            vor.addEdge(e12);
            vor.addEdge(e21);
            vor.addEdge(e22);

        } else {    // inter != null <=> orientation(p1, p2, p3) != 0

            // edges naming convention :
            // e11 is the edge of cell c1 which origin is inter, e12 is it's twin
            // e21 is the edge of cell c2 which origin is inter, e22 is it's twin
            // e31 is the edge of cell c3 which origin is inter, e32 is it's twin
            Edge e11 = vor.new Edge();
            Edge e12 = vor.new Edge();
            Edge e21 = vor.new Edge();
            Edge e22 = vor.new Edge();
            Edge e31 = vor.new Edge();
            Edge e32 = vor.new Edge();

            // voronoi cells naming convention :
            // c1 is the cell whose site is p1
            // c2 is the cell whose site is p2
            // c3 is the cell whose site is p3
            c1 = vor.new VorCell(p1, e11);
            c2 = vor.new VorCell(p2, e21);
            c3 = vor.new VorCell(p3, e31);

            // the three points bounding the bisectors in the rectangle
            Point q1, q2, q3;
            q1 = b1.findUpperPoint(bound);
            if (l1.findSide(p3) == l1.findSide(q1)) {
                q1 = b1.findLowerPoint(bound);
            }
            q2 = b2.findUpperPoint(bound);
            if (l2.findSide(p2) == l2.findSide(q2)) {
                q2 = b2.findLowerPoint(bound);
            }
            q3 = b3.findUpperPoint(bound);
            if (l3.findSide(p1) == l3.findSide(q3)) {
                q3 = b3.findLowerPoint(bound);
            }

            Vert v0 = vor.new Vert(inter, e12);
            // vertex naming convention :
            // v1 is the infinite point in bisector(p1, p2),
            // v2 is the infinite point in bisector(p1, p3),
            // v3 is the infinite point in bisector(p3, p4),
            Vert v1, v2, v3;
            if (Utils.orientation(p1, p2, p3) < 0) {
                v1 = vor.new Vert(q1, e11);
                v2 = vor.new Vert(q2, e31);
                v3 = vor.new Vert(q3, e21);
                e11.fill(v1, e12, c1, e32, e32);
                e12.fill(v0, e11, c2, e21, e21);
                e21.fill(v3, e22, c2, e12, e12);
                e22.fill(v0, e21, c3, e31, e31);
                e31.fill(v2, e32, c3, e22, e22);
                e32.fill(v0, e31, c1, e11, e11);
            } else {    // Utils.orientation(p1, p2, p3) > 0
                v1 = vor.new Vert(q1, e21);
                v2 = vor.new Vert(q2, e11);
                v3 = vor.new Vert(q3, e31);
                e11.fill(v2, e12, c1, e22, e22);
                e12.fill(v0, e11, c3, e31, e31);
                e21.fill(v1, e22, c2, e32, e32);
                e22.fill(v0, e21, c1, e11, e11);
                e31.fill(v3, e32, c3, e12, e12);
                e32.fill(v0, e31, c2, e21, e21);
            }

            vor.addEdge(e11);
            vor.addEdge(e12);
            vor.addEdge(e21);
            vor.addEdge(e22);
            vor.addEdge(e31);
            vor.addEdge(e32);
        }
        vor.addFace(c1);
        vor.addFace(c2);
        vor.addFace(c3);

        return vor;
    }

    private static VoronoiDiagram handleNPointsVor(List<Point> points) {
        int splitIndex = points.size()/2;
        List<Point> L1 = points.subList(0, splitIndex);
        List<Point> L2 = points.subList(splitIndex, points.size());

        VoronoiDiagram vor1 = Voronoi.Vor(L1).vor;
        VoronoiDiagram vor2 = Voronoi.Vor(L2).vor;

        return mergeVor(vor1, vor2, ConvexHull.ConvexHullDivideAndConquer(points));
    }

    private static VoronoiDiagram mergeVor(VoronoiDiagram vor1,
                                           VoronoiDiagram vor2,
                                           HullResult hullResult) {
        VoronoiDiagram res = new VoronoiDiagram();
        Point u, v;
        u = hullResult.getUpperTangent().u;
        v = hullResult.getUpperTangent().v;
        Line l = Lines.findBisector(u, v);

        VorCell cr = null, cl = null;
        Iterator<Face> itFace = vor1.getFaceIterator();
        while (itFace.hasNext()) {
            VorCell cell = (VorCell)itFace.next();
            if (cell.getSite().equals(u)) {
                cl = cell;
                break;
            }
        }
        itFace = vor2.getFaceIterator();
        while (itFace.hasNext()) {
            VorCell cell = (VorCell)itFace.next();
            if (cell.getSite().equals(v)) {
                cr = cell;
                break;
            }
        }
        if (cl == null || cr == null) {
            System.err.println("Unable to find left or right cell");
            return null;
        }

        List<Point> divPoints = new ArrayList<Point>();

        // the ray will be l bounded at y by rayUpperBound
        Point lastDivPoint = l.findUpperPoint(bound);
        int rayUpperBound = Integer.MIN_VALUE;//Double.NEGATIVE_INFINITY;
        Edge lastDivEdge = vor1.new Edge(); // downward
        Edge twinLastDivEdge = vor1.new Edge();

        Vert v1 = vor1.new Vert(lastDivPoint, lastDivEdge);
        Vert v2 = vor1.new Vert(l.findLowerPoint(bound), twinLastDivEdge);

        lastDivEdge.fill(v1, twinLastDivEdge, cr, lastDivEdge, lastDivEdge);
        twinLastDivEdge.fill(v2, lastDivEdge, cl, twinLastDivEdge, twinLastDivEdge);

        res.addEdge(lastDivEdge);
        res.addEdge(twinLastDivEdge);

        Segment curSeg;
        Point interCr = null, interCl = null;
        divPoints.add(lastDivPoint);
        Edge eCr = cr.getEdge();
        Edge eCl = cl.getEdge();
        while (!(curSeg = new Segment(cl.getSite(), cr.getSite())).equals(hullResult.getLowerTangent())) {
            // find intersection between the dividing line and the right cell bounds
            do {
                Segment s = new Segment(eCr.getOrigin().getPoint(), eCr.getTwin().getOrigin().getPoint());
                if (!eCr.isNew && (interCr = Lines.findIntersection(l, s)) != null) {
                    if (interCr.y <= rayUpperBound) {
                        interCr = null;
                    } else {
                        break;
                    }
                }
                eCr = eCr.getNext();
            } while (!eCr.equals(cr.getEdge()));
            // find intersection between the dividing line and the right cell bounds
            do {
                Segment s = new Segment(eCl.getOrigin().getPoint(), eCl.getTwin().getOrigin().getPoint());
                if (!eCl.isNew && (interCl = Lines.findIntersection(l, s)) != null) {
                    if (interCl.y <= rayUpperBound) {
                        interCl = null;
                    } else {
                        break;
                    }
                }
                eCl = eCl.getNext();
            } while (!eCl.equals(cl.getEdge()));
            if (interCr == null && interCl == null) {
                System.err.println("OMG : ray didn't find anything");
                break;
            } else if (interCl == null || (interCr != null && interCl.y > interCr.y+2)) {
                cr = (VorCell) eCr.getTwin().getFace();
                l = Lines.findBisector(cl.getSite(), cr.getSite());

                Edge newEdge = vor1.new Edge();
                Edge twinNewEdge = vor1.new Edge();
                newEdge.isNew = true;
                twinNewEdge.isNew = true;

                Vert newVert = vor1.new Vert(interCr, newEdge);
                Vert newEndDivEdge = vor1.new Vert(l.findLowerPoint(bound), twinNewEdge);
                newEndDivEdge.getPoint().setInfinite(false);

                newEdge.fill(newVert, twinNewEdge, cr, eCr.getTwin().getNext(), eCr.getTwin());
                twinNewEdge.fill(newEndDivEdge, newEdge, cl, lastDivEdge.getTwin(), eCr.getTwin().getNext());
                lastDivEdge.setNext(eCr);
                lastDivEdge.getTwin().setOrigin(newVert);
                eCr.setOrigin(newVert);
                eCr.getTwin().setNext(newEdge);
                if (eCr.getNext().equals(eCr.getNext())) {
                    eCr.setNext(lastDivEdge);
                }

                res.addEdge(newEdge);
                res.addEdge(twinNewEdge);

                eCr = newEdge.getNext();
                lastDivEdge = newEdge;
                lastDivPoint = interCr;
            } else if (interCr == null || (interCl != null && interCl.y+2 < interCr.y)) {
                cl = (VorCell) eCl.getTwin().getFace();
                l = Lines.findBisector(cl.getSite(), cr.getSite());
                Edge newEdge = vor1.new Edge();
                Edge twinNewEdge = vor1.new Edge();
                newEdge.isNew = true;
                twinNewEdge.isNew = true;

                Vert newVert = vor1.new Vert(interCl, newEdge);
                Vert newEndDivEdge = vor1.new Vert(l.findLowerPoint(bound), twinNewEdge);
                newEndDivEdge.getPoint().setInfinite(false);

                newEdge.fill(newVert, twinNewEdge, cl, lastDivEdge.getNext(), eCl.getTwin().getPrev());
                twinNewEdge.fill(newEndDivEdge, newEdge, cl, eCl.getTwin(), eCl.getTwin().getNext());
                lastDivEdge.setNext(newEdge);
                lastDivEdge.getTwin().setOrigin(newVert);
                eCl.setNext(lastDivEdge.getTwin());
                eCl.getTwin().setOrigin(newVert);
                if (eCl.getTwin().getNext().equals(eCl.getTwin().getNext())) {
                    eCl.getTwin().setNext(twinNewEdge);
                }

                res.addEdge(newEdge);
                res.addEdge(twinNewEdge);

                eCl = eCl.getTwin();
                lastDivEdge = newEdge;
                lastDivPoint = interCl;
            } else {    // interCl = interCr
                cl = (VorCell) eCl.getTwin().getFace();
                cr = (VorCell) eCr.getTwin().getFace();
                l = Lines.findBisector(cl.getSite(), cr.getSite());
                Edge newEdge = vor1.new Edge();
                Edge twinNewEdge = vor1.new Edge();
                newEdge.isNew = true;
                twinNewEdge.isNew = true;

                Vert newVert = vor1.new Vert(interCl, newEdge);
                Vert newEndDivEdge = vor1.new Vert(l.findLowerPoint(bound), twinNewEdge);
                newEndDivEdge.getPoint().setInfinite(false);

                newEdge.fill(newVert, twinNewEdge, cr, eCr.getTwin().getNext(), eCr.getTwin());
                twinNewEdge.fill(newEndDivEdge, newEdge, cl, eCl.getTwin(), eCl.getTwin().getNext());
                lastDivEdge.setNext(eCr);
                lastDivEdge.getTwin().setOrigin(newVert);
                eCl.setNext(lastDivEdge.getTwin());
                eCl.getTwin().setOrigin(newVert);
                eCr.setOrigin(newVert);
                eCr.getTwin().setNext(newEdge);
                if (eCr.getNext().equals(eCr.getNext())) {
                    eCr.setNext(lastDivEdge);
                }
                if (eCl.getTwin().getNext().equals(eCl.getTwin().getNext())) {
                    eCl.getTwin().setNext(twinNewEdge);
                }

                res.addEdge(newEdge);
                res.addEdge(twinNewEdge);

                eCl = cl.getEdge();
                eCr = cr.getEdge();
                lastDivEdge = newEdge;
                lastDivPoint = interCl;
            }
            divPoints.add(lastDivPoint);
            rayUpperBound = (int) Math.ceil(lastDivPoint.y);//+1;
            interCr = interCl = null;
        }
        l = Lines.findBisector(curSeg.u, curSeg.v);
        Point endOfDiv = l.findLowerPoint(bound);
        endOfDiv.setInfinite(false);
        divPoints.add(endOfDiv);
        res.lastDivideLine = divPoints;

        itFace = vor1.getFaceIterator();
        while (itFace.hasNext()) {
            res.addFace(itFace.next());
        }
        itFace = vor2.getFaceIterator();
        while (itFace.hasNext()) {
            res.addFace(itFace.next());
        }
        Iterator<Edge> itEdge = vor1.getEdgeIterator();
        while (itEdge.hasNext()) {
            res.addEdge(itEdge.next());
        }
        itEdge = vor2.getEdgeIterator();
        while (itEdge.hasNext()) {
            res.addEdge(itEdge.next());
        }
        return res;
    }

}
