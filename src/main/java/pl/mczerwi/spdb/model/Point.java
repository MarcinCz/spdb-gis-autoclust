package pl.mczerwi.spdb.model;

public class Point implements Comparable<Point>{

	public final static int CLUSTER_UNDEFINED = -1;
	
	private String id;
	private double x;
	private double y;
	private int cluster = CLUSTER_UNDEFINED;
	
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

	public int getCluster() {
		return cluster;
	}

	public void setCluster(int cluster) {
		this.cluster = cluster;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Edge)) {
			return false;
		} else {
			Point point = (Point) obj;
			return this.id == point.id;
		}
	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

}
