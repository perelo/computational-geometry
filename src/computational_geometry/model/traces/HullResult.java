package computational_geometry.model.traces;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Segment;
import computational_geometry.model.data_structures.CircularList;
import computational_geometry.model.data_structures.LinkNode;
import computational_geometry.views.SwingDrawer;

/**
 * Result of a convex hull computation algorithm
 * @author eloi
 *
 */
public class HullResult implements AlgoTrace {

    private List<Segment> segments; // TODO remove, but check TODO on polygonConvexHull b4
    private LinkNode<Point> hull;
    private Segment lowerTangent;
    private Segment upperTangent;

    private LinkNode<Point> lastLeftHull;
    private LinkNode<Point> lastRightHull;
    private List<Segment> lastTmpTangents;
    private int upperTanIndex;
    private int state;  // index in lastTmpTangents

    @Override
    public void drawFullResult(Graphics g, boolean colorize) {
        Color c = g.getColor();
        Drawer drawer = SwingDrawer.getInstance(g);
        if (segments.size() != 0) {
            if (colorize) {
                g.setColor(new Color((float)0, (float)1, (float)0, (float)0.4));
                List<Point> points = new ArrayList<Point>();
                for (Segment s : segments) {
                    points.add(s.u);
                }
                drawer.drawPolygon(points, true);
            } else {
                g.setColor(Color.GREEN);
                for (Segment s : segments) {
                    drawer.drawSegment(s);
                }
            }
        } else if (hull != null) {
            if (colorize) {
                List<Point> points = new ArrayList<Point>();
                LinkNode<Point> lnp;
                for (lnp = hull; !lnp.getNext().equals(hull) && lnp.getNext() != null; lnp = lnp.getNext()) {
                    points.add(lnp.getValue());
                }
                if (lnp != null) {
                    points.add(lnp.getValue());
                }
                g.setColor(new Color((float)0, (float)1, (float)0, (float)0.4));
                drawer.drawPolygon(points, true);
            } else {
                g.setColor(Color.GREEN);
                drawer.drawSegmentsBetweenPoints(hull);
            }
        }
        g.setColor(c);
    }

    @Override
    public void nextStep() {
        ++state;
    }

    @Override
    public boolean hasStep() {
        return state < lastTmpTangents.size() - 1;
    }

    @Override
    public boolean isDone() {
        return state >= lastTmpTangents.size();
    }

    @Override
    public void drawCurrentState(Graphics g, boolean colorize) {
        if (lastLeftHull == null || lastRightHull == null) {
            return;
        }
        Color c = g.getColor();
        g.setColor(Color.GREEN);
        Drawer drawer = SwingDrawer.getInstance(g);
        drawer.drawSegmentsBetweenPoints(lastLeftHull);
        drawer.drawSegmentsBetweenPoints(lastRightHull);
        g.setColor(Color.RED);
        Graphics2D g2 = (Graphics2D) g;
        Stroke s = g2.getStroke();
        g2.setStroke(new BasicStroke(2));
        if (state >= upperTanIndex) {
            drawer.drawSegment(upperTangent);
        }
        drawer.drawSegment(lastTmpTangents.get(state));
        g2.setStroke(s);
        g.setColor(c);
    }

    public HullResult() {
        this.segments = new ArrayList<Segment>();
        this.lastTmpTangents = new ArrayList<Segment>();
    }

    public static LinkNode<Point> copyRevertHull(LinkNode<Point> hull) {
        if (hull == null) {
            return null;
        }
        CircularList<Point> revertedHull = new CircularList<Point>();
        LinkNode<Point> ref = hull;
        do {
            revertedHull.addFirst(hull.getValue());
            hull = hull.getNext();
        } while (!hull.getValue().equals(ref.getValue()));
        return revertedHull.getNode(0);
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

    public Segment getLowerTangent() {
        return lowerTangent;
    }

    public void setLowerTangent(Segment lowerTangent) {
        this.lowerTangent = lowerTangent;
    }

    public Segment getUpperTangent() {
        return upperTangent;
    }

    public void setUpperTangent(Segment upperTangent) {
        this.upperTangent = upperTangent;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public LinkNode<Point> getLastLeftHull() {
        return lastLeftHull;
    }

    public void setLastLeftHull(LinkNode<Point> lastLeftHull) {
        this.lastLeftHull = lastLeftHull;
    }

    public LinkNode<Point> getLastRightHull() {
        return lastRightHull;
    }

    public void setLastRightHull(LinkNode<Point> lastRightHull) {
        this.lastRightHull = lastRightHull;
    }

    public List<Segment> getLastTmpTangents() {
        return lastTmpTangents;
    }

    public void setLastTmpTangents(List<Segment> lastTmpTangents) {
        this.lastTmpTangents = lastTmpTangents;
    }

    public int getUpperTanIndex() {
        return upperTanIndex;
    }

    public void setUpperTanIndex(int upperTanIndex) {
        this.upperTanIndex = upperTanIndex;
    }

}
