package computational_geometry.model.traces;

import java.awt.Graphics;

/**
 * Interface AlgoTrace used to draw or check each step of an algorithm separately
 * TODO : separate nextStep(), hasStep() and isDone() from drawing methods
 *
 * @author eloi
 *
 */
public interface AlgoTrace {

    void nextStep();

    boolean hasStep();

    boolean isDone();

    public void drawCurrentState(Graphics g);

    public void drawFullResult(Graphics g);

}
