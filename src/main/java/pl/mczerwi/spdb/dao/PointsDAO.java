package pl.mczerwi.spdb.dao;

import java.util.List;
import java.util.Set;

import pl.mczerwi.spdb.model.Point;

public interface PointsDAO {
	public List<Point> getPoints();
	public List<Point> getDelaunayTriangulationPolygonPoints();
	public void updatePoints(Set<Point> points);
}
