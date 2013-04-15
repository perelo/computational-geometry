package computational_geometry.model.beans;

import computational_geometry.model.core.Polygons;
import computational_geometry.model.data_structures.CircularList;

/**
 * Polygon
 * @author eloi
 *
 */
public class Polygon extends Graph {

    public Polygon() {
        super();
    }

    public Polygon(CircularList<Point> points) {
        super(points);
    }

    /**
     * Add segments from points and run all algorithms
     */
    @Override
    public void calculate() {
        segments.clear();
        Polygons.addSegmentsFromPoints(this);
        super.calculate();
    }

    @Override
    public void clear() {
        super.clear();
        algorithms.clear();
    }

}
