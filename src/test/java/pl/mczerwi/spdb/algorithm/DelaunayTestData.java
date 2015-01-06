package pl.mczerwi.spdb.algorithm;

import java.util.Arrays;
import java.util.List;

import pl.mczerwi.spdb.model.Point;

public class DelaunayTestData {

	/**
	 * Test data for triangulation looking like this:
	 * 		  /p4
	 * 		p3  |
	 * 		| \	|
	 * 		|	p2
	 * 		p1 /
	 */
	public final static Point P1 = new Point("1", 1f, 1f);
	public final static Point P2 = new Point("2", 2f, 2f);
	public final static Point P3 = new Point("3", 1f, 4f);
	public final static Point P4 = new Point("4", 2f, 5f);
	
	public static List<Point> getPolygonPoints() {
		return Arrays.asList(new Point[] {P1, P2, P3, P1, P2, P3, P4, P2});
	}
}
