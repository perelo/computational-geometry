package computational_geometry.model.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import computational_geometry.model.beans.Diagonal;
import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Polygon;
import computational_geometry.model.beans.Point.Side;
import computational_geometry.model.beans.Segment;
import computational_geometry.model.beans.Segment.WayMark;
import computational_geometry.model.core.Polygons;
import computational_geometry.model.core.Utils;
import computational_geometry.model.traces.AlgoTrace;
import computational_geometry.model.traces.MonotonTriangTrace;
import computational_geometry.model.traces.MonotonTriangTrace.Event;
import computational_geometry.model.traces.TriangulationTrace;

public class Triangulation {

    /**
     * Compute the diagonals triangulating a monotonous polygon
     * @param polygon
     * @return
     */
    public static MonotonTriangTrace triangulateMonotonPolygon(Polygon polygon) {
        MonotonTriangTrace trace = new MonotonTriangTrace();
        if (polygon.getNbPoints() <= 2 || !Polygons.isMonotonous(polygon))
            return trace;

        Stack<Point> stack = new Stack<Point>();

        int dir = Utils.getDirection(polygon.getPoints());
        Polygons.markPointsWithChains(polygon.getPoints());
        Vector<Point> points = Polygons.mergeChains(polygon.getPoints(), dir);

        Event traceEvent = trace.createNewEvent(points.elementAt(0));
        trace.addEvent(traceEvent);
        traceEvent = trace.createNewEvent(points.elementAt(1));
        trace.addEvent(traceEvent);

        stack.push(points.elementAt(0));
        stack.push(points.elementAt(1));
        for (int i = 2; i < points.size() - 1; ++i) {
            Point p = points.elementAt(i);
            traceEvent = trace.createNewEvent(p);
            trace.addEvent(traceEvent);
            Side side = stack.peek().side;

            if (p.side == side) {

                for (; stack.size() > 1;) {
                    Point oldHead = stack.pop();
                    Point newHead = stack.peek();
                    double vectProd;

                    Point v1 = new Point(newHead.x - oldHead.x, newHead.y
                            - oldHead.y);
                    Point v2 = new Point(p.x - oldHead.x, p.y - oldHead.y);
                    vectProd = v1.x * v2.y - v2.x * v1.y;

                    if ((vectProd >= 0 && side == Side.RIGHT)
                            || (vectProd <= 0 && side == Side.LEFT)) {
                        stack.push(oldHead);
                        break;
                    } else {
                        traceEvent.addDiagonal(new Diagonal(p, stack.peek()));
                    }
                }
                stack.push(p);

            } else { // p.mark != side

                while (stack.size() > 1) {
                    traceEvent.addDiagonal(new Diagonal(p, stack.pop()));
                }
                stack.pop(); // pop last element
                stack.push(points.elementAt(i - 1));
                stack.push(p);
            }

        }

        Point p = points.lastElement();
        traceEvent = trace.createNewEvent(p);
        trace.addEvent(traceEvent);
        stack.pop(); // stack cannot be empty
        for (; stack.size() > 1;) {
            traceEvent.addDiagonal(new Diagonal(p, stack.pop()));
        }

        return trace;
    }

    /**
     * Compute the diagonals triangulating a simple polygon
     * @param polygon
     * @return
     */
    public static AlgoTrace triangulateSimplePolygon(Polygon polygon) {
        TriangulationTrace trace = new TriangulationTrace();
        if (polygon.getNbPoints() < 3)
            return trace;
        int dir = Utils.getDirection(polygon.getPoints());
        if (dir > 0) { // TODO the algo work w/o this
            System.err.println("wrong direction");
            return null;
        }

        /* reset UV marks */
        for (Point p : polygon.getPoints()) {
            p.getDiagonals().clear();
            p.clearSegments();
        }

        if (Polygons.isMonotonous(polygon)) {
            return triangulateMonotonPolygon(polygon);
        }

        trace.setMonotonSubdivisionTrace(Monotonization.monotonisePolygon(polygon));

        // triangulate each monotonous piece found
        for (Polygon p : trace.getMonotonSubdivisionTrace().getMonotonPolygons()) {
            trace.addMonotonPolygons(p);
            trace.addMonotonPolygonsTriangTrace(triangulateMonotonPolygon(p));
        }
        return trace;
    }

    public static List<Polygon> retrieveMonotonPolygons(Polygon polygon) {
        List<Polygon> monotonPolygons = new ArrayList<Polygon>();

        /* prepare all marks */
        for (Point p : polygon.getPoints()) {
            p.markSegmentsInSortedList();
            p.atBegin.setFlagUV(false);
            p.atEnd.setFlagUV(false);
            /* don't retrieve the outside polygon */
            p.atBegin.setFlagVU(true);
            p.atEnd.setFlagVU(true);
        }
        for (Segment s : polygon.getSegments()) {
            Polygon p1 = new Polygon();
            Polygon p2 = new Polygon();
            calculatePolygon(p1, s, Segment.WayMark.UV);
            calculatePolygon(p2, s, Segment.WayMark.VU);
            if (p1.getNbPoints() > 0) {
                monotonPolygons.add(p1);
            }
            if (p2.getNbSegments() > 0) {
                monotonPolygons.add(p2);
            }
        }
        return monotonPolygons;
    }

    /**
     * Compute the monotonous polygon of which a segment belongs
     * @param polygon
     * @param s
     * @param mark
     */
    private static void calculatePolygon(Polygon polygon, Segment s,
            Segment.WayMark mark) {

        Segment nextSeg = null;
        Segment.WayMark nextMark = null;
        switch (mark) {
        case UV:
            if (s.isFlagUV())
                return;
            s.setFlagUV(true);
            nextSeg = s.v.getNextSegInSortedList(s.getPosInVSortedList());
            nextMark = (s.v.equals(nextSeg.u) ? WayMark.UV : WayMark.VU);
            polygon.addPoint(s.v);
            break;
        case VU:
            if (s.isFlagVU())
                return;
            s.setFlagVU(true);
            nextSeg = s.u.getNextSegInSortedList(s.getPosInUSortedList());
            nextMark = (s.u.equals(nextSeg.u) ? WayMark.UV : WayMark.VU);
            polygon.addPoint(s.u);
            break;
        }
        calculatePolygon(polygon, nextSeg, nextMark);
    }

}
