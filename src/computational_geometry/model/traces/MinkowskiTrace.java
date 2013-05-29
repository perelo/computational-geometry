package computational_geometry.model.traces;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import computational_geometry.model.beans.Polygon;
import computational_geometry.views.SwingDrawer;

public class MinkowskiTrace extends SimpleAlgoResult {

    private Polygon polygon;

    @Override
    public void drawFullResult(Graphics g, boolean colorize) {
        Drawer d = SwingDrawer.getInstance(g);
        Color c = g.getColor();
        if (colorize) {
            g.setColor(Color.ORANGE);
            d.drawPolygon(polygon.getPoints(), true);
        } else {
            Graphics2D g2 = (Graphics2D) g;
            Stroke s = g2.getStroke();
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            d.drawPolygon(polygon.getPoints(), false);
            g2.setStroke(s);
        }
        g.setColor(c);
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

}
