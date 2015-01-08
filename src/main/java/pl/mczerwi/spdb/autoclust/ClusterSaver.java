package pl.mczerwi.spdb.autoclust;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import pl.mczerwi.spdb.dao.PointsDAO;
import pl.mczerwi.spdb.helper.BeanHelper;
import pl.mczerwi.spdb.model.Graph;
import pl.mczerwi.spdb.model.Point;

public class ClusterSaver {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private PointsDAO pointsDAO;
	
	ClusterSaver() {
	}
	
	public static ClusterSaver getInstance() {
		return BeanHelper.getInstance().getInitializedBean(new ClusterSaver());
	}
	
	public void setPointsDAO(PointsDAO pointsDAO) {
		this.pointsDAO = pointsDAO;
	}
	
	public void saveForGraph(Graph graph) {
		//get connected components
		Map<Point, ConnectedComponent> connectedComponents = ConnectedComponent.generateConnectedComponents(graph);
		
		//set cluster numbers in points
		ComponentIdGenerator clusterNumberGenerator = new ComponentIdGenerator();
		for(Point point: graph.getPoints()) {
			ConnectedComponent component = connectedComponents.get(point);
			if(!component.isTrivial()) {
				point.setCluster(connectedComponents.get(point).getId());
			}
		}
		
		pointsDAO.updatePoints(graph.getPoints());
		logger.info("Saved clusters");
	}
}
