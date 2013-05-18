package computational_geometry.model.data_structures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Segment;

/**
 * Voronoi diagram
 * @author eloi
 *
 */
public class VoronoiDiagram extends DCEL {

    public VoronoiDiagram() {
        super();
    }

    public class VorCell extends DCEL.Face {
        protected Point site;

        public String toString() {
            return "site=" + site;
        }

        public VorCell(Point site, Edge e) {
            super(e);
            this.site = site;
        }

        public Point getSite() {
            return site;
        }
    }

    @Override
    public void addFace(Face f) {
        if (!(f instanceof VorCell)) {
            throw new IllegalArgumentException(
                    "Can only add VorCells to VoronoiDiagram.");
        }
        super.addFace(f);
    }

    /**
     * Compute the dual of this VoronoiDiagram
     * @return the segments belonging to the Delaunay's triangulation
     */
    public List<Segment> computeDelaunay() {
        List<Segment> del = new ArrayList<Segment>();
        for (Iterator<Edge> it = getEdgeIterator(); it.hasNext(); ) {
            Edge e = it.next();
            if (!e.isDrawn) {
                del.add(new Segment(((VorCell)e.getFace()).site, ((VorCell)e.getTwin().getFace()).site));
                e.isDrawn = true;
                e.getTwin().isDrawn = true;
            }
        }
        for (Iterator<Edge> it = getEdgeIterator(); it.hasNext(); ) {
            it.next().isDrawn = false;
        }
        return del;
    }

}
