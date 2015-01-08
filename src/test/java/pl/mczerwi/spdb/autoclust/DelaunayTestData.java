package pl.mczerwi.spdb.autoclust;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.mczerwi.spdb.model.Edge;
import pl.mczerwi.spdb.model.EdgeType;
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
	public final static Point P1 = new Point(1, 10f, 10f);
	public final static Point P2 = new Point(2, 20f, 20f);
	public final static Point P3 = new Point(3, 10f, 40f);
	public final static Point P4 = new Point(4, 20f, 50f);
	
	public static List<Point> getPolygonPoints() {
		return Arrays.asList(new Point[] {P1, P2, P3, P1, P2, P3, P4, P2});
	}
	
	public static List<Edge> getEdges() {
		List<Edge> edges = new ArrayList<Edge>();
		edges.add(new Edge(P1, P2));
		edges.add(new Edge(P2, P3));
		edges.add(new Edge(P3, P4));
		edges.add(new Edge(P2, P4));
		for(Edge edge: edges) {
			edge.setType(EdgeType.OTHER);
		}
		return edges;
	}
}
