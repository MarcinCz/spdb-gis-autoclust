package pl.mczerwi.spdb.autoclust;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import pl.mczerwi.spdb.model.Edge;
import pl.mczerwi.spdb.model.Graph;
import pl.mczerwi.spdb.model.Point;

public class ConnectedComponent {

	private Set<Point> points = new TreeSet<Point>();
	
	public Set<Point> getPoints() {
		return points;
	}
	
	public void setPoints(Set<Point> points) {
		this.points = points;
	}
	
	private void addPoint(Point point) {
		this.points.add(point);
	}
	
	public static ConnectedComponent generateComponent(Graph graph, Point point) {
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
		
		return component;
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
