package computational_geometry.model.traces;

import java.awt.Graphics;
import java.util.Iterator;

import computational_geometry.model.beans.Segment;
import computational_geometry.model.data_structures.HalfEdge.Edge;
import computational_geometry.model.data_structures.VoronoiDiagram;
import computational_geometry.model.data_structures.VoronoiDiagram.VorEdge;
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
            VorEdge e = (VorEdge) it.next();
            if (e.getTwin().isDrawn == false) {
                Segment s = new Segment(e.getOrigin().getPoint(), e.getTwin().getOrigin().getPoint());
                SwingDrawer.getInstance(g).drawSegment(s);
                e.isDrawn = true;
            }
        }
    }

}
