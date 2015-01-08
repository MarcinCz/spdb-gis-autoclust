package pl.mczerwi.spdb.autoclust;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import pl.mczerwi.spdb.dao.PointsDAO;
import pl.mczerwi.spdb.model.Graph;
import pl.mczerwi.spdb.model.Point;

public class DelaunayGraphProvider {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private PointsDAO pointsDAO;
	
	public DelaunayGraphProvider() {
	}
	
	public Graph getDelaunayGraph() {
		logger.info("Getting delaunay graph");
		Graph graph = new Graph();
		List<Point> delaunayPoints = pointsDAO.getDelaunayTriangulationPolygonPoints();
		for(int i = 0; i < delaunayPoints.size(); i += 4) {
			graph.addEdge(delaunayPoints.get(i), delaunayPoints.get(i + 1));
			graph.addEdge(delaunayPoints.get(i + 1), delaunayPoints.get(i + 2));
			graph.addEdge(delaunayPoints.get(i + 2), delaunayPoints.get(i + 3));
		}
		logger.info("Got delaunay graph with " + graph.getPoints().size() + " points and " + graph.getEdges().size() + " edges");
		return graph;
	}
	
	public void setPointsDao(PointsDAO pointsDAO) {
		this.pointsDAO = pointsDAO;
	}
}
