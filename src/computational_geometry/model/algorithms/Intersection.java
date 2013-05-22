package computational_geometry.model.algorithms;

import computational_geometry.model.PolygonIntersectionTrace;
import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Polygon;
import computational_geometry.model.beans.Segment;
import computational_geometry.model.core.Lines;
import computational_geometry.model.core.Polygons;
import computational_geometry.model.core.Utils;
import computational_geometry.model.data_structures.LinkNode;
import computational_geometry.model.traces.AlgoTrace;

public class Intersection {

    private enum Inside { FIRST, SECOND, UNKNOWN };

    public static AlgoTrace IntersectConvexPolygons(Polygon firstPolygon, Polygon secondPolygon) {
        if (!Polygons.isConvex(firstPolygon) || !Polygons.isConvex(secondPolygon)) {
            return null;
        }

        PolygonIntersectionTrace res = new PolygonIntersectionTrace();
        LinkNode<Segment> segA, segB;
        int advanceA = 0, advanceB = 0;
        double AcrossB;     // segA x segB
        int aInHB, bInHA;   // < 0 if true
        Point inter;
        Inside flag = Inside.UNKNOWN;

        segA = firstPolygon.getSegments().getNode(0);
        segB = secondPolygon.getSegments().getNode(0);
        do {
            bInHA = Utils.orientation(segA.getValue().u, segA.getValue().v, segB.getValue().v);
            aInHB = Utils.orientation(segB.getValue().u, segB.getValue().v, segA.getValue().v);

            AcrossB = Utils.crossProduct(segA.getValue(), segB.getValue());

            inter = Lines.findIntersection(segA.getValue(), segB.getValue());
            if (inter != null) {
                if (flag == Inside.UNKNOWN) {
                    advanceA = advanceB = 0;
                }
                flag = updateFlag(flag, aInHB, bInHA);
                res.addPoint(inter);
            }
            if (AcrossB >= 0) {
                if (bInHA <= 0) {
                    if (flag == Inside.FIRST) {
                        res.addPoint(segA.getValue().v);
                    }
                    ++advanceA;
                    segA = segA.getNext();
                } else {
                    if (flag == Inside.SECOND) {
                        res.addPoint(segB.getValue().v);
                    }
                    ++advanceB;
                    segB = segB.getNext();
                }
            } else {    // AcrossB < 0
                if (aInHB <= 0) {
                    if (flag == Inside.SECOND) {
                        res.addPoint(segB.getValue().v);
                    }
                    ++advanceB;
                    segB = segB.getNext();
                } else {
                    if (flag == Inside.FIRST) {
                        res.addPoint(segA.getValue().v);
                    }
                    ++advanceA;
                    segA = segA.getNext();
                }
            }
        } while ((advanceA < firstPolygon.getNbPoints() || advanceB < secondPolygon.getNbPoints()) &&
                 (advanceA < 2*firstPolygon.getNbPoints()) && advanceB < 2*secondPolygon.getNbPoints());

        return res;
    }

    private static Inside updateFlag(Inside flag, double aInHB, double bInHA) {
        if (aInHB < 0) {
            return Inside.FIRST;
        } else if (bInHA < 0) {
            return Inside.SECOND;
        } else {
            return flag;
        }
    }

}
