package computational_geometry.views;

import javax.swing.JPanel;

import computational_geometry.controller.Controller;
import computational_geometry.model.Model;

public abstract class AbstractPanelView extends AbstractView {

    protected JPanel panelView;

    public AbstractPanelView(Model model, Controller controller) {
        super(model, controller);
        createPanelView();
    }

    public JPanel getPanel() {
        return this.panelView;
    }

    protected abstract void createPanelView();

}
