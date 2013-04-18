package computational_geometry.model.traces;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import computational_geometry.model.beans.Polygon;

/**
 * Trace for the algorithm that compute a triangulation of a simple polygon
 * @author eloi
 *
 */
public class TriangulationTrace implements AlgoTrace {

    private int curMonotonPolygon;
    private MonotonTriangTrace monotonSubdivisionTrace;
    private List<Polygon> monotonPolygons;
    private List<MonotonTriangTrace> monotonPolygonsTriangTrace;

    public TriangulationTrace() {
        this.curMonotonPolygon = 0;
        this.monotonPolygons = new ArrayList<Polygon>();
        this.monotonPolygonsTriangTrace = new ArrayList<MonotonTriangTrace>();
    }

    @Override
    public void nextStep() {
        if (isDone())
            return;
        if (!monotonSubdivisionTrace.isDone()) {
            monotonSubdivisionTrace.nextStep();
        } else {
            if (monotonPolygonsTriangTrace.get(curMonotonPolygon).hasStep()) {
                monotonPolygonsTriangTrace.get(curMonotonPolygon).nextStep();
            } else {
                ++curMonotonPolygon;
            }
        }
    }

    @Override
    public boolean hasStep() {
        return curMonotonPolygon + 1 < monotonPolygons.size();
    }

    @Override
    public boolean isDone() {
        return curMonotonPolygon >= monotonPolygons.size();
    }

    @Override
    public void drawCurrentState(Graphics g) {
        if (isDone()) {
            drawFullResult(g);
            return;
        }
        if (!monotonSubdivisionTrace.isDone()) {
            monotonSubdivisionTrace.drawCurrentState(g);
        } else {
            monotonSubdivisionTrace.drawFullResult(g);
            for (int i = 0; i < curMonotonPolygon; ++i) {
                monotonPolygonsTriangTrace.get(i).drawFullResult(g);
            }
            if (!monotonPolygonsTriangTrace.get(curMonotonPolygon).isDone()) {
                try {
                    Graphics2D g2 = (Graphics2D) g;
                    Stroke s = g2.getStroke();
                    Color c = g2.getColor();
                    g.setColor(Color.RED);
                    g2.setStroke(new BasicStroke(2));
                    drawPolygon(g, monotonPolygons.get(curMonotonPolygon));
                    g2.setColor(c);
                    g2.setStroke(s);
                } catch (ClassCastException cce) {}
                monotonPolygonsTriangTrace.get(curMonotonPolygon)
                        .drawCurrentState(g);
                return;
            }
        }
    }

    @Override
    public void drawFullResult(Graphics g) {
        monotonSubdivisionTrace.drawFullResult(g);
        for (MonotonTriangTrace triangTrace : monotonPolygonsTriangTrace) {
            triangTrace.drawFullResult(g);
        }
    }

    public void drawPolygon(Graphics g, Polygon p) {
        int[] xPoints = new int[p.getNbPoints()];
        int[] yPoints = new int[p.getNbPoints()];
        for (int i = 0; i < p.getNbPoints(); ++i) {
            xPoints[i] = (int)p.getPoints().get(i).x;
            yPoints[i] = (int)p.getPoints().get(i).y;
        }
        g.drawPolygon(xPoints, yPoints, p.getNbPoints());
    }

    public void setMonotonSubdivisionTrace(
            MonotonTriangTrace monotonSubdivisionTrace) {
        this.monotonSubdivisionTrace = monotonSubdivisionTrace;
    }

    public void addMonotonPolygonsTriangTrace(MonotonTriangTrace trace) {
        this.monotonPolygonsTriangTrace.add(trace);
    }

    public void addMonotonPolygons(Polygon p) {
        this.monotonPolygons.add(p);
    }

    public int getCurMonotonPolygon() {
        return curMonotonPolygon;
    }

    public MonotonTriangTrace getMonotonSubdivisionTrace() {
        return monotonSubdivisionTrace;
    }

    public List<Polygon> getMonotonPolygons() {
        return monotonPolygons;
    }

    public List<MonotonTriangTrace> getMonotonPolygonsTriangTrace() {
        return monotonPolygonsTriangTrace;
    }

}
