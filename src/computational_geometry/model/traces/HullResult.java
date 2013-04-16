package computational_geometry.model.traces;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Segment;
import computational_geometry.model.data_structures.LinkNode;
import computational_geometry.views.SwingDrawer;

/**
 * Result of a convex hull computation algorithm
 * @author eloi
 *
 */
public class HullResult extends SimpleAlgoResult {

    private List<Segment> segments; // TODO remove, but check TODO on polygonConvexHull b4
    private LinkNode<Point> hull;

    @Override
    public void drawFullResult(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.GREEN);
        Drawer drawer = SwingDrawer.getInstance(g);
        if (segments.size() != 0) {
            for (Segment s : segments) {
                drawer.drawSegment(s);
            }
        }
        else if (hull != null) {
            LinkNode<Point> node = hull;
            do {
                drawer.drawSegment(new Segment(node.getValue(), node.getNext().getValue()));
                node = node.getNext();
            } while (!node.getValue().equals(hull.getValue()));
        }
        g.setColor(c);
    }

    public HullResult() {
        this.segments = new ArrayList<Segment>();
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

    public LinkNode<Point> getHull() {
        return hull;
    }

    public void setHull(LinkNode<Point> hull) {
        this.hull = hull;
    }

}
