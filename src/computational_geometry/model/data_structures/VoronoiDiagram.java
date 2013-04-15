package computational_geometry.model.data_structures;

import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Segment;

/**
 * Voronoi diagram
 * @author eloi
 *
 */
public class VoronoiDiagram extends HalfEdge {

    public VoronoiDiagram() {
        super();
    }

    public class VorEdge extends HalfEdge.Edge {
        protected Segment s;

        @Override
        public void fill(Vert vert, Edge twin, Face face, Edge next) {
            if (!(twin instanceof VorEdge) || !(face instanceof VorCell)
                    || !(next instanceof VorEdge)) {
                throw new IllegalArgumentException(
                        "VorEdge must be filled with VorEdges and VorCell");
            }
            super.fill(vert, twin, face, next);
        }

        public void fill(Segment s, Vert vert, VorEdge twin, VorCell cell,
                VorEdge next) {
            super.fill(vert, twin, face, next);
            this.s = s;
        }

        public Segment getSeg() {
            return s;
        }
    }

    public class VorCell extends HalfEdge.Face {
        protected Point site;

        public VorCell(Point site, Edge e) {
            super(e);
            this.site = site;
        }

        public Point getSite() {
            return site;
        }
    }

    @Override
    public void addEdge(Edge e) {
        if (!(e instanceof VorEdge)) {
            throw new IllegalArgumentException(
                    "Can only add VorEdges to VoronoiDiagram.");
        }
        super.addEdge(e);
    }

}
