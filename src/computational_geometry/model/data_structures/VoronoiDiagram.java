package computational_geometry.model.data_structures;

import computational_geometry.model.beans.Point;

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

}
