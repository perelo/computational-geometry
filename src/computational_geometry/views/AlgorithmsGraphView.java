package computational_geometry.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import computational_geometry.controller.Controller;
import computational_geometry.model.Model;
import computational_geometry.model.Model.GraphType;

public class AlgorithmsGraphView extends AbstractPanelView {

    private ButtonGroup typesButtonGroup;
    private JPanel panelAlgorithmsButtons;
    private Map<String, JRadioButton> algorithmButtons;

    public AlgorithmsGraphView(Model model, Controller controller) {
        super(model, controller);
    }

    @Override
    protected void createPanelView() {

        algorithmButtons = new HashMap<String, JRadioButton>();

        JPanel panelTypeButtons = new JPanel();
        JPanel panelAlgorithms = new JPanel(new BorderLayout());
        panelAlgorithmsButtons = new JPanel(new CardLayout());

        final JLabel lblAlgorithms = new JLabel("Algorithms");

        // for each types, add button and a panel with it's algorithms
        typesButtonGroup = new ButtonGroup();
        for (GraphType type : Model.GraphType.values()) {
            // add types of graph buttons
            final GraphType t = type;
            JRadioButton b = new JRadioButton(type.toString());
            b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    controller.notifyChangeGraphTypeRequest(t);
                }
            });
            typesButtonGroup.add(b);
            panelTypeButtons.add(b);

            // add panel of algorithms for the type
            JPanel panelAlgoPanel = new JPanel();
            for (String algoName : type.getAlgorithms().keySet()) {
                JRadioButton algorithmButton = new JRadioButton(algoName);
                algorithmButtons.put(algoName, algorithmButton);
                algorithmButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        JRadioButton b = algorithmButtons.get(arg0.getActionCommand());
                        controller.notifyChangeAlgorithmStateRequest(b.getActionCommand(), b.isSelected());
                    }
                });
                panelAlgoPanel.add(algorithmButton);
            }
            panelAlgorithmsButtons.add(panelAlgoPanel, type.toString());
        }
        setButtonAsGraphType(typesButtonGroup);

        panelAlgorithms.add(lblAlgorithms, BorderLayout.WEST);
        panelAlgorithms.add(panelAlgorithmsButtons, BorderLayout.CENTER);

        panelView = new JPanel(new BorderLayout());

        panelView.add(panelTypeButtons, BorderLayout.CENTER);
        panelView.add(panelAlgorithms, BorderLayout.SOUTH);

    }

    @Override
    public void polygonModified() {
        ((CardLayout)panelAlgorithmsButtons.getLayout()).show(panelAlgorithmsButtons, model.getGraphType().toString());

        for (Entry<String, JRadioButton> algoButton : algorithmButtons.entrySet()) {
            algoButton.getValue().setSelected(
                          model.getGraph().getAlgorithms().contains(
                              model.getGraphType().getAlgorithms().get(algoButton.getKey())));
        }
    }

    private void setButtonAsGraphType(ButtonGroup group) {
        String type = model.getGraphType().toString();

        Enumeration<AbstractButton> buttons = group.getElements();
        while (buttons.hasMoreElements()) {
            AbstractButton b = buttons.nextElement();
            if (b.getActionCommand().equals(type)) {
                b.setSelected(true);
                return;
            }
        }
    }

}
