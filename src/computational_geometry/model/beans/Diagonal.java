package computational_geometry.model.beans;

/**
 * Just like segments but don't set atBegin and atEnd of u and v when constructing
 * TODO : remove this class and fix this poor design by modifying Segment class
 * @author eloi
 *
 */
public class Diagonal extends Segment {

    public Diagonal(Point u, Point v) {
        super();	// use this so we don't do crap u.atBegin = this...
        this.u = u;
        this.v = v;
        this.helper = null;
        this.u.addDiagonal(this);
        this.v.addDiagonal(this);
    }


}
