package computational_geometry.model.traces;

import java.awt.Graphics;

/**
 * Trace of an algorithm which we don't want to bother writing the step by step movement
 * @author eloi
 *
 */
public abstract class SimpleAlgoResult implements AlgoTrace {

    @Override
    public void nextStep() {}

    @Override
    public boolean hasStep() {
        return false;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public void drawCurrentState(Graphics g, boolean colorize) {}

}
