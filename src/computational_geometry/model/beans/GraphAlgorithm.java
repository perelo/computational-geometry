package computational_geometry.model.beans;

import computational_geometry.model.traces.AlgoTrace;

/**
 * Abstract class of algorithms applied on graphs
 * @author eloi
 *
 */
public abstract class GraphAlgorithm {

    protected AlgoTrace trace;

    public abstract void run(Graph graph);

    public AlgoTrace getTrace() {
        return trace;
    }

}
