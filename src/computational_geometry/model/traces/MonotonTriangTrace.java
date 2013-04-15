package computational_geometry.model.traces;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Segment;
import computational_geometry.views.SwingDrawer;

/**
 * Trace of a monoton triangulation trace
 * TODO : refactor and change description because it can be used for any algorithms
 * that uses a vertical sweep line and add diagonals to events represented by points
 * @author eloi
 *
 */
public class MonotonTriangTrace implements AlgoTrace {

    private int diagIndex;
    private int state;
    private List<Event> events;

    public MonotonTriangTrace() {
        this.diagIndex = 0;
        this.state = 0;
        this.events = new ArrayList<Event>();
    }

    public class Event {
        public Point p;
        public List<Segment> diags;

        public Event(Point p) {
            this.p = p;
            this.diags = new ArrayList<Segment>();
        }

        public void addDiagonal(Segment s) {
            diags.add(s);
        }
    }

    @Override
    public void nextStep() {
        if (++diagIndex >= events.get(state).diags.size()) {
            ++state;
            diagIndex = 0;
        }
    }

    @Override
    public boolean hasStep() {
        return state + 1 < events.size();
    }

    @Override
    public void drawCurrentState(Graphics g) {
        if (g == null) {
            System.err.println("Graphics is null");
            return;
        }
        Event event;
        Drawer drawer;
        // draw diagonals
        Color c = g.getColor();
        g.setColor(Color.GREEN);
        drawer = SwingDrawer.getInstance(g);
        for (int i = 0; i <= state; ++i) {
            event = events.get(i);
            if (i == state) {
                if (event.diags.size() > 0) {
                    for (int j = 0; j <= diagIndex; ++j) {
                        drawer.drawSegment(event.diags.get(j));
                    }
                }
            } else {
                drawEventDiags(drawer, event);
            }
        }
        event = events.get(state);
        // draw event (point)
        g.setColor(Color.YELLOW);
        Point p = event.p;
        g.fillOval(p.x - Point.range, p.y - Point.range, 2 * Point.range + 1,
                2 * Point.range + 1);
        g.drawOval(p.x - 2 * Point.range, p.y - 2 * Point.range,
                2 * 2 * Point.range + 1, 2 * 2 * Point.range + 1);
        // draw sweep line
        g.setColor(c);
        g.drawLine(0, event.p.y, (int) g.getClipBounds().getWidth(), event.p.y);
    }

    @Override
    public void drawFullResult(Graphics g) {
        if (g == null) {
            System.err.println("Graphics is null");
            return;
        }
        Color c = g.getColor();
        g.setColor(Color.GREEN);
        for (Event event : events) {
            drawEventDiags(SwingDrawer.getInstance(g), event);
        }
        g.setColor(c);
    }

    private void drawEventDiags(Drawer drawer, Event event) {
        for (Segment s : event.diags) {
            drawer.drawSegment(s);
        }
    }

    @Override
    public boolean isDone() {
        return state >= events.size();
    }

    public Event createNewEvent(Point p) {
        return new Event(p);
    }

    public int getDiagIndex() {
        return diagIndex;
    }

    public void setDiagIndex(int diagIndex) {
        this.diagIndex = diagIndex;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void addEvent(Event e) {
        events.add(e);
    }

    public void removeEvent(Event e) {
        events.remove(e);
    }

    public List<Event> getEvents() {
        return events;
    }

}
