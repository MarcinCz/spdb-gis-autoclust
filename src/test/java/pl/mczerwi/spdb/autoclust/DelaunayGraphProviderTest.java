package pl.mczerwi.spdb.autoclust;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import pl.mczerwi.spdb.autoclust.DelaunayGraphProvider;
import pl.mczerwi.spdb.dao.PointsDAO;
import pl.mczerwi.spdb.model.Graph;

public class DelaunayGraphProviderTest {

	private DelaunayGraphProvider testee;
	private PointsDAO pointsDAO = mock(PointsDAO.class);
	
	@Before
	public void setUp() {
		testee = new DelaunayGraphProvider();
		testee.setPointsDao(pointsDAO);
	}
	
	@Test
	public void shouldCreateCorrectDelaunayGraph() {
		when(pointsDAO.getDelaunayTriangulationPolygonPoints()).thenReturn(DelaunayTestData.getPolygonPoints());
		
		Graph graph = testee.getDelaunayGraph();
		
		assertEquals(5, graph.getPoints().size());
		assertEquals(2, graph.getOutgoingEdgesForPoint(DelaunayTestData.P1).size());
		assertEquals(4, graph.getOutgoingEdgesForPoint(DelaunayTestData.P2).size());
		assertEquals(3, graph.getOutgoingEdgesForPoint(DelaunayTestData.P3).size());
		assertEquals(3, graph.getOutgoingEdgesForPoint(DelaunayTestData.P4).size());
		assertEquals(2, graph.getOutgoingEdgesForPoint(DelaunayTestData.P5).size());
	}
}
