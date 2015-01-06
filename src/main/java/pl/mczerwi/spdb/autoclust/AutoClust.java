package pl.mczerwi.spdb.autoclust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import pl.mczerwi.spdb.dao.PointsDAO;
import pl.mczerwi.spdb.helper.BeanHelper;
import pl.mczerwi.spdb.model.Edge;
import pl.mczerwi.spdb.model.EdgeType;
import pl.mczerwi.spdb.model.Graph;
import pl.mczerwi.spdb.model.Point;

public class AutoClust {
	
	@Autowired
	private PointsDAO pointsDAO;
	private DelaunayGraphProvider graphProvider;
	private Graph graph;
	private ClusterNumberGenerator clusterNumberGenerator;
	
	private AutoClust() {
	
	};
	
	public static AutoClust getInstance() {
		AutoClust clust = new AutoClust();
		BeanHelper.getInstance().initBean(clust);
		clust.graphProvider = new DelaunayGraphProvider(clust.pointsDAO);
		clust.clusterNumberGenerator = new ClusterNumberGenerator();
		return clust;
	}
	
	public void run() {
		graph = graphProvider.getDelaunayGraph();
		
		//phase 1 - classify edges
		for(Point point: graph.getPoints()) {
			classifyEdges(point);
		}
		//create connected components
		Map<Point, ConnectedComponent> connectedComponents = new HashMap<Point, ConnectedComponent>();
		for(Point point: graph.getPoints()) {
			connectedComponents.put(point, ConnectedComponent.generateComponent(graph, point, clusterNumberGenerator.getNext()));
		}
		
		//phase 2
		for(Point point: graph.getPoints()) {
			List<ConnectedComponent> shortConnectedComponents = new ArrayList<ConnectedComponent>();
			for(Edge edge: graph.getEdgesForPoint(point)) {
				if(edge.getType() == EdgeType.SHORT) {
					ConnectedComponent component = connectedComponents.get(edge.getSecondPoint());
					if(!shortConnectedComponents.contains(component)) {
						shortConnectedComponents.add(component);
					}
				}
			}
		}
		
	}

	private void classifyEdges(Point point) {
		Set<Edge> edges = graph.getEdgesForPoint(point);
		int edgesCount = edges.size();
		
		//get local mean for edges distance
		double distanceSum = 0;
		for(Edge edge: edges) {
			distanceSum += edge.getDistance();
		}
		double localMean = distanceSum / edgesCount;
		
		//get local standard deviation
		double localVariance = 0;
		for(Edge edge: edges) {
			localVariance += Math.pow(localMean - edge.getDistance(), 2) / edgesCount;
		}
		double localStDeviation = Math.sqrt(localVariance);
		
		//edge classification based on mean and st. deviation
		for(Edge edge: edges) {
			if(edge.getDistance() < localMean - localStDeviation) {
				edge.setType(EdgeType.SHORT);
				edge.setRemoved(true);
			} else if(edge.getDistance() > localMean + localStDeviation) {
				edge.setType(EdgeType.LONG);
				edge.setRemoved(true);
			} else {
				edge.setType(EdgeType.OTHER);
			}
		}
	}	
	
}
