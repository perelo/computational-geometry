package computational_geometry.model.traces;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import computational_geometry.model.beans.Segment;
import computational_geometry.model.data_structures.DCEL.Edge;
import computational_geometry.model.data_structures.VoronoiDiagram;
import computational_geometry.views.SwingDrawer;

/**
 * Trace of the voronoi diagram computation algorithm using the divide and conquer paradigm
 * @author eloi
 *
 */
public class VoronoiTrace extends SimpleAlgoResult {

    // TODO list of vor diagrams and list of dividing chain
    public VoronoiDiagram vor;

    @Override
    public void drawFullResult(Graphics g) {
        Iterator<Edge> it = vor.getEdgeIterator();
        while (it.hasNext()) {
            Edge e = it.next();
            if (e.getTwin().isDrawn == false) {
                Segment s = new Segment(e.getOrigin().getPoint(), e.getTwin().getOrigin().getPoint());
                SwingDrawer.getInstance(g).drawSegment(s);
                e.isDrawn = true;
            }
        }
        if (vor.lastDivideLine != null && vor.lastDivideLine.size() != 0) {
            Color c = g.getColor();
            g.setColor(Color.GREEN);
            for (int i = 1; i < vor.lastDivideLine.size(); ++i) {
                SwingDrawer.getInstance(g).drawSegment(new Segment(vor.lastDivideLine.get(i-1), vor.lastDivideLine.get(i)));
            }
            g.setColor(c);
        }
    }

}
