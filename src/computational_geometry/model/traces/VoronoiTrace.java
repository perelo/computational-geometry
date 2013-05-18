package computational_geometry.model.traces;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Segment;
import computational_geometry.model.data_structures.DCEL.Edge;
import computational_geometry.model.data_structures.DCEL.Face;
import computational_geometry.model.data_structures.VoronoiDiagram;
import computational_geometry.views.SwingDrawer;

/**
 * Trace of the voronoi diagram computation algorithm using the divide and conquer paradigm
 * @author eloi
 *
 */
public class VoronoiTrace implements AlgoTrace {

    // full voronoi diagram
    public VoronoiDiagram vor;

    public HullResult hull;

    // last step's elements
    public List<Segment> vorLSegs;
    public List<Segment> vorRSegs;
    public List<Point> zipLine;
    private int state;

    public VoronoiTrace() {
        this.state = 1;
    }

    @Override
    public void drawFullResult(Graphics g, boolean colorize) {
        drawVoronoiDiagram(vor, g, colorize);
    }

    @Override
    public void nextStep() {
        ++state;
    }

    @Override
    public boolean hasStep() {
        return zipLine != null || state < zipLine.size();
    }

    @Override
    public boolean isDone() {
        return zipLine == null || state > zipLine.size();
    }

    @Override
    public void drawCurrentState(Graphics g, boolean colorize) {
        Drawer d = SwingDrawer.getInstance(g);
        if (zipLine == null) {
            drawVoronoiDiagram(vor, g, colorize);
        } else {
            Graphics2D g2 = (Graphics2D) g;
            Stroke s = g2.getStroke();
            Color c = g.getColor();
            g.setColor(Color.RED);
            for (Segment seg : vorLSegs) {
                d.drawSegment(seg);
            }
            g.setColor(Color.BLUE);
            for (Segment seg : vorRSegs) {
                d.drawSegment(seg);
            }
            g.setColor(Color.BLACK);
            for (int i = 0; i < state-1; ++i) {
                d.drawSegment(zipLine.get(i), zipLine.get(i+1));
            }
            g.setColor(Color.CYAN);
            g2.setStroke(new BasicStroke(2));
            d.drawSegment(hull.getLowerTangent());
            d.drawSegment(hull.getUpperTangent());
            g2.setStroke(s);
            g.setColor(c);
        }
    }

    private void drawVoronoiDiagram(VoronoiDiagram vor, Graphics g, boolean colorize) {
        if (colorize) { // draw by faces
            Random r = new Random();
            for (Iterator<Face> it = vor.getFaceIterator(); it.hasNext(); ) {
                Face f = it.next();
                Edge e = f.getEdge();
                List<Point> points = new ArrayList<Point>();
                do {
                    points.add(e.getOrigin().getPoint());
                    if (!e.getTwin().getOrigin().equals(e.getNext().getOrigin())) {
                        points.add(e.getTwin().getOrigin().getPoint());
                    }
                    e = e.getNext();
                } while (!e.equals(f.getEdge()));
                int[] xPoints = new int[points.size()];
                int[] yPoints = new int[points.size()];
                for (int i = 0; i < points.size(); ++i) {
                    Point p = points.get(i);
                    xPoints[i] = (int) p.x;
                    yPoints[i] = (int) p.y;
                }
                g.setColor(new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), (float)0.5));
                g.fillPolygon(xPoints, yPoints, points.size());
            }
        } else {    // draw by edges
            Iterator<Edge> it = vor.getEdgeIterator();
            Drawer d = SwingDrawer.getInstance(g);
            while (it.hasNext()) {
                Edge e = it.next();
                if (e.getTwin().isDrawn == false) {
                    d.drawSegment(new Segment(e.getOrigin().getPoint(), e.getTwin().getOrigin().getPoint()));
                    e.isDrawn = true;
                }
            }
        }
    }

}
