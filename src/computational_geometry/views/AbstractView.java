package computational_geometry.views;

import computational_geometry.controller.Controller;
import computational_geometry.model.Listener;
import computational_geometry.model.Model;

public abstract class AbstractView implements Listener {

    protected Model model;
    protected Controller controller;

    public AbstractView(Model model, Controller controller) {
        super();
        this.model = model;
        this.controller = controller;
    }

}
