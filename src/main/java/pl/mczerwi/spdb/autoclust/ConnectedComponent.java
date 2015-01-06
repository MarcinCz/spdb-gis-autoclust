package pl.mczerwi.spdb.autoclust;

import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import pl.mczerwi.spdb.model.Edge;
import pl.mczerwi.spdb.model.Graph;
import pl.mczerwi.spdb.model.Point;

public class ConnectedComponent {

	private int id;
	private Set<Point> points = new TreeSet<Point>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Set<Point> getPoints() {
		return points;
	}
	
	public void setPoints(Set<Point> points) {
		this.points = points;
	}
	
	private void addPoint(Point point) {
		this.points.add(point);
	}
	
	public static ConnectedComponent generateComponent(Graph graph, Point point, int id) {
		//create new connected component using breadth first search
		//it searches for all points connected by other edges
		ConnectedComponent component = new ConnectedComponent();
		component.setId(id);
		component.addPoint(point);
		LinkedList<Point> pointsToVisit = new LinkedList<Point>();
		pointsToVisit.push(point);
		
		while(pointsToVisit.size() > 0) {
			Point visitedPoint = pointsToVisit.pop();
			for(Edge edge: graph.getEdgesForPoint(visitedPoint)) {
				if(!edge.isRemoved() && !component.getPoints().contains(edge.getSecondPoint())) {
					component.addPoint(edge.getSecondPoint());
					pointsToVisit.push(edge.getSecondPoint());
				}
			}
		}
		
		return component;
	}
	
	public static ConnectedComponent getTrivialConnectedComponent(Point point, int id) {
		ConnectedComponent component = new ConnectedComponent();
		component.addPoint(point);
		component.id = id;
		return component;
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
}
