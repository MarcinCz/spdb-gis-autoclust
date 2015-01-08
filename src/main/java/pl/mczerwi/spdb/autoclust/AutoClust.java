package pl.mczerwi.spdb.autoclust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
	
	private Graph graph;
	
	public AutoClust() {	
	}
	
	public void run(Graph graph) {
		this.graph = graph;
		
		//phase 1 - classify edges
		double stDeviationSum = 0;
		Map<Point, EdgesStatisticalData> edgesStatisticalDataMap = new HashMap<Point, EdgesStatisticalData>();
		for(Point point: graph.getPoints()) {
			EdgesStatisticalData data = getEdgesStatisticalData(point);
			stDeviationSum += data.getLocalStDeviation();
			edgesStatisticalDataMap.put(point, data);
		}
		double meanStDeviation = stDeviationSum / graph.getPoints().size(); 
		
		for(Point point: graph.getPoints()) {
			classifyEdges(point, edgesStatisticalDataMap.get(point), meanStDeviation);
		}
		
		//create connected components
		Map<Point, ConnectedComponent> connectedComponents = new HashMap<Point, ConnectedComponent>();
		for(Point point: graph.getPoints()) {
			connectedComponents.put(point, ConnectedComponent.generateComponent(graph, point));
		}
		
		//phase 2
		for(Point point: graph.getPoints()) {
			List<ConnectedComponent> shortConnectedComponents = new ArrayList<ConnectedComponent>();
			//get short edges count and components connected by short edges
			int shortEdgesCount = 0;
			for(Edge edge: graph.getEdgesForPoint(point)) {
				if(edge.getType() == EdgeType.SHORT) {
					shortEdgesCount += 1;
					ConnectedComponent component = connectedComponents.get(edge.getSecondPoint());
					if(!shortConnectedComponents.contains(component)) {
						shortConnectedComponents.add(component);
					}
				}
			}
			
			//this phase is applied only to points with short edges
			if(shortEdgesCount > 0) {
				ConnectedComponent maxShortConnectedComponent = shortConnectedComponents.get(0);
				
				//find maximal short connected component if more than 1 component
				if(shortConnectedComponents.size() > 1) {
					for(ConnectedComponent shortConnectedComponent: shortConnectedComponents) {
						if(shortConnectedComponent.getPoints().size() > maxShortConnectedComponent.getPoints().size()) {
							maxShortConnectedComponent = shortConnectedComponent;
						}
					}
				}
				
				//remove other edges which do not point to selected component
				for(Edge edge: graph.getEdgesForPoint(point)) {
					if(edge.getType() == EdgeType.OTHER) {
						ConnectedComponent otherEdgeConnectedComponent = connectedComponents.get(edge.getSecondPoint());
						if(!otherEdgeConnectedComponent.equals(maxShortConnectedComponent)) {
							edge.setRemoved(true);
						}
					}
				}
				
				//restore short edges which connectes to selected component
				for(Edge edge: graph.getEdgesForPoint(point)) {
					if(edge.getType() == EdgeType.SHORT && maxShortConnectedComponent.getPoints().contains(edge.getSecondPoint())) {
						edge.setRemoved(false);
					}
				}
			}
		}
		
		//phase 3
		for(Point point: graph.getPoints()) {
			Set<Edge> edgesWithinTwo = new HashSet<Edge>(graph.getEdgesForPoint(point));
			for(Edge edge: graph.getEdgesForPoint(point)) {
				for(Edge edge2: graph.getEdgesForPoint(edge.getSecondPoint())) {
					edgesWithinTwo.add(edge2);
				}
			}
			
			//find local mean for edges within two
			double localDistSum = 0;
			for(Edge edge: edgesWithinTwo) {
				localDistSum += edge.getDistance();
			}
			double localMeanWithinTwo = localDistSum / edgesWithinTwo.size();
			
			//remove long edges within two
			for(Edge edge: edgesWithinTwo) {
				if(edge.getDistance() > localMeanWithinTwo + meanStDeviation) {
					edge.setRemoved(true);
				}
			}
		}
	}

	private void classifyEdges(Point point, EdgesStatisticalData data, double meanStDeviation) {
		Set<Edge> edges = graph.getEdgesForPoint(point);
		
		//edge classification based on local mean and mean st. deviation
		for(Edge edge: edges) {
			if(edge.isRemoved()) {
				continue;
			}
			if(edge.getDistance() < data.getLocalMean() - meanStDeviation) {
				edge.setType(EdgeType.SHORT);
				edge.setRemoved(true);
			} else if(edge.getDistance() > data.getLocalMean() + meanStDeviation) {
				edge.setType(EdgeType.LONG);
				edge.setRemoved(true);
			} else {
				edge.setType(EdgeType.OTHER);
			}
		}
	}

	private EdgesStatisticalData getEdgesStatisticalData(Point point) {
		Set<Edge> edges = graph.getEdgesForPoint(point);
		int edgesCount = edges.size();
		
		//get local mean for edges distance
		double distanceSum = 0;
		for(Edge edge: edges) {
			distanceSum += edge.getDistance();
		}
		final double localMean = distanceSum / edgesCount;
				
		//get local standard deviation
		double localVariance = 0;
		for(Edge edge: edges) {
			localVariance += Math.pow(localMean - edge.getDistance(), 2) / edgesCount;
		}
		final double localStDeviation = Math.sqrt(localVariance);
		
		return new EdgesStatisticalData() {
			
			public double getLocalStDeviation() {
				return localStDeviation;
			}
			
			public double getLocalMean() {
				return localMean;
			}
		};
	}	
}
