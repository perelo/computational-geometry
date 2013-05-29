package computational_geometry.model.algorithms;

import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Polygon;
import computational_geometry.model.core.Polygons;
import computational_geometry.model.core.Utils;
import computational_geometry.model.data_structures.LinkNode;
import computational_geometry.model.traces.MinkowskiTrace;

public class Minkowski {

    /**
     * Compute the Minkowski sum
     * the two polygons must be convex,
     * the lists of their vertices are in counterclockwise order
     * @param firstPolygon
     * @param secondPolygon
     * @return
     */
    public static MinkowskiTrace computeMinkowskiSum(Polygon firstPolygon, Polygon secondPolygon) {
        if (firstPolygon.getNbPoints() < 3   || secondPolygon.getNbPoints() < 3 ||
            !Polygons.isConvex(firstPolygon) || !Polygons.isConvex(secondPolygon)) {
            return null;
        }

        MinkowskiTrace trace = new MinkowskiTrace();
        Polygon sum = new Polygon();
        LinkNode<Point> v, w;
        int i, j;

        // find the two first vertices, ie
        // those w/ the smallest y-coordinate
        v = firstPolygon.getPoints().getNode(0);
        for (LinkNode<Point> lnp : firstPolygon.getPoints().getNodes()) {
            if ((lnp.getValue().y > v.getValue().y) ||
                (lnp.getValue().y == v.getValue().y &&
                 lnp.getValue().x <  v.getValue().x)) {
                v = lnp;
            }
        }
        w = secondPolygon.getPoints().getNode(0);
        for (LinkNode<Point> lnp : secondPolygon.getPoints().getNodes()) {
            if ((lnp.getValue().y > w.getValue().y) ||
                (lnp.getValue().y == w.getValue().y &&
                 lnp.getValue().x <  w.getValue().x)) {
                w = lnp;
            }
        }

        int ori;
        i = j = 1;
        do {
            sum.addPoint(v.getValue().sum(w.getValue()));
            trace.addCouple(v.getValue(), w.getValue());
            ori = Utils.orientation(v.getValue(), v.getNext().getValue(), w.getValue(), w.getNext().getValue());
            if (ori < 0) {
                v = v.getNext();
                ++i;
            } else if (ori > 0) {
                w = w.getNext();
                ++j;
            } else {
                v = v.getNext();
                w = w.getNext();
                ++i; ++j;
            }
        } while (i !=  firstPolygon.getNbPoints()+1 ||
                 j != secondPolygon.getNbPoints()+1);

        trace.setPolygon(sum);
        return trace;
    }

}
