package computational_geometry.model.algorithms;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import computational_geometry.model.beans.Line;
import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Segment;
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
		}
		else if (points.size() == 3) {
			vor = handle3PointsVor(points);
		}
		else {
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

		Point q1, q2;
		q1 = bisector.findUpLeftPoint(bound);
		q2 = bisector.findDownRightPoint(bound);
		e1.fill(new Segment(q2, q1), null, e2, f1, null);
		e2.fill(new Segment(q1, q2), null, e1, f2, null);

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
			
			e11.fill(new Segment(b1.findUpLeftPoint(bound), b1.findDownRightPoint(bound)), null, e12, f1, null);
			e12.fill(new Segment(b1.findUpLeftPoint(bound), b1.findDownRightPoint(bound)), null, e11, f2, null);
			e21.fill(new Segment(b3.findUpLeftPoint(bound), b3.findDownRightPoint(bound)), null, e22, f2, null);
			e22.fill(new Segment(b3.findUpLeftPoint(bound), b3.findDownRightPoint(bound)), null, e21, f3, null);
			
			vor.addEdge(e11);
			vor.addEdge(e12);
			vor.addEdge(e21);
			vor.addEdge(e22);
			
			return vor;
		}

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

		VorEdge e11 = vor.new VorEdge();
		VorEdge e12 = vor.new VorEdge();
		VorEdge e21 = vor.new VorEdge();
		VorEdge e22 = vor.new VorEdge();
		VorEdge e31 = vor.new VorEdge();
		VorEdge e32 = vor.new VorEdge();

		VorCell f1 = vor.new VorCell(p1, e11);
		VorCell f2 = vor.new VorCell(p2, e21);
		VorCell f3 = vor.new VorCell(p3, e31);

		Vert v = vor.new Vert(inter, e11);

		e11.fill(new Segment(q1, inter), null, e21, f1, e12);
		e12.fill(new Segment(inter, q3), v, e32, f1, null);
		e21.fill(new Segment(inter, q1), v, e11, f2, null);
		e22.fill(new Segment(q2, inter), null, e31, f2, e21);
		e31.fill(new Segment(inter, q2), v, e22, f3, null);
		e32.fill(new Segment(q3, inter), null, e12, f3, e31);

		vor.addEdge(e11);
		vor.addEdge(e12);
		vor.addEdge(e21);
		vor.addEdge(e22);
		vor.addEdge(e31);
		vor.addEdge(e32);
		
		return vor;
	}

}
