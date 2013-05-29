package computational_geometry.model.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import computational_geometry.model.core.CrossProdComparator;
import computational_geometry.model.data_structures.CircularList;

/**
 * Class Point used in *all* algorithms 
 * TODO : separate fields according to which are needed in which algorithm
 * @author eloi
 *
 */
public class Point implements Comparable<Point> {

    // Marque d'appartenance a la chaine droite ou gauche
    public enum Side {
        RIGHT, LEFT, UNKNOWN
    }

    public enum Type {
        START, END, MERGE, SPLIT, REGULARR, REGULARL, UNKNOWN
    }

    public double x;
    public double y;
    public Side side;
    public Type type;
    public int pos;

    public Segment atBegin; // needed for easy monotonization (e_{i-1})
    public Segment atEnd;
    private CircularList<Segment> diagonals;
    private List<Segment> segments;
    private boolean isSegmentsSorted;

    private boolean isInfinite; // if this points represents infinite

    public final static int range = 2;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        this.side = Side.UNKNOWN;
        this.type = Type.UNKNOWN;
        this.pos = -1;
        this.diagonals = new CircularList<Segment>();
        this.segments = new ArrayList<Segment>();
        this.isSegmentsSorted = false;
        this.isInfinite = false;
    }

    public void addDiagonal(Diagonal diagonal) {
        diagonals.add(diagonal);
    }

    public void removeDiagonal(Diagonal diagonal) {
        this.diagonals.remove(diagonal);
    }

    public CircularList<Segment> getDiagonals() {
        return this.diagonals;
    }

    @Override
    public boolean equals(Object obj) {
        Point p = (Point) obj;
        return p.x == x && p.y == y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + type + ")";
    }

    @Override
    public int compareTo(Point p) {
        if (y < p.y)
            return -1;
        else if (y == p.y) {
            if (x < p.x)
                return -1;
            else if (x == p.x)
                return 0;
            else
                return 1;
        } else
            return 1;
    }

    public boolean isInRange(Point p) {
        return x > p.x - 2 * range && x < p.x + 2 * range
                && y > p.y - 2 * range && y < p.y + 2 * range;
    }

    public void clearSegments() {
        segments.clear();
        isSegmentsSorted = false;
    }

    public List<Segment> getIncidentSegments() {
        List<Segment> segs = new ArrayList<Segment>();

        segs.add(atBegin);
        segs.add(atEnd);
        for (Segment s : diagonals) {
            segs.add(s);
        }

        return segs;
    }

    public List<Segment> getOrderedSegments() {
        if (!isSegmentsSorted) {
            segments = getIncidentSegments();
            Collections.sort(segments, new CrossProdComparator(this));
            isSegmentsSorted = true;
        }
        return segments;
    }

    public void markSegmentsInSortedList() {
        List<Segment> segs = getIncidentSegments();
        Collections.sort(segs, new CrossProdComparator(this));

        for (int i = 0; i < segs.size(); ++i) {
            if (this.equals(segs.get(i).u)) {
                segs.get(i).setPosInUSortedList(i);
            } else if (this.equals(segs.get(i).v)) {
                segs.get(i).setPosInVSortedList(i);
            } else {
                System.err.println("incidents segments are not incident...");
            }
        }
    }

    public Segment getNextSegInSortedList(int posInSortedList) {
        return getOrderedSegments().get(
                (posInSortedList + 1) % getOrderedSegments().size());
    }

    public Segment getPrevSegInSortedList(int posInSortedList) {
        int i = posInSortedList;
        if (posInSortedList <= 0) {
            i += (getOrderedSegments().size() * (Math.abs(posInSortedList) / getOrderedSegments().size() +1));
        }
        return getOrderedSegments().get(i-1);
    }

    public boolean isInfinite() {
        return isInfinite;
    }

    public void setInfinite(boolean isInfinite) {
        this.isInfinite = isInfinite;
    }

    public Point sum(Point p) {
        return new Point(this.x + p.x, this.y + p.y);
    }

}
