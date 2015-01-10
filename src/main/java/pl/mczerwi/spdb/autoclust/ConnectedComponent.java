package pl.mczerwi.spdb.autoclust;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import pl.mczerwi.spdb.model.Edge;
import pl.mczerwi.spdb.model.Graph;
import pl.mczerwi.spdb.model.Point;

public class ConnectedComponent {

	private final static Logger logger = Logger.getLogger(ConnectedComponent.class);

	private Set<Point> points = new LinkedHashSet<Point>();
	
	private int id;
	
	public Set<Point> getPoints() {
		return points;
	}
	
	public void setPoints(Set<Point> points) {
		this.points = points;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	private void addPoint(Point point) {
		this.points.add(point);
	}
	
	public static Map<Point, ConnectedComponent> generateConnectedComponents(Graph graph) {
		ComponentIdGenerator idGenerator = new ComponentIdGenerator();
		Map<Point, ConnectedComponent> connectedComponents = new HashMap<Point, ConnectedComponent>();
		for(Point point: graph.getPoints()) {
			boolean hasComponent = false;
			for(ConnectedComponent component: connectedComponents.values()) {
				if(component.getPoints().contains(point)) {
					connectedComponents.put(point, component);
					hasComponent = true;
					break;
				}
			}
			if(!hasComponent) {
				ConnectedComponent component = generateComponent(graph, point);
				connectedComponents.put(point, component);
				setIdForGeneratedComponent(component, idGenerator);
			}
		}
		
		return connectedComponents;
	}
	
	static ConnectedComponent generateComponent(Graph graph, Point point) {
		//create new connected component using breadth first search
		//it searches for all points connected by other edges
		ConnectedComponent component = new ConnectedComponent();
		component.addPoint(point);
		LinkedList<Point> pointsToVisit = new LinkedList<Point>();
		pointsToVisit.push(point);
		
		while(pointsToVisit.size() > 0) {
			Point visitedPoint = pointsToVisit.pop();
			for(Edge edge: graph.getOutgoingEdgesForPoint(visitedPoint)) {
				if(!edge.isRemoved() && !component.getPoints().contains(edge.getSecondPoint())) {
					component.addPoint(edge.getSecondPoint());
					pointsToVisit.push(edge.getSecondPoint());
				}
			}
			for(Edge edge: graph.getIngoingEdgesForPoint(visitedPoint)) {
				if(!edge.isRemoved() && !component.getPoints().contains(edge.getFirstPoint())) {
					component.addPoint(edge.getFirstPoint());
					pointsToVisit.push(edge.getFirstPoint());
				}
			}
		}
		if(Logger.getRootLogger().getLevel().isGreaterOrEqual(Level.TRACE)) {
			logger.trace("Generated component for point " + point.getId());
		}
		return component;
	}
	
	private static void setIdForGeneratedComponent(ConnectedComponent component, ComponentIdGenerator generator) {
		if(component.isTrivial()) {
			component.setId(Point.CLUSTER_UNDEFINED);
		} else {
			component.setId(generator.getNext());
		}
	}

	public boolean isTrivial() {
		return points.size() == 1;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof ConnectedComponent)) {
			return false;
		} else {
			ConnectedComponent component = (ConnectedComponent) obj;
			return this.getPoints().equals(component.getPoints());
		}
	}
	
	@Override
	public int hashCode() {
		return points.hashCode();
	}

}
