package computational_geometry.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.List;

import computational_geometry.model.beans.Point;
import computational_geometry.model.data_structures.CircularList;
import computational_geometry.model.traces.AlgoTrace;
import computational_geometry.model.traces.SimpleAlgoResult;
import computational_geometry.views.SwingDrawer;

public class PolygonIntersectionTrace extends SimpleAlgoResult implements AlgoTrace {

    private List<Point> interPolygon;

    public PolygonIntersectionTrace() {
        this.interPolygon = new CircularList<Point>();
    }

    @Override
    public void drawFullResult(Graphics g, boolean colorize) {
        if (interPolygon.size() < 3) {
            return;
        }
        SwingDrawer d = SwingDrawer.getInstance(g);
        Graphics2D g2 = (Graphics2D) g;
        Stroke s = g2.getStroke();
        Color c = g.getColor();
        if (colorize) {
            g.setColor(d.getRandomColor((float)0.5));
        } else {
            g2.setStroke(new BasicStroke(2));
            g.setColor(Color.RED);
        }
        SwingDrawer.getInstance(g).drawPolygon(interPolygon, colorize);
        g2.setStroke(s);
        g.setColor(c);
    }

    public boolean addPoint(Point p) {
        return interPolygon.add(p);
    }

    public boolean removePoint(Point p) {
        return interPolygon.remove(p);
    }

}
