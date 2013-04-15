package computational_geometry.model.beans;

import computational_geometry.model.data_structures.CircularList;

/**
 * Scatter plot
 * @author eloi
 *
 */
public class Scatterplot extends Graph {

    public Scatterplot() {
        super();
    }

    public Scatterplot(CircularList<Point> points) {
        super(points);
    }

}
