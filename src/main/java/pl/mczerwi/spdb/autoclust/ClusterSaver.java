package pl.mczerwi.spdb.autoclust;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import pl.mczerwi.spdb.dao.PointsDAO;
import pl.mczerwi.spdb.model.Graph;
import pl.mczerwi.spdb.model.Point;

public class ClusterSaver {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private PointsDAO pointsDAO;
	
	public ClusterSaver() {
	}
	
	public void setPointsDAO(PointsDAO pointsDAO) {
		this.pointsDAO = pointsDAO;
	}
	
	public void saveForGraph(Graph graph) {
		//get connected components
		Set<ConnectedComponent> connectedComponents = new HashSet<ConnectedComponent>();
		for(Point point: graph.getPoints()) {
			connectedComponents.add(ConnectedComponent.generateComponent(graph, point));
		}
		//set cluster numbers in points
		ClusterNumberGenerator clusterNumberGenerator = new ClusterNumberGenerator();
		for(ConnectedComponent component: connectedComponents) {
			if(!component.isTrivial()) {
				int clusterNumber = clusterNumberGenerator.getNext();
				for(Point point: component.getPoints()) {
					point.setCluster(clusterNumber);
				}
			}
		}
		
		pointsDAO.updatePoints(graph.getPoints());
		logger.info("Saved clusters");
	}
}
