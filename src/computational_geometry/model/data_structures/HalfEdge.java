package computational_geometry.model.data_structures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import computational_geometry.model.beans.Point;

/**
 * Half-Edge data structure
 * @author eloi
 *
 */
public class HalfEdge {

    private List<Edge> edges;

    public HalfEdge() {
        edges = new ArrayList<HalfEdge.Edge>();
    }

    public class Vert {
        protected Point p;
        protected Edge edge;

        public Vert(Point p, Edge e) {
            this.p = p;
            this.edge = e;
        }

        public Point getPoint() {
            return p;
        }

        public Edge getEdge() {
            return edge;
        }
    }

    public class Edge {
        public boolean isDrawn; // TODO try to remove this field
        protected Vert vert;
        protected Edge twin;
        protected Face face;
        protected Edge next;

        public void fill(Vert vert, Edge twin, Face face, Edge next) {
            this.vert = vert;
            this.twin = twin;
            this.face = face;
            this.next = next;
        }

        public Vert getVert() {
            return vert;
        }

        public Edge getTwin() {
            return twin;
        }

        public Face getFace() {
            return face;
        }

        public Edge getNext() {
            return next;
        }
    }

    public class Face {
        protected Edge edge;

        public Face(Edge e) {
            this.edge = e;
        }

        public Edge getEdge() {
            return edge;
        }
    }

    /**
     * Checks if this HalfEdge is valid
     * TODO : write this method and detail description
     * @return
     */
    public boolean isValid() {
        return true;
    }

    public void addEdge(Edge e) {
        edges.add(e);
    }

    public void removeEdge(Edge e) {
        edges.remove(e);
    }

    /**
     * Get an iterator for edges, so we can iterate on them
     * without having the actual list and messing around with it
     * @return
     */
    public Iterator<Edge> getEdgeIterator() {
        return edges.iterator();
    }

}
