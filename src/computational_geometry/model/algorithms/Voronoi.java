package computational_geometry.model.algorithms;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import computational_geometry.model.beans.Line;
import computational_geometry.model.beans.Point;
import computational_geometry.model.core.Lines;
import computational_geometry.model.core.PointComparatorX;
import computational_geometry.model.data_structures.HalfEdge.Vert;
import computational_geometry.model.data_structures.VoronoiDiagram;
import computational_geometry.model.data_structures.VoronoiDiagram.VorCell;
import computational_geometry.model.data_structures.VoronoiDiagram.VorEdge;
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
            vor = new VoronoiDiagram();
            // TODO
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

        VorEdge e1 = vor.new VorEdge();
        VorEdge e2 = vor.new VorEdge();

        VorCell f1 = vor.new VorCell(p1, e1);
        VorCell f2 = vor.new VorCell(p2, e2);

        Vert v1 = vor.new Vert(bisector.findDownRightPoint(bound), e1);
        Vert v2 = vor.new Vert(bisector.findUpLeftPoint(bound), e2);

        e1.fill(v1, e2, f1, e1);
        e2.fill(v2, e1, f2, e2);

        vor.addEdge(e1);
        vor.addEdge(e2);

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

        if (inter == null) {
            // two bisectors,
            // assuming that point is sorted, it's bisector(p1, p2) and bisector(p2, p3)
            VorEdge e11 = vor.new VorEdge();
            VorEdge e12 = vor.new VorEdge();
            VorEdge e21 = vor.new VorEdge();
            VorEdge e22 = vor.new VorEdge();

            VorCell f1 = vor.new VorCell(p1, e11);
            VorCell f2 = vor.new VorCell(p2, e12);
            VorCell f3 = vor.new VorCell(p3, e22);

            Vert v1 = vor.new Vert(b1.findDownRightPoint(bound), e11);
            Vert v2 = vor.new Vert(b1.findUpLeftPoint(bound), e12);
            Vert v3 = vor.new Vert(b3.findDownRightPoint(bound), e21);
            Vert v4 = vor.new Vert(b3.findUpLeftPoint(bound), e22);

            e11.fill(v1, e12, f1, e11);
            e12.fill(v2, e11, f2, e12);
            e21.fill(v3, e22, f2, e21);
            e22.fill(v4, e21, f3, e22);

            vor.addEdge(e11);
            vor.addEdge(e12);
            vor.addEdge(e21);
            vor.addEdge(e22);

        } else {    // inter != null

            VorEdge e11 = vor.new VorEdge();
            VorEdge e12 = vor.new VorEdge();
            VorEdge e21 = vor.new VorEdge();
            VorEdge e22 = vor.new VorEdge();
            VorEdge e31 = vor.new VorEdge();
            VorEdge e32 = vor.new VorEdge();

            VorCell f1 = vor.new VorCell(p1, e11);
            VorCell f2 = vor.new VorCell(p2, e22);
            VorCell f3 = vor.new VorCell(p3, e31);

            // the three points bounding the bisectors in the rectangle
            Point q1, q2, q3;
            q1 = b1.findUpLeftPoint(bound);
            if (l1.findSide(p3) == l1.findSide(q1)) {
                q1 = b1.findDownRightPoint(bound);
            }
            q2 = b2.findUpLeftPoint(bound);
            if (l2.findSide(p2) == l2.findSide(q2)) {
                q2 = b2.findDownRightPoint(bound);
            }
            q3 = b3.findUpLeftPoint(bound);
            if (l3.findSide(p1) == l3.findSide(q3)) {
                q3 = b3.findDownRightPoint(bound);
            }

            Vert vInter = vor.new Vert(inter, e11);
            Vert v1 = vor.new Vert(q1, e11);
            Vert v2 = vor.new Vert(q2, e32);
            Vert v3 = vor.new Vert(q3, e22);

            e11.fill(v1, e21, f1, e12);
            e12.fill(vInter, e32, f1, e11);
            e21.fill(vInter, e11, f2, e22);
            e22.fill(v3, e31, f2, e21);
            e31.fill(vInter, e22, f3, e32);
            e32.fill(v2, e12, f3, e31);

            vor.addEdge(e11);
            vor.addEdge(e12);
            vor.addEdge(e21);
            vor.addEdge(e22);
            vor.addEdge(e31);
            vor.addEdge(e32);
        }

        return vor;
    }

}
