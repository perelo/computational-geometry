package computational_geometry.model.traces;

import computational_geometry.model.beans.Line;
import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Segment;

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

}
