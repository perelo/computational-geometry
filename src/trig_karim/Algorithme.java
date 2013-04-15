package trig_karim;

import java.util.Random;
import java.util.Vector;

/** La classe algorithme. */
class Algorithmes {

	/** Algorithme qui prend un ensemble de points et qui retourne un ensemble de segments. */
	static Vector<Segment> algorithme1(Vector<Point> points)
	{
		// Creation de la liste des segments
		Vector<Segment> segments = new Vector<Segment>();

		// Ajout d'un segment entre chaque paire de points consecutifs
		for(int n1 = 0; n1 < points.size() - 1; n1++)
		{
			Point p1 = points.elementAt(n1);
			Point p2 = points.elementAt(n1+1);
			segments.addElement(new Segment(p1,p2));
		}

		return segments;
	}

	/** Retourne un nombre aleatoire entre 0 et n-1. */
	static int rand(int n)
	{
		int r = new Random().nextInt();

		if (r < 0) r = -r;

		return r % n;
	}
}
