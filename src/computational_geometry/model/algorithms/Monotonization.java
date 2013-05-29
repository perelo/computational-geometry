package computational_geometry.model.algorithms;

import java.util.PriorityQueue;

import computational_geometry.model.beans.Diagonal;
import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Polygon;
import computational_geometry.model.beans.Point.Type;
import computational_geometry.model.beans.Segment;
import computational_geometry.model.core.Polygons;
import computational_geometry.model.core.Utils;
import computational_geometry.model.data_structures.LinkNode;
import computational_geometry.model.data_structures.SweepState;
import computational_geometry.model.traces.MonotonTriangTrace;
import computational_geometry.model.traces.MonotonTriangTrace.Event;

public class Monotonization {

    /**
     * Compute the diagonals monotonizing a simple polygon
     * @param polygon
     * @return
     */
    public static MonotonTriangTrace monotonisePolygon(Polygon polygon) {
        MonotonTriangTrace trace = new MonotonTriangTrace();
        if (Polygons.isMonotonous(polygon))
            return trace;

        /* reset UV marks */
        for (Point p : polygon.getPoints()) {
            p.getDiagonals().clear();
            p.clearSegments();
        }

        int dir = Utils.getDirection(polygon.getPoints());
        Polygons.markPointsWithType(polygon.getPoints(), dir);

        // we need the nodes, not only the point, so we can get v_{i} and v_{i-1}
        PriorityQueue<LinkNode<Point>> queue = new PriorityQueue<LinkNode<Point>>(
                polygon.getPoints().getNodes());
        SweepState sweepState = new SweepState(); // binary tree A

        for (; !queue.isEmpty();) {
            LinkNode<Point> v = queue.poll();
            Event traceEvent = trace.createNewEvent(v.getValue());
            trace.addEvent(traceEvent);
            sweepState.setEvent(v.getValue());

            switch (v.getValue().type) {
            case START: {
                handleStartVertex(v, sweepState, dir);
                break;
            }
            case END: {
                handleEndVertex(v, sweepState, traceEvent, dir);
                break;
            }
            case SPLIT: {
                handleSplitVertex(v, sweepState, traceEvent, dir);
                break;
            }
            case MERGE: {
                handleMergeVertex(v, sweepState, traceEvent, dir);
                break;
            }
            case UNKNOWN: {
                break;
            }
            default: {
                handleRegularVertex(v, sweepState, traceEvent, dir);
                break;
            }
            }
        }

        trace.setMonotonPolygons(Triangulation.retrieveMonotonPolygons(polygon, dir));
        return trace;
    }

    private static Segment getEi(Point p, boolean im, int dir) { // im = i - 1
        if (p == null)
            return null;

        if ((im && dir > 0) || (!im && dir < 0)) {
            return p.atEnd;
        } else {
            return p.atBegin;
        }
    }

    private static void handleStartVertex(LinkNode<Point> v,
            SweepState sweepState, int dir) {

        // get e_{i}
        Segment ei = getEi(v.getValue(), false, dir);

        sweepState.insert(ei);
        ei.helper = v.getValue();
    }

    private static void handleEndVertex(LinkNode<Point> v,
            SweepState sweepState, Event traceEvent, int dir) {

        // get e_{i-1}
        Segment eim = getEi(v.getValue(), true, dir);
        if (eim.helper.type == Point.Type.MERGE) {
            traceEvent.addDiagonal(new Diagonal(v.getValue(), eim.helper));
        }
        sweepState.delete(eim);
    }

    private static void handleSplitVertex(LinkNode<Point> v,
            SweepState sweepState, Event traceEvent, int dir) {

        Segment ej = sweepState.findFirstLeftOfEvent(sweepState.getRoot());
        traceEvent.addDiagonal(new Diagonal(v.getValue(), ej.helper));
        ej.helper = v.getValue();

        // get e_{i}
        Segment ei = getEi(v.getValue(), false, dir);
        ei.helper = v.getValue();
        sweepState.insert(ei);
    }

    private static void handleMergeVertex(LinkNode<Point> v,
            SweepState sweepState, Event traceEvent, int dir) {

        // get e_{i-1}
        Segment eim = getEi(v.getValue(), true, dir);
        if (eim.helper.type == Point.Type.MERGE) {
            traceEvent.addDiagonal(new Diagonal(v.getValue(), eim.helper));
        }
        sweepState.delete(eim);

        Segment ej = sweepState.findFirstLeftOfEvent(sweepState.getRoot());
        if (ej.helper.type == Point.Type.MERGE) {
            traceEvent.addDiagonal(new Diagonal(v.getValue(), ej.helper));
        }
        ej.helper = v.getValue();
    }

    private static void handleRegularVertex(LinkNode<Point> v,
            SweepState sweepState, Event traceEvent, int dir) {

        if (v.getValue().type == Type.REGULARR) {

            // get e_{i-1}
            Segment eim = getEi(v.getValue(), true, dir);

            if (eim.helper.type == Point.Type.MERGE) {
                traceEvent.addDiagonal(new Diagonal(eim.helper, v.getValue()));
            }
            sweepState.delete(eim);

            // get e_{i}
            Segment ei = getEi(v.getValue(), false, dir);
            ei.helper = v.getValue();
            sweepState.insert(ei);

        } else if (v.getValue().type == Type.REGULARL) {

            Segment ej = sweepState.findFirstLeftOfEvent(sweepState.getRoot());
            if (ej.helper.type == Point.Type.MERGE) {
                traceEvent.addDiagonal(new Diagonal(v.getValue(), ej.helper));
            }
            ej.helper = v.getValue();

        } else {
            System.err
                    .println("handle unregular vertex in handleRegularVertex(),"
                            + "should not have happened");
        }
    }

}
