package computational_geometry.model.traces;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Segment;
import computational_geometry.views.SwingDrawer;

/**
 * Result of a convex hull computation algorithm
 * @author eloi
 *
 */
public class HullResult extends SimpleAlgoResult {

    private List<Segment> segments; // TODO remove, but check TODO on polygonConvexHull b4
    private List<Point> points;

    @Override
    public void drawFullResult(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.GREEN);
        Drawer drawer = SwingDrawer.getInstance(g);
        if (segments != null) {
            for (Segment s : segments) {
                drawer.drawSegment(s);
            }
        }
        else if (points != null) {
            for (int i = 1; i < points.size(); ++i) {
                drawer.drawSegment(new Segment(points.get(i-1), points.get(i)));
            }
        }
        g.setColor(c);
    }

    public HullResult() {
        this.segments = new ArrayList<Segment>();
        this.points = new ArrayList<Point>();
    }

    public void addSegment(Segment s) {
        segments.add(s);
    }

    public void removeSegment(Segment s) {
        segments.remove(s);
    }

    public List<Segment> getSegments() {
        return segments;
    }
    
    public void addPoint(Point p) {
        points.add(p);
    }
    
    public void removePoint(Point p) {
        points.remove(p);
    }
    
    public List<Point> getPoints() {
        return points;
    }

}
