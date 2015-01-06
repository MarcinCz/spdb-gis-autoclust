package pl.mczerwi.spdb.model;

public class Point implements Comparable<Point>{

	private String id;
	private double x;
	private double y;
	
	public Point() {
		
	};
	
	public Point(String id, double x, double y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int compareTo(Point o) {
		return o.id.compareTo(this.id);
	}
}
