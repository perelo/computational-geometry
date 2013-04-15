package computational_geometry.model.beans;

import java.util.ArrayList;
import java.util.Collection;

import computational_geometry.model.beans.Point.Side;
import computational_geometry.model.core.Utils;
import computational_geometry.model.data_structures.CircularList;

/**
 * Class graph, cannot be instanciated directly
 * Contains a list of points, a list of segments
 * and a collection of algorithms to run when "calculating" this Graph
 * @author eloi
 *
 */
public abstract class Graph {

    protected CircularList<Point> points;
    protected CircularList<Segment> segments;

    protected Collection<GraphAlgorithm> algorithms;

    public Graph(CircularList<Point> points) {
        this.points = points;
        this.segments = new CircularList<Segment>();
        this.algorithms = new ArrayList<GraphAlgorithm>();
    }

    public Graph() {
        this(new CircularList<Point>());
    }

    public void clearMarks() {
        for (Point p : points) {
            p.side = Side.UNKNOWN;
        }
    }

    /**
     * Add a random scatterplot to this Graph
     * @param n
     * @param minX
     * @param maxX
     * @param minY
     * @param maxY
     */
    public void generateNewPoints(int n, int minX, int maxX, int minY, int maxY) {
        for (int i = 0; i < n; ++i) {
            points.add(new Point(minX + Utils.rand(maxX - minX),
                                 minY + Utils.rand(maxY - minY)));
        }
    }

    /**
     * Clear segments and points of this Graphs
     */
    public void clear() {
        segments.clear();
        points.clear();
        clearMarks();
    }

    public void addPoint(Point p) {
        points.add(p);
    }

    public void removePoint(Point p) {
        points.remove(p);
    }

    public int findFirstPointInRange(Point p) {
        int i = 0;
        // not rly good cuz we are not supposed to know how iterator works
        for (Point p1 : getPoints()) {
            if (p.isInRange(p1)) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    public void addSegment(Segment s) {
        segments.add(s);
    }

    public void removeSegment(Segment s) {
        segments.remove(s);
    }

    public Collection<GraphAlgorithm> getAlgorithms() {
        return algorithms;
    }

    public int getNbPoints() {
        return points.size();
    }

    public int getNbSegments() {
        return segments.size();
    }

    public CircularList<Point> getPoints() {
        return this.points;
    }

    public void setPoints(CircularList<Point> points) {
        this.points = points;
    }

    public CircularList<Segment> getSegments() {
        return this.segments;
    }

    public void addAlgorithm(GraphAlgorithm algo) {
        algorithms.add(algo);
    }

    public void removeAlgorithm(GraphAlgorithm algo) {
        algorithms.remove(algo);
    }

    /**
     * Calculate this Graph : run each algorithms
     */
    public void calculate() {
        for (GraphAlgorithm algo : algorithms) {
            algo.run(this);
        }
    }

	public void clearAlgorithms() {
		algorithms.clear();
	}

}
