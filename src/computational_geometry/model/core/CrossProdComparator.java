package computational_geometry.model.core;

import java.util.Comparator;

import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Segment;

/**
 * Comparator of Segments using cross product
 * At least one of the segment's bound must coincide with one of the other,
 * this is the origin
 * @author eloi
 *
 */
public class CrossProdComparator implements Comparator<Segment> {

    private Point origin;

    public CrossProdComparator(Point origin) {
        this.origin = origin;
    }

    @Override
    public int compare(Segment s1, Segment s2) {
        Point a, b;

        if      (s1.u.equals(origin)) a = s1.v;
        else if (s1.v.equals(origin)) a = s1.u;
        else { System.err.println("not from origin"); return 0; }

        if      (s2.u.equals(origin)) b = s2.v;
        else if (s2.v.equals(origin)) b = s2.u;
        else { System.err.println("not from origin"); return 0; }

        if      (a.y > origin.y && b.y < origin.y) return 1;
        else if (a.y < origin.y && b.y > origin.y) return -1;
        else return (int) Utils.crossProduct(a, origin, b);
    }

}
