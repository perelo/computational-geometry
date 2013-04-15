package computational_geometry.views;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

import computational_geometry.controller.Controller;
import computational_geometry.model.Listener;
import computational_geometry.model.Model;

public class StateGraphView extends AbstractPanelView implements Listener {

    private JLabel labelState;
    private JLabel labelNbPoints;
    private JLabel labelNbSegments;

    public StateGraphView(Model model, Controller controller) {
        super(model, controller);
    }

    @Override
    protected void createPanelView() {

        JPanel panelMonoton = new JPanel();

        JLabel labelMonoton = new JLabel("Monoton : ");

        labelState = new JLabel();

        panelMonoton.add(labelMonoton);
        panelMonoton.add(labelState);

        JPanel panelNumStats = new JPanel(new BorderLayout());

        JPanel panelNbPoints = new JPanel(new BorderLayout());
        JLabel labelNbPointsLibelle = new JLabel("points : ");
        labelNbPoints = new JLabel("0");
        panelNbPoints.add(labelNbPointsLibelle, BorderLayout.WEST);
        panelNbPoints.add(labelNbPoints, BorderLayout.EAST);

        JPanel panelNbSegments = new JPanel(new BorderLayout());
        JLabel labelNbSegmentsLibelle = new JLabel("segments : ");
        labelNbSegments = new JLabel("0");
        panelNbSegments.add(labelNbSegmentsLibelle, BorderLayout.WEST);
        panelNbSegments.add(labelNbSegments, BorderLayout.EAST);

        panelNumStats.add(panelNbPoints, BorderLayout.NORTH);
        panelNumStats.add(panelNbSegments, BorderLayout.CENTER);

        panelView = new JPanel(new BorderLayout());
        panelView.add(panelMonoton, BorderLayout.WEST);
        panelView.add(panelNumStats, BorderLayout.EAST);
    }

    @Override
    public void polygonModified() {
        labelNbPoints.setText(model.getGraph().getNbPoints() + "");
        labelNbSegments.setText(model.getGraph().getNbSegments() + "");

        String label;
        if (model.getGraph().getNbPoints() < 3) {
            label = "No polygon";
            labelState.setForeground(Color.RED);
        } else if (model.isPolygonMonotonous()) {
            labelState.setForeground(Color.MAGENTA);
            label = "YES";
        } else {
            labelState.setForeground(Color.RED);
            label = "NO";
        }
        labelState.setText(label);

    }

}
