package computational_geometry.model;

import java.util.EventListener;

/**
 * Listener interface
 * @author eloi
 *
 */
public interface Listener extends EventListener {

	// TODO : refactor method because it's not only polygons
    public void polygonModified();

}
