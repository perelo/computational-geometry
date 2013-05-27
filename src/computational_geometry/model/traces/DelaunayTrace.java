package computational_geometry.model.traces;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import computational_geometry.model.beans.Segment;
import computational_geometry.views.SwingDrawer;

public class DelaunayTrace extends SimpleAlgoResult {

    private List<Segment> segments;

    public DelaunayTrace(List<Segment> segments) {
        this.segments = segments;
    }

    @Override
    public void drawFullResult(Graphics g, boolean colorize) {
        if (segments == null) {
            return;
        }
        Color c = g.getColor();
        g.setColor(Color.MAGENTA);
        Drawer d = SwingDrawer.getInstance(g);
        for (Segment s : segments) {
            d.drawSegment(s);
        }
        g.setColor(c);
    }

}
