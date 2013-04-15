package computational_geometry.model;

import java.util.HashMap;
import java.util.Map;

import javax.swing.event.EventListenerList;

import computational_geometry.model.algorithms.ConvexHull;
import computational_geometry.model.algorithms.Monotonization;
import computational_geometry.model.algorithms.Triangulation;
import computational_geometry.model.algorithms.Voronoi;
import computational_geometry.model.beans.Graph;
import computational_geometry.model.beans.GraphAlgorithm;
import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Polygon;
import computational_geometry.model.beans.Scatterplot;
import computational_geometry.model.core.Polygons;
import computational_geometry.model.data_structures.CircularList;
import computational_geometry.model.traces.AlgoTrace;

/**
 * The model of the application
 * @author eloi
 *
 */
public class Model {

    private Graph graph;
    private GraphType graphType;

    private int numSelectedPoint;
    
    private boolean doSteps;

    protected EventListenerList listeners;

    public enum GraphType {
        POLYGON (new Polygon(), createPolygonAlgorithms()),
        SCATTERPLOT (new Scatterplot(), createScatterplotAlgorithms());

        private final Graph newGraph;
        private Map<String, GraphAlgorithm> algorithms;

        private GraphType(Graph graph, Map<String, GraphAlgorithm> algorithms) {
            this.newGraph = graph;
            this.algorithms = algorithms;
        }

        private static Map<String, GraphAlgorithm> createScatterplotAlgorithms() {
            Map<String, GraphAlgorithm> algorithms = new HashMap<String, GraphAlgorithm>();

            algorithms.put("Voronoï", new GraphAlgorithm() {
				@Override
				public void run(Graph graph) {
					trace = Voronoi.ComputeVoronoiDiagram(graph.getPoints());
				}
			});

            return algorithms;
        }

        private static Map<String, GraphAlgorithm> createPolygonAlgorithms() {
            Map<String, GraphAlgorithm> algorithms = new HashMap<String, GraphAlgorithm>();

            algorithms.put("Triangulation (monoton)", new GraphAlgorithm() {
                @Override
                public void run(Graph graph) {
                    trace = Triangulation.triangulateMonotonPolygon((Polygon) graph);
                }
            });

            algorithms.put("Monotonisation", new GraphAlgorithm() {
                @Override
                public void run(Graph graph) {
                    trace = Monotonization.monotonisePolygon((Polygon) graph);
                }
            });

            algorithms.put("Triangulation (simple)", new GraphAlgorithm() {
                @Override
                public void run(Graph graph) {
                    trace = Triangulation.triangulateSimplePolygon((Polygon) graph);
                }
            });

            algorithms.put("Convex hull", new GraphAlgorithm() {
                @Override
                public void run(Graph graph) {
                    trace = ConvexHull.polygonConvexHull((Polygon) graph);
                }
            });

            return algorithms;
        }

        public Map<String, GraphAlgorithm> getAlgorithms() {
            return algorithms;
        }

        public Graph getNewGraph(CircularList<Point> points) {
            newGraph.setPoints(points);
            return newGraph;
        }

        @Override
        public String toString() {      // first letter capitalized
            String s = super.toString();
            return s.substring(0, 1) + s.substring(1).toLowerCase();
        }

    }

    public Model() {
        this.graph = new Polygon();
        this.graphType = GraphType.POLYGON;
        this.numSelectedPoint = -1;
        this.doSteps = false;
        this.listeners = new EventListenerList();
    }

    public int getNumSelectedPoint() {
        return numSelectedPoint;
    }

    public void setNumSelectedPoint(int x, int y) {
        int oldNumSelectedPoint = numSelectedPoint;
        numSelectedPoint = graph.findFirstPointInRange(new Point(x, y));

        // no need to repaint all if nothing has changed
        if (oldNumSelectedPoint != numSelectedPoint) {
            firePolygonModified();
        }
    }

    public void addPointAt(int x, int y) {
        if (numSelectedPoint == -1) {
            numSelectedPoint = graph.getPoints().size();
            graph.addPoint(new Point(x, y));

            firePolygonModified();
        }
    }

    public void removePointAt(int x, int y) {
        if (numSelectedPoint != -1) {
            graph.getPoints().removeElementAt(numSelectedPoint);
            numSelectedPoint = graph.findFirstPointInRange(new Point(x, y));

            firePolygonModified();
        }
    }

    public void moveSelectedPointAt(int x, int y) {
        if (numSelectedPoint != -1) {
            Point p = graph.getPoints().elementAt(numSelectedPoint);
            p.x = x;
            p.y = y;

            firePolygonModified();
        }
    }

    public void eraseGraph() {
//        graph = graphType.getNewGraph(new CircularList<Point>());
        graph.clear();

        firePolygonModified();
    }

    public void generateRandPolygon(int n, int minX, int maxX, int minY, int maxY) {
        graph.generateNewPoints(n, minX, maxX, minY, maxY);
        firePolygonModified();
    }

    public boolean isPolygonMonotonous() {
        if (graphType == GraphType.POLYGON) {
            return Polygons.isMonotonous((Polygon) graph);
        }
        else {
            return false;
        }
    }

    public void addPolygonListener(Listener pl) {
        listeners.add(Listener.class, pl);
    }

    public Graph getGraph() {
        return this.graph;
    }

    public GraphType getGraphType() {
        return this.graphType;
    }

    public void setGraphType(GraphType type) {
        graphType = type;
        graph = type.getNewGraph(graph.getPoints());
        firePolygonModified();
    }

    public boolean doSteps() {
		return doSteps;
	}

	public void setDoSteps(boolean doSteps) {
		this.doSteps = doSteps;
		graph.clearAlgorithms();
		firePolygonModified();
	}

	public void addAlgorithmToExecute(GraphAlgorithm algo) {
        graph.addAlgorithm(algo);
        if (doSteps) {
	        algo.run(graph);
        }
        firePolygonModified();
    }

    public void removeAlgorithmToExecute(GraphAlgorithm algo) {
        graph.removeAlgorithm(algo);
        firePolygonModified();
    }

    public void removePolygonListener(Listener pl) {
        listeners.remove(Listener.class, pl);
    }

    public void firePolygonModified() {
    	if (!doSteps) {
	        graph.calculate();
    	}

        Listener[] polygonListeners = (Listener[]) listeners.getListeners(Listener.class);
        for (Listener l : polygonListeners) {
            l.polygonModified();
        }
    }
    
    public GraphAlgorithm getFirstAlgo() {
    	if (graph.getAlgorithms().isEmpty()) return null;
    	else return ((GraphAlgorithm)graph.getAlgorithms().toArray()[0]);
    }

	public void nextStepToAlgorithmTrace() {
		GraphAlgorithm algo = getFirstAlgo();
		AlgoTrace trace = (algo != null) ? algo.getTrace() : null;
		if (trace != null) {
			trace.nextStep();
		}
		firePolygonModified();
	}
}
