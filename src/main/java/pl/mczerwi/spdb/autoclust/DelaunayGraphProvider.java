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
			Point point1 = getPointToAdd(graph, delaunayPoints.get(i));
			Point point2 = getPointToAdd(graph, delaunayPoints.get(i + 1));
			Point point3 = getPointToAdd(graph, delaunayPoints.get(i + 2));
			graph.addEdges(point1, point2);
			graph.addEdges(point2, point3);
			graph.addEdges(point3, point1);
		}
		logger.info("Got delaunay graph with " + graph.getPoints().size() + " points and " + graph.getEdges().size() + " edges");
		return graph;
	}
	
	
	private Point getPointToAdd(Graph graph, Point point) {
		for(Point pointFromGraph: graph.getPoints()) {
			if(pointFromGraph.getId() == point.getId()) {
				return pointFromGraph;
			}
		}
		return point;
	}

	public void setPointsDao(PointsDAO pointsDAO) {
		this.pointsDAO = pointsDAO;
	}
}
