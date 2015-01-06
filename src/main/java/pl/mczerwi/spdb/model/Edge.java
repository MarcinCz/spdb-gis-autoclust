package pl.mczerwi.spdb.model;

public class Edge {

	private Point firstPoint;
	private Point secondPoint;
	private double distance;
	
	public Edge(Point firstPoint, Point secondPoint) {
		this.firstPoint = firstPoint;
		this.secondPoint = secondPoint;
		this.distance = Math.sqrt(Math.pow(firstPoint.getX() - secondPoint.getX(), 2f) + Math.pow(firstPoint.getY() - secondPoint.getY(), 2f));
	}
	
	private Edge(Point firstPoint, Point secondPoint, double distance) {
		this.firstPoint = firstPoint;
		this.secondPoint = secondPoint;
		this.distance = distance;
	}
	
	public Point getFirstPoint() {
		return firstPoint;
	}
	
	public Point getSecondPoint() {
		return secondPoint;
	}

	public double getDistance() {
		return distance;
	}
	
	public Edge getReversedEdge() {
		return new Edge(this.secondPoint, this.firstPoint, this.distance);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Edge)) {
			return false;
		} else {
			Edge edge = (Edge) obj;
			return this.firstPoint.getId() == edge.getFirstPoint().getId() && this.secondPoint.getId() == edge.getSecondPoint().getId();
		}
	}
	
	@Override
	public int hashCode() {
		return (firstPoint.getId() + "-" + secondPoint.getId()).hashCode();
	}
}
