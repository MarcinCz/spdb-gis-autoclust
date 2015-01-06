package pl.mczerwi.spdb.dao;

import java.util.List;

import pl.mczerwi.spdb.model.Point;

public interface PointsDAO {
	public List<Point> getPoints();
	public List<Point> getDelaunayTriangulationPolygonPoints();
}
