package computational_geometry.model.core;

import java.util.Comparator;

import computational_geometry.model.beans.Point;

/**
 * Comparator of Points according to X coordinate first, than Y
 * @author eloi
 *
 */
public class PointLexicalComparator implements Comparator<Point> {

    @Override
    public int compare(Point arg0, Point arg1) {
        if (arg0.x < arg1.x)
            return -1;
        else if (arg0.x == arg1.x) {
            if (arg0.y < arg1.y)
                return -1;
            else if (arg0.y == arg1.y)
                return 0;
            else
                return 1;
        } else
            return 1;
    }

}