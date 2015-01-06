package pl.mczerwi.spdb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {
	private Map<Point, Set<Edge>> adjecencyMap = new HashMap<Point, Set<Edge>>();
	
	public List<Point> getPoints() {
		return new ArrayList<Point>(adjecencyMap.keySet());
	}
	
	public Set<Edge> getEdgesForPoint(Point point) {
		return adjecencyMap.get(point);
	}
	
	public void addEdge(Point firstPoint, Point secondPoint) {
		addEdge(new Edge(firstPoint, secondPoint));
	}
	
	public void addEdge(Edge edge) {
		maybeAddNewPoint(edge.getFirstPoint());
		maybeAddNewPoint(edge.getSecondPoint());
		
		adjecencyMap.get(edge.getFirstPoint()).add(edge);
		adjecencyMap.get(edge.getSecondPoint()).add(edge.getReversedEdge());
	}
	
	private void maybeAddNewPoint(Point point) {
		if(!adjecencyMap.containsKey(point)) {
			adjecencyMap.put(point, new HashSet<Edge>());
		}
	}
}
