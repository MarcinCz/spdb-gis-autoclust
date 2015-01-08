package pl.mczerwi.spdb.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class Graph {
	
	private final Logger logger = Logger.getLogger(this.getClass());

	private Map<Point, Set<Edge>> adjecencyMap = new HashMap<Point, Set<Edge>>();
	
	public Set<Point> getPoints() {
		return adjecencyMap.keySet();
	}
	
	public Set<Edge> getEdgesForPoint(Point point) {
		return adjecencyMap.get(point);
	}
	
	public Set<Edge> getEdges() {
		Set<Edge> allEdges = new HashSet<Edge>();
		for(Set<Edge> edges: adjecencyMap.values()) {
			allEdges.addAll(edges);
		}
		return allEdges;
	}
	
	/**
	 * Adds two edges from first point to second point and reversed one.
	 * @param firstPoint
	 * @param secondPoint
	 */
	public void addEdges(Point firstPoint, Point secondPoint) {
		addEdge(new Edge(firstPoint, secondPoint));
	}
	
	public void addEdge(Edge edge) {
		maybeAddNewPoint(edge.getFirstPoint());
		maybeAddNewPoint(edge.getSecondPoint());
		
		adjecencyMap.get(edge.getFirstPoint()).add(edge);
		logger.trace("Added new edge to graph " + edge);
		adjecencyMap.get(edge.getSecondPoint()).add(edge.getReversedEdge());
		logger.trace("Added new edge to graph " + edge.getReversedEdge());
	}
	
	private void maybeAddNewPoint(Point point) {
		if(!adjecencyMap.containsKey(point)) {
			adjecencyMap.put(point, new HashSet<Edge>());
		}
	}
}
