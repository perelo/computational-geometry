package computational_geometry.model.data_structures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Segment;

/**
 * Half-Edge data structure
 * @author eloi
 *
 */
public class HalfEdge {

    private List<Edge> edges;
    private List<Face> faces;

    public HalfEdge() {
        edges = new ArrayList<HalfEdge.Edge>();
        faces = new ArrayList<HalfEdge.Face>();
    }

    public class Vert {
        protected Point p;
        protected Edge edge;

        public String toString() {
            return "vert.p=" + p;
        }

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
        public boolean isNew;   // TODO try to remove or at least put in VorEdge
        protected Vert origin;
        protected Edge twin;
        protected Face face;
        protected Edge next;
        public Edge() {
            isNew = false;
        }
        public String toString() {
            return getSegment().toString();
        }

        public void fill(Vert origin, Edge twin, Face face, Edge next) {
            this.origin = origin;
            this.twin = twin;
            this.face = face;
            this.next = next;
        }

        public Segment getSegment() {
            return new Segment(origin.p, twin.origin.p);
        }

        public Vert getOrigin() {
            return origin;
        }

        public void setOrigin(Vert origin) {
            this.origin = origin;
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

        public void setNext(Edge next) {
            this.next = next;
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

    public void addFace(Face f) {
        faces.add(f);
    }

    public void removeFace(Face f) {
        faces.remove(f);
    }

    /**
     * Get an iterator for edges, so we can iterate on them
     * without having the actual list and messing around with it
     * @return
     */
    public Iterator<Edge> getEdgeIterator() {
        return edges.iterator();
    }

    /**
     * Get an iterator for faces, so we can iterate on them
     * without having the actual list and messing around with it
     * @return
     */
    public Iterator<Face> getFaceIterator() {
        return faces.iterator();
    }

}
