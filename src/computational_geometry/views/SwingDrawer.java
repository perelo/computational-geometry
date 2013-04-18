package computational_geometry.views;

import java.awt.Graphics;

import computational_geometry.model.beans.Line;
import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Segment;
import computational_geometry.model.traces.Drawer;

/**
 * Singleton drawer used to draw beans and data structures in a swing environment
 * the Graphics object is set when getting the instance or by setGraphics() method
 * note : The colors are NOT handled here
 * @author eloi
 *
 */
public class SwingDrawer implements Drawer {

    private static SwingDrawer instance;

    public static SwingDrawer getInstance(Graphics g) {
        if (instance == null) {
            return new SwingDrawer(g);
        } else {
            instance.setGraphics(g);
        }
        return instance;
    }

    private Graphics g;

    private SwingDrawer(Graphics g) {
        this.g = g;
    }

    public void setGraphics(Graphics g) {
        this.g = g;
    }

    /**
     * Draw a given point
     * @param g
     * @param p
     */
    public void drawPoint(Point p) {
        if (p != null) {
            g.fillOval((int)p.x - Point.range, (int)p.y - Point.range,
                    2 * Point.range + 1, 2 * Point.range + 1);
            g.drawOval((int)p.x - 2 * Point.range, (int)p.y - 2 * Point.range,
                    2 * 2 * Point.range + 1, 2 * 2 * Point.range + 1);
        }
    }

    /**
     * Draw mark of a point,
     * position of writing depends on the mark
     * @param p
     */
    public void drawMark(Point p) {
        if (p.side == Point.Side.LEFT) {
            g.drawString(p.pos + " G", (int)p.x - 30, (int)p.y);
        } else if (p.side == Point.Side.RIGHT) {
            g.drawString("D " + p.pos, (int)p.x + 15, (int)p.y);
        } else {
            g.drawString(p.pos + "", (int)p.x + 15, (int)p.y);
        }
    }

    /**
     * Draw type of a point,
     * position of writing depends on the type
     * @param p
     */
    public void drawType(Point p) {
        if (p.type == Point.Type.START || p.type == Point.Type.SPLIT) {
            g.drawString(p.type.toString(), (int)p.x, (int)p.y - 20);
        } else if (p.type == Point.Type.END || p.type == Point.Type.MERGE) {
            g.drawString(p.type.toString(), (int)p.x, (int)p.y + 20);
        } else if (p.type == Point.Type.REGULARR) {
            g.drawString(p.type.toString(), (int)p.x - 80, (int)p.y);
        } else if (p.type == Point.Type.REGULARL) {
            g.drawString(p.type.toString(), (int)p.x + 10, (int)p.y);
        }
        // if unknown, leave it
    }

    /**
     * Draw coordinates of a point under it
     * @param p
     */
    public void drawCoordinates(Point p) {
        g.drawString("(" + (int)p.x + ", " + (int)p.y + ')', (int)p.x - 25, (int)p.y + 20);
    }

    /**
     * Draw a straight line l apparently infinite
     * @param g
     * @param l
     */
    public void drawStraightLine(Line l) {
        double x1, x2, y1, y2;
        // find two points passing through l
        if (Math.abs(l.slope()) > 1) {
            y1 = 0;
            x1 = l.findX(y1);
            y2 = g.getClipBounds().height;
            x2 = l.findX(y2);
        } else {
            x1 = 0;
            y1 = l.findY(x1);
            x2 = g.getClipBounds().width;
            y2 = l.findY(x2);
        }
        g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
    }

    /**
     * Draw a simple segment if none of it's point are "infinite",
     * else draw the (half-)line corresponding
     * TODO : I think some of the code is is Lines static methods, find and destroy !
     * @param g
     * @param s
     */
    public void drawSegment(Segment s) {
        if (s == null) {
            throw new IllegalArgumentException("segment is null");
        }
        g.drawLine((int)s.u.x, (int)s.u.y, (int)s.v.x, (int)s.v.y);
//        if (!s.u.isInfinite() && !s.v.isInfinite()) {
//            g.drawLine(s.u.x, s.u.y, s.v.x, s.v.y);
//            return;
//        }
//        double x1, x2, y1, y2, a;
//        x1 = s.u.x;
//        x2 = s.v.x;
//        y1 = s.u.y;
//        y2 = s.v.y;
//        if (s.u.x == s.v.x) { // a would be infinite
//            if (s.u.isInfinite()) {
//                y1 = s.v.y < s.u.y ? g.getClipBounds().height : 0;
//            }
//            if (s.v.isInfinite()) {
//                y2 = s.u.y < s.v.y ? g.getClipBounds().height : 0;
//            }
//        } else {
//            a = ((double) (s.v.y - s.u.y) / (s.v.x - s.u.x));
//            // adapt dimension reference so we don't draw to infinite
//            if (Math.abs(a) > 1) {
//                if (s.u.isInfinite()) {
//                    y1 = s.v.y < s.u.y ? g.getClipBounds().height : 0;
//                    x1 = (y1 - s.u.y + s.u.x * a) / a;
//                }
//                if (s.v.isInfinite()) {
//                    y2 = s.u.y < s.v.y ? g.getClipBounds().height : 0;
//                    x2 = (y2 - s.u.y + s.u.x * a) / a;
//                }
//            } else {
//                if (s.u.isInfinite()) {
//                    x1 = s.v.x < s.u.x ? g.getClipBounds().width : 0;
//                    y1 = a * (x1 - s.u.x) + s.u.y;
//                }
//                if (s.v.isInfinite()) {
//                    x2 = s.u.x < s.v.x ? g.getClipBounds().width : 0;
//                    y2 = a * (x2 - s.u.x) + s.u.y;
//                }
//            }
//        }
//        g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
    }

}
