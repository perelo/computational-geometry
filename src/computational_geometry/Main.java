package computational_geometry;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import computational_geometry.controller.Controller;
import computational_geometry.model.Model;
import computational_geometry.views.AbstractPanelView;
import computational_geometry.views.AlgorithmsGraphView;
import computational_geometry.views.DrawGraphView;
import computational_geometry.views.StateGraphView;

/**
 * Main class of the application
 * it creates the frame by composition and display it 
 * @author eloi
 *
 */
public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Computational geometry");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Model model = new Model();
        Controller controller = new Controller(model);

        AbstractPanelView typeView = new AlgorithmsGraphView(model, controller);
        AbstractPanelView drawView = new DrawGraphView(model, controller);
        AbstractPanelView stateView = new StateGraphView(model, controller);

        controller.addView(typeView);
        controller.addView(drawView);
        controller.addView(stateView);

        frame.getContentPane().add(typeView.getPanel(), BorderLayout.NORTH);
        frame.getContentPane().add(drawView.getPanel(), BorderLayout.CENTER);
        frame.getContentPane().add(stateView.getPanel(), BorderLayout.SOUTH);

        frame.pack();

        frame.setVisible(true);

    }

}
