package computational_geometry.model.traces;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import computational_geometry.model.beans.Segment;
import computational_geometry.views.SwingDrawer;

/**
 * Result of a convex hull computation algorithm
 * @author eloi
 *
 */
public class HullResult extends SimpleAlgoResult {

    private List<Segment> segments;

    @Override
    public void drawFullResult(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.GREEN);
        Drawer drawer = SwingDrawer.getInstance(g);
        for (Segment s : segments) {
            drawer.drawSegment(s);
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

}
