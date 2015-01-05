package pl.mczerwi.spdb.model;

public class Point {

	private String id;
	private double x;
	private double y;
	
	public Point() {
		
	};
	
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
}
