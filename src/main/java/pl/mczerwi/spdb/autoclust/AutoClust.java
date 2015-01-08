package pl.mczerwi.spdb.autoclust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import pl.mczerwi.spdb.model.Edge;
import pl.mczerwi.spdb.model.EdgeType;
import pl.mczerwi.spdb.model.Graph;
import pl.mczerwi.spdb.model.Point;

public class AutoClust {
	
	private final Logger logger = Logger.getLogger(this.getClass());

	private Graph graph;
	
	public AutoClust() {	
	}
	
	public void clusterGraph(Graph graph) {
		logger.info("Starting AUTOCLUST");
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
		logger.trace("Mean standard deviation: " + meanStDeviation);
		
		for(Point point: graph.getPoints()) {
			classifyEdges(point, edgesStatisticalDataMap.get(point), meanStDeviation);
		}
		
		//create connected components
		Map<Point, ConnectedComponent> connectedComponents = ConnectedComponent.generateConnectedComponents(graph);
		logger.info("Phase 1 finished");
		
		//phase 2
		for(Point point: graph.getPoints()) {
			List<ConnectedComponent> shortConnectedComponents = new ArrayList<ConnectedComponent>();
			//get short edges count and components connected by short edges
			for(Edge edge: graph.getOutgoingEdgesForPoint(point)) {
				if(edge.getType() == EdgeType.SHORT) {
					ConnectedComponent component = connectedComponents.get(edge.getSecondPoint());
					if(!component.isTrivial() && !shortConnectedComponents.contains(component)) {
						shortConnectedComponents.add(component);
					}
				}
			}
			
			//this phase is applied only to points with short connected non-trivial components
			if(shortConnectedComponents.size() > 0) {
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
				for(Edge edge: graph.getOutgoingEdgesForPoint(point)) {
					if(edge.getType() == EdgeType.OTHER) {
						ConnectedComponent otherEdgeConnectedComponent = connectedComponents.get(edge.getSecondPoint());
						if(!otherEdgeConnectedComponent.equals(maxShortConnectedComponent)) {
							edge.setRemoved(true);
						}
					}
				}
				
				//restore short edges which connect to selected component
				for(Edge edge: graph.getOutgoingEdgesForPoint(point)) {
					if(edge.getType() == EdgeType.SHORT && maxShortConnectedComponent.getPoints().contains(edge.getSecondPoint())) {
						edge.setRemoved(false);
					}
				}
			}
		}
		logger.info("Phase 2 finished");
		
//		phase 3
		for(Point point: graph.getPoints()) {
			Set<Edge> edgesWithinTwo = new HashSet<Edge>(graph.getOutgoingEdgesForPoint(point));
			for(Edge edge: graph.getOutgoingEdgesForPoint(point)) {
				for(Edge edge2: graph.getOutgoingEdgesForPoint(edge.getSecondPoint())) {
					if(edge2.getSecondPoint() != point) {
						edgesWithinTwo.add(edge2);
					}
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
		logger.info("Phase 3 finished");
	}

	private void classifyEdges(Point point, EdgesStatisticalData data, double meanStDeviation) {
		Set<Edge> edges = graph.getOutgoingEdgesForPoint(point);
		
		logger.trace("Edge classification for point " + point.getId());
		logger.trace("Local mean is " + data.getLocalMean());
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
			logger.trace("Edge " + edge + " classified as " + edge.getType().name());
		}
	}

	private EdgesStatisticalData getEdgesStatisticalData(Point point) {
		Set<Edge> edges = graph.getOutgoingEdgesForPoint(point);
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
