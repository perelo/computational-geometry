package computational_geometry.controller;

import java.util.ArrayList;
import java.util.List;

import computational_geometry.model.Model;
import computational_geometry.model.Model.GraphType;
import computational_geometry.model.beans.GraphAlgorithm;
import computational_geometry.views.AbstractView;

/**
 * The controller of the application
 * @author eloi
 *
 */
public class Controller {

    private List<AbstractView> views;
    private Model model;

    public Controller(Model model) {
        this.views = new ArrayList<AbstractView>();
        this.model = model;
    }

    public void addView(AbstractView view) {
        views.add(view);
        model.addPolygonListener(view);
    }

    public void notifyPointAddRequest(int x, int y) {
        model.addPointAt(x, y);
    }

    public void notifyPointRemoveRequest(int x, int y) {
        model.removePointAt(x, y);
    }

    public void notifyHoverChanged(int x, int y) {
        model.setNumSelectedPoint(x, y);
    }

    public void notifyPointMoveRequest(int x, int y) {
        model.moveSelectedPointAt(x, y);
    }

    public void notifyErasePolygonRequest() {
        model.eraseGraph();
    }

    public void notifyChangeAlgorithmStateRequest(String name, boolean state) {
        GraphAlgorithm algo = model.getGraphType().getAlgorithms().get(name);
        if (algo != null) {
            if (state) {
                model.addAlgorithmToExecute(algo);
            } else {
                model.removeAlgorithmToExecute(algo);
            }
        }
    }

    public void notifyChangeGraphTypeRequest(GraphType type) {
        model.setGraphType(type);
    }

    public void notifyGenerateRandPolygonRequest(int n, int minX, int maxX,
            int minY, int maxY) {
        model.eraseGraph();
        model.generateRandPolygon(n, minX, maxX, minY, maxY);
    }

    public void stepByStepRequested(boolean b) {
        model.setDoSteps(b);
    }

    public void nextStepRequested() {
        model.nextStepToAlgorithmTrace();
    }

}
