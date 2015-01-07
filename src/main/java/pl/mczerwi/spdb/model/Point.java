package pl.mczerwi.spdb.model;

public class Point implements Comparable<Point>{

	public final static int CLUSTER_UNDEFINED = -1;
	
	private int id;
	private double x;
	private double y;
	private int cluster = CLUSTER_UNDEFINED;
	
	public Point() {
		
	};
	
	public Point(int id, double x, double y) {
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int compareTo(Point o) {
		return String.valueOf(o.id).compareTo(String.valueOf(this.id));
	}

	public int getCluster() {
		return cluster;
	}

	public void setCluster(int cluster) {
		this.cluster = cluster;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Point)) {
			return false;
		} else {
			Point point = (Point) obj;
			return this.id == point.id;
		}
	}
	
	@Override
	public int hashCode() {
		return String.valueOf(this.id).hashCode();
	}
	
	@Override
	public String toString() {
		return this.id + ": x=" + this.x + ", y=" + this.y + " cluster=" + this.cluster;
	}

}
