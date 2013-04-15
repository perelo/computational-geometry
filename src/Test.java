import java.util.Collections;

import computational_geometry.model.beans.Point;
import computational_geometry.model.beans.Point.Type;
import computational_geometry.model.beans.Segment;
import computational_geometry.model.core.CrossProdComparator;
import computational_geometry.model.data_structures.BinarySearchTree;
import computational_geometry.model.data_structures.CircularList;
import computational_geometry.model.data_structures.SweepState;



public class Test {

    public static void main(String[] args) {

        testCircularList();
//        testBinaryTree();
//        testSortSegments();

    }

    @SuppressWarnings("unused")
    private static void testSortSegments() {

        Point origin = new Point(0, 0);
        Point a = new Point(-5, 5);
        Point b = new Point(0, 5);
        Point c = new Point(5, 5);
        Point d = new Point(-5, -5);
        Point e = new Point(0, -5);
        Point f = new Point(5, -5);
        a.type = Type.START;
        b.type = Type.MERGE;
        c.type = Type.END;
        d.type = Type.REGULARL;
        e.type = Type.SPLIT;
        f.type = Type.REGULARR;

        CircularList<Segment> segments = new CircularList<Segment>();
        segments.add(new Segment(origin, b));
        segments.add(new Segment(origin, a));
        segments.add(new Segment(origin, f));
        segments.add(new Segment(origin, d));
        segments.add(new Segment(origin, c));
        segments.add(new Segment(origin, e));

        Collections.sort(segments, new CrossProdComparator(origin));

        for (Segment s : segments) {
            System.out.println(s);
        }

    }

    @SuppressWarnings("unused")
    private static void testBinaryTree() {

        BinarySearchTree<Segment> tree = new BinarySearchTree<Segment>();

        Segment.yComp = 2;
        Segment s1 = new Segment(new Point(2, 1), new Point(4, 3));
        Segment s2 = new Segment(new Point(3, 5), new Point(6, 0));
        tree.insert(s1);
        tree.insert(s2);

        tree.delete(s2);

        return;

    }

    private static void testCircularList() {

        Point start = new Point(400,  80);
        Point endl  = new Point(40,  500);
        Point endr  = new Point(800, 500);
        Point split = new Point(400, 300);

        start.type = Point.Type.START;
        endl.type = Point.Type.END;
        endr.type = Point.Type.END;
        split.type = Point.Type.SPLIT;

        new Segment(start, endl);
        new Segment(start, split);
        new Segment(start, endr);

//        for (Segment s : start.getDiagonals()) {
//            System.out.println(s);
//        }


//        System.out.println(points.forward());

//        PriorityQueue<LinkNode<Point>> queue = new PriorityQueue<LinkNode<Point>>(points.getNodes());
//        for ( ; !queue.isEmpty(); ) {
//            System.out.println(queue.poll().getValue());
//        }


        CircularList<Integer> list = new CircularList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        for (int i = 0; i <  list.size() ; ++i) {
            System.out.print(list.get(i));
        }
        Collections.reverse(list);
        System.out.println();
        for (int i = 0; i <  list.size() ; ++i) {
            System.out.print(list.get(i));
        }

    }

    @SuppressWarnings("unused")
    private static void test() {

        System.out.println();

        SweepState state = new SweepState();
        state.setEvent(new Point(6, 2));

//        Segment a = new Segment("a", new Point(1, 1), new Point(3, 3));
//        Segment b = new Segment("b", new Point(3, 5), new Point(6, 0));
//        state.insert(a);
//        state.insert(b);

//        state.display();
        System.out.println(state.findFirstLeftOfEvent(state.getRoot()));
    }

}
