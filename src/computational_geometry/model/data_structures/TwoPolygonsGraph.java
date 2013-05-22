package computational_geometry.model.data_structures;

import computational_geometry.model.beans.Graph;
import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Polygon;
import computational_geometry.model.core.Polygons;

/**
 * Two polygons, used for binary operations algorithms
 * points and segments of the graph are either thoses
 * of polygon1 or polygon2
 * @author eloi
 *
 */
public class TwoPolygonsGraph extends Graph {

    public enum Selected { FIRST, SECOND };

    private Polygon polygon1;
    private Polygon polygon2;

    public TwoPolygonsGraph(CircularList<Point> points) {
        throw new IllegalAccessError("This constructor cannot be used with TwoPolygonsGraph");
    }

    public TwoPolygonsGraph() {
        polygon1 = new Polygon();
        polygon2 = new Polygon();
        this.points = polygon1.getPoints();
    }

    public void setSelected(Selected selected) {
        switch (selected) {
        case FIRST : {
            this.setPoints(polygon1.getPoints());
            this.segments = polygon1.getSegments();
            break;
        }
        case SECOND : {
            setPoints(polygon2.getPoints());
            segments = polygon2.getSegments();
            break;
        }
        }
    }

    /**
     * Add segments from points and run all algorithms
     */
    @Override
    public void calculate() {
        segments.clear();
        Polygons.addSegmentsFromPoints(polygon1);
        Polygons.addSegmentsFromPoints(polygon2);
        super.calculate();
    }

    public Polygon getFirstPolygon() {
        return polygon1;
    }

    public Polygon getSecondPolygon() {
        return polygon2;
    }

}
