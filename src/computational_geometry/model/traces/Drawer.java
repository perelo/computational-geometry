package computational_geometry.model.traces;

import java.util.List;

import computational_geometry.model.beans.Line;
import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Segment;
import computational_geometry.model.data_structures.LinkNode;

/**
 * Interface Drawer used to draw beans and data structures
 * @author eloi
 *
 */
public interface Drawer {

    /**
     * Draw a given point
     * @param g
     * @param p
     */
    public void drawPoint(Point p);

    /**
     * Draw mark of a point,
     * position of writing depends on the mark
     * @param p
     */
    public void drawMark(Point p);

    /**
     * Draw type of a point,
     * position of writing depends on the type
     * @param p
     */
    public void drawType(Point p);

    /**
     * Draw coordinates of a point under it
     * @param p
     */
    public void drawCoordinates(Point p);

    /**
     * Draw a straight line l apparently infinite
     * @param g
     * @param l
     */
    public void drawStraightLine(Line l);

    /**
     * Draw a simple segment if none of it's point are "infinite",
     * else draw the (half-)line corresponding
     * @param g
     * @param s
     */
    public void drawSegment(Segment s);

    /**
     * Draw a simple line between the two given points
     * @param u
     * @param v
     */
    public void drawSegment(Point u, Point v);

    /**
     * Draw the segments between the points given in
     * a node representing a circular list
     * @param nodePoint
     */
    public void drawSegmentsBetweenPoints(LinkNode<Point> nodePoint);

    /**
     * Draw a polygon given a list of points
     * if fill is set, the polygon will be filled with a color
     * @param points
     * @param fill
     */
    void drawPolygon(List<Point> points, boolean fill);

}
