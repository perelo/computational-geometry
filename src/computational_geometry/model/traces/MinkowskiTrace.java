package computational_geometry.model.traces;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Polygon;
import computational_geometry.model.data_structures.CircularList;
import computational_geometry.views.SwingDrawer;

public class MinkowskiTrace implements AlgoTrace {

    private Polygon polygon;

    private List<Entry<Point, Point>> ptsCouplesList;
    private int stateCouples, stateResult;

    public MinkowskiTrace() {
        ptsCouplesList = new ArrayList<Entry<Point, Point>>();
        stateCouples = stateResult = 0;
    }

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

    public void addCouple(Point p, Point q) {
        ptsCouplesList.add(new AbstractMap.SimpleEntry<Point, Point>(p, q));
    }

    public void removeCouple(Point p, Point q) {
        ptsCouplesList.remove(new AbstractMap.SimpleEntry<Point, Point>(p, q));
    }

    @Override
    public void nextStep() {
        ++stateCouples;
        ++stateResult;
    }

    @Override
    public boolean hasStep() {
        return stateCouples < ptsCouplesList.size();
    }

    @Override
    public boolean isDone() {
        return stateCouples >= ptsCouplesList.size();
    }

    @Override
    public void drawCurrentState(Graphics g, boolean colorize) {
        Drawer d = SwingDrawer.getInstance(g);
        Color c = g.getColor();
        g.setColor(Color.BLACK);
        CircularList<Point> points = polygon.getPoints();
        for (int n = 0; n < stateResult; n++) {
            d.drawSegment(points.elementAt(n), points.elementAt(n + 1));
        }
        g.setColor(Color.ORANGE);
        d.drawPoint(ptsCouplesList.get(stateCouples).getKey());
        d.drawPoint(ptsCouplesList.get(stateCouples).getValue());
        g.setColor(c);
    }

}
