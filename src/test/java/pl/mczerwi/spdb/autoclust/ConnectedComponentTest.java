package pl.mczerwi.spdb.autoclust;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pl.mczerwi.spdb.autoclust.ConnectedComponent;
import pl.mczerwi.spdb.model.Edge;
import pl.mczerwi.spdb.model.Graph;
import pl.mczerwi.spdb.model.Point;

public class ConnectedComponentTest {

	private Graph graph = new Graph();
	
	@Before
	public void setUp() {
		for(Edge edge: DelaunayTestData.getEdges()) {
			graph.addEdge(edge);
		}
	}
	
	@Test
	public void shouldGenerateCorrectComponent() {
		ConnectedComponent component = ConnectedComponent.generateComponent(graph, DelaunayTestData.P1);
		assertEquals(4, component.getPoints().size());
		for(Point point: DelaunayTestData.getPolygonPoints()) {
			assertTrue(component.getPoints().contains(point));
		}
	}
	
	@Test
	public void shouldGenerateEqualComponents() {
		ConnectedComponent component = ConnectedComponent.generateComponent(graph, DelaunayTestData.P1);
		ConnectedComponent component2 = ConnectedComponent.generateComponent(graph, DelaunayTestData.P4);
		
		assertEquals(component, component2);
	}
}
