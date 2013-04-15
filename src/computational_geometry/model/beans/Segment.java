package computational_geometry.model.beans;

/**
 * Class Segment used in *all* algorithms 
 * TODO : separate fields according to which are needed in which algorithm
 * @author eloi
 *
 */
public class Segment implements Comparable<Segment> {

    public enum WayMark {
        UV, VU
    }

    public Point u;
    public Point v;
    public Point helper;

    private int posInUSortedList;
    private int posInVSortedList;

    /* if true, segment uv (or vu) have been incorporated in a monotonous piece
     * while getting them to triangulate simple polygon */
    private boolean flagUV;
    private boolean flagVU;

    public static int yComp = -1;
    
    public Segment() {
    }

    public Segment(Point u, Point v) {
        this.u = u;
        this.v = v;
        this.helper = null;
        this.u.atBegin = this;
        this.v.atEnd = this;
        this.flagUV = false;
        this.flagUV = false;
    }

    @Override
    public String toString() {
        return "s(" + u + ", " + v + ")";
    }

    public boolean isAtLeftOf(Point p) {
        return getXAtStaticY() < p.x;
    }

    public boolean equals(Segment s) {
        if ((u.equals(s.u) && v.equals(s.v)) ||
            (u.equals(s.v) && v.equals(s.u))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Segment s) {
        if ((yComp < u.y && yComp < v.y) ||
            (yComp > s.u.y && yComp > s.v.y))
            return -1;  // should not happen in our algorithms
        return getXAtStaticY().compareTo(s.getXAtStaticY());
    }

    /**
     *
     * @return the x coordinate of the point of intersection between
     * the segment and line y = yComp
     */
    private Double getXAtStaticY() {
        if (u.x == v.x)
            return Double.valueOf(u.x);
        else if (u.y == v.y) {
            return Double.valueOf(Math.abs(u.x - v.x));
        }

        // a*x + b = y
        double a, b;
        a = (double)(u.y - v.y) / (u.x - v.x);
        b = u.y - (a * u.x);
        return (yComp - b) / a;
    }

	public int getPosInUSortedList() {
        return posInUSortedList;
    }

    public void setPosInUSortedList(int posInUSortedList) {
        this.posInUSortedList = posInUSortedList;
    }

    public int getPosInVSortedList() {
        return posInVSortedList;
    }

    public void setPosInVSortedList(int posInVSortedList) {
        this.posInVSortedList = posInVSortedList;
    }

    public boolean isFlagUV() {
        return flagUV;
    }

    public void setFlagUV(boolean flagUV) {
        this.flagUV = flagUV;
    }

    public boolean isFlagVU() {
        return flagVU;
    }

    public void setFlagVU(boolean flagVU) {
        this.flagVU = flagVU;
    }

}
