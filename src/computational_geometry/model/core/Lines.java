package computational_geometry.model.core;

import computational_geometry.model.beans.Line;
import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Segment;

/**
 * Methods to manipulate lines
 * Inspired by http://wcipeg.com/wiki/Computational_geometry
 * @author eloi
 *
 */
public class Lines {

	/**
	 * Find a line given two points passing through this line
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static Line findLine(Point p1, Point p2) {
		double A, B, C;
		A = p1.y - p2.y;
		B = p2.x - p1.x;
		C = -(A * p1.x) - (B * p1.y);
		return new Line(A, B, C);
	}
	
	/**
	 * Find the perpendicular bisector of a given segment (bounds)
	 * @param p1 : the first bound of the segment
	 * @param p2 : the second bound of the segment
	 * @return
	 */
	public static Line findBisector(Point p1, Point p2) {
		double A, B, C, x, y;
		A = p2.x - p1.x;
		B = p2.y - p1.y;
		x = (p2.x + p1.x) / 2;
		y = (p2.y + p1.y) / 2;
		C = -(A * x) - (B * y);
		return new Line(A, B, C);
	}
	
	/**
	 * Find the intersection point of a line and a segment
	 * @param l
	 * @param s
	 * @return The intersection of l and s if exists, null otherwise
	 */
	public static Point findIntersection(Line l, Segment s) {
		Point p = l.findIntersection(findLine(s.u, s.v));
		return isInRectangle(p, s) ? p : null;
	}
	
	/**
	 * If a point is in the rectangle represented by its diagonal (segment)
	 * @param p
	 * @param s
	 * @return
	 */
	public static boolean isInRectangle(Point p, Segment s) {
		if (p == null || s == null) return false;
		return ((s.u.x < s.v.x && p.x >= s.u.x && p.x <= s.v.x)  ||
			    (s.u.x > s.v.x && p.x <= s.u.x && p.x >= s.v.x)) &&
			   ((s.u.x < s.v.x && p.x >= s.u.x && p.x <= s.v.x)  ||
				(s.u.x > s.v.x && p.x <= s.u.x && p.x >= s.v.x));
	}
	
}
